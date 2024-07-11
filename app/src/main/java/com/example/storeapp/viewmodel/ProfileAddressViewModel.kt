package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.address.BodySubmitAddress
import com.example.storeapp.data.models.address.ResponseProfileAddresses
import com.example.storeapp.data.models.address.ResponseProvinceList
import com.example.storeapp.data.models.address.ResponseSubmitAddress
import com.example.storeapp.data.models.profile_comments.ResponseDeleteComment
import com.example.storeapp.data.repository.ProfileAddressRepository
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
class ProfileAddressViewModel @Inject constructor(private val repository: ProfileAddressRepository) : ViewModel() {
    //Addresses list
    private val _addressesListData = MutableLiveData<NetworkRequest<ResponseProfileAddresses>>()
    val addressesListData: LiveData<NetworkRequest<ResponseProfileAddresses>> = _addressesListData

    fun callAddressesListApi() = viewModelScope.launch {
        _addressesListData.value = NetworkRequest.Loading()
        val response = repository.getProfileAddressesList()
        _addressesListData.value = NetworkResponse(response).generateResponse()
    }

    //Province list
    private val _provincesData = MutableLiveData<NetworkRequest<ResponseProvinceList>>()
    val provincesData: LiveData<NetworkRequest<ResponseProvinceList>> = _provincesData

    fun callProvincesListApi() = viewModelScope.launch {
        _provincesData.value = NetworkRequest.Loading()
        val response = repository.getProvinceList()
        _provincesData.value = NetworkResponse(response).generateResponse()
    }

    //City list
    private val _citiesData = MutableLiveData<NetworkRequest<ResponseProvinceList>>()
    val citiesData: LiveData<NetworkRequest<ResponseProvinceList>> = _citiesData

    fun callCitiesListApi(id: Int) = viewModelScope.launch {
        _citiesData.value = NetworkRequest.Loading()
        val response = repository.getCityList(id)
        _citiesData.value = NetworkResponse(response).generateResponse()
    }

    //City list
    private val _submitAddressData = MutableLiveData<NetworkRequest<ResponseSubmitAddress>>()
    val submitAddressData: LiveData<NetworkRequest<ResponseSubmitAddress>> = _submitAddressData

    fun callSubmitAddressApi(body: BodySubmitAddress) = viewModelScope.launch {
        _submitAddressData.value = NetworkRequest.Loading()
        val response = repository.postSubmitAddress(body)
        _submitAddressData.value = NetworkResponse(response).generateResponse()
    }

    //Delete address
    private val _deleteAddressData = MutableLiveData<NetworkRequest<ResponseDeleteComment>>()
    val deleteAddressData: LiveData<NetworkRequest<ResponseDeleteComment>> = _deleteAddressData

    fun callDeleteAddressApi(id: Int) = viewModelScope.launch {
        _deleteAddressData.value = NetworkRequest.Loading()
        val response = repository.deleteProfileAddress(id)
        _deleteAddressData.value = NetworkResponse(response).generateResponse()
    }
}