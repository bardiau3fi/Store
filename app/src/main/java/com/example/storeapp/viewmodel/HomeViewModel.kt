package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.home.ResponseBanners
import com.example.storeapp.data.models.home.ResponseDiscount
import com.example.storeapp.data.repository.HomeRepository
import com.example.storeapp.utils.ELECTRONIC_DEVICES
import com.example.storeapp.utils.GENERAL
import com.example.storeapp.utils.NEW
import com.example.storeapp.utils.ProductsCategories
import com.example.storeapp.utils.SORT
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.utils.network.NetworkResponse
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            delay(300)
            callBannerApi()
            callDiscountApi()
        }
    }

    //Save state
    var lastStateOfScroll: Parcelable? = null

    //Banner
    private val _bannerData = MutableLiveData<NetworkRequest<ResponseBanners>>()
    val bannerData: LiveData<NetworkRequest<ResponseBanners>> = _bannerData

    private fun callBannerApi() = viewModelScope.launch {
        _bannerData.value = NetworkRequest.Loading()
        val response = repository.getBannersList(GENERAL)
        _bannerData.value = NetworkResponse(response).generateResponse()
    }

    //Discount
    private val _discountData = MutableLiveData<NetworkRequest<ResponseDiscount>>()
    val discountData: LiveData<NetworkRequest<ResponseDiscount>> = _discountData

    private fun callDiscountApi() = viewModelScope.launch {
        _discountData.value = NetworkRequest.Loading()
        val response = repository.getDiscountList(ELECTRONIC_DEVICES)
        _discountData.value = NetworkResponse(response).generateResponse()
    }

    //Products
    private fun productsQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[SORT] = NEW
        return queries
    }

    private fun callProductsApi(category: ProductsCategories) = liveData {
        val cats = category.item
        emit(NetworkRequest.Loading())
        val response = repository.getProductsList(cats, productsQueries())
        emit(NetworkResponse(response).generateResponse())
    }

    private val categoriesName = ProductsCategories.values()
        .associateWith {
            callProductsApi(it)
        }

    fun getProductsData(category: ProductsCategories) = categoriesName.getValue(category)
}