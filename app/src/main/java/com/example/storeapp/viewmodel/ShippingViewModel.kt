package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.address.BodySetAddressForShipping
import com.example.storeapp.data.models.shipping.BodyCoupon
import com.example.storeapp.data.models.shipping.ResponseCoupon
import com.example.storeapp.data.models.shipping.ResponseShipping
import com.example.storeapp.data.models.wallet.ResponseIncreaseWallet
import com.example.storeapp.data.repository.ShippingRepository
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.utils.network.NetworkResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingViewModel @Inject constructor(private val repository: ShippingRepository) : ViewModel() {
    //Shipping
    private val _shippingData = MutableLiveData<NetworkRequest<ResponseShipping>>()
    val shippingData: LiveData<NetworkRequest<ResponseShipping>> = _shippingData

    fun callShippingApi() = viewModelScope.launch {
        _shippingData.value = NetworkRequest.Loading()
        val response = repository.getShipping()
        _shippingData.value = NetworkResponse(response).generateResponse()
    }

    //Set Address
    fun callSetAddressApi(body: BodySetAddressForShipping) = viewModelScope.launch {
        repository.putSetAddress(body)
    }

    //Check coupon
    private val _couponData = MutableLiveData<NetworkRequest<ResponseCoupon>>()
    val couponData: LiveData<NetworkRequest<ResponseCoupon>> = _couponData

    fun callCheckCouponApi(body: BodyCoupon) = viewModelScope.launch {
        _couponData.value = NetworkRequest.Loading()
        val response = repository.postCoupon(body)
        _couponData.value = NetworkResponse(response).generateResponse()
    }

    //Payment
    private val _paymentData = MutableLiveData<NetworkRequest<ResponseIncreaseWallet>>()
    val paymentData: LiveData<NetworkRequest<ResponseIncreaseWallet>> = _paymentData

    fun callPaymentApi(body: BodyCoupon) = viewModelScope.launch {
        _paymentData.value = NetworkRequest.Loading()
        val response = repository.postPayment(body)
        _paymentData.value = NetworkResponse(response).generateResponse()
    }
}