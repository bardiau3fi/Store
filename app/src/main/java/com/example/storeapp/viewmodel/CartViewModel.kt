package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.SimpleResponse
import com.example.storeapp.data.models.cart.BodyAddToCart
import com.example.storeapp.data.models.cart.ResponseCartList
import com.example.storeapp.data.models.cart.ResponseUpdateCart
import com.example.storeapp.data.repository.CartRepository
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
class CartViewModel @Inject constructor(private val repository: CartRepository) : ViewModel() {
    //Add to cart
    private val _addToCartData = MutableLiveData<NetworkRequest<SimpleResponse>>()
    val addToCartData: LiveData<NetworkRequest<SimpleResponse>> = _addToCartData

    fun callAddTOCartApi(id: Int, body: BodyAddToCart) = viewModelScope.launch {
        _addToCartData.value = NetworkRequest.Loading()
        val response = repository.postAddToCart(id, body)
        _addToCartData.value = NetworkResponse(response).generateResponse()
    }

    //Cart list
    private val _cartListData = MutableLiveData<NetworkRequest<ResponseCartList>>()
    val cartListData: LiveData<NetworkRequest<ResponseCartList>> = _cartListData

    fun callCartListApi() = viewModelScope.launch {
        _cartListData.value = NetworkRequest.Loading()
        val response = repository.getCartList()
        _cartListData.value = NetworkResponse(response).generateResponse()
    }

    //Increment
    private val _updateCartData = MutableLiveData<NetworkRequest<ResponseUpdateCart>>()
    val updateCartData: LiveData<NetworkRequest<ResponseUpdateCart>> = _updateCartData

    fun callIncrementCartApi(id: Int) = viewModelScope.launch {
        _updateCartData.value = NetworkRequest.Loading()
        val response = repository.putIncrementCart(id)
        _updateCartData.value = NetworkResponse(response).generateResponse()
    }

    //Decrement
    fun callDecrementDataCartApi(id: Int) = viewModelScope.launch {
        _updateCartData.value = NetworkRequest.Loading()
        val response = repository.putDecrementCart(id)
        _updateCartData.value = NetworkResponse(response).generateResponse()
    }

    //Delete
    fun callDeleteProductApi(id: Int) = viewModelScope.launch {
        _updateCartData.value = NetworkRequest.Loading()
        val response = repository.deleteProductFromCart(id)
        _updateCartData.value = NetworkResponse(response).generateResponse()
    }

    //Cart Continue
    private val _cartContinueData = MutableLiveData<NetworkRequest<ResponseCartList>>()
    val cartContinueData: LiveData<NetworkRequest<ResponseCartList>> = _cartContinueData

    fun callCartContinueApi() = viewModelScope.launch {
        _cartContinueData.value = NetworkRequest.Loading()
        val response = repository.getCartContinueList()
        _cartContinueData.value = NetworkResponse(response).generateResponse()
    }
}