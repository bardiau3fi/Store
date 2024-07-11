package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.login.BodyLogin
import com.example.storeapp.data.models.login.ResponseLogin
import com.example.storeapp.data.models.login.ResponseVerify
import com.example.storeapp.data.repository.LoginRepository
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
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {
    //Login
    private val _loginData = MutableLiveData<NetworkRequest<ResponseLogin>>()
    val loginData: LiveData<NetworkRequest<ResponseLogin>> = _loginData

    fun callLoginApi(body: BodyLogin) = viewModelScope.launch {
        _loginData.value = NetworkRequest.Loading()
        val response = repository.postLogin(body)
        _loginData.value = NetworkResponse(response).generateResponse()
    }

    //Verify
    private val _verifyData = MutableLiveData<NetworkRequest<ResponseVerify>>()
    val verifyData: LiveData<NetworkRequest<ResponseVerify>> = _verifyData

    fun callVerifyApi(body: BodyLogin) = viewModelScope.launch {
        _verifyData.value = NetworkRequest.Loading()
        val response = repository.postVerify(body)
        _verifyData.value = NetworkResponse(response).generateResponse()
    }
}