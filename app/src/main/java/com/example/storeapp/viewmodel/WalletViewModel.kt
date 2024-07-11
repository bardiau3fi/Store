package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.profile.ResponseWallet
import com.example.storeapp.data.models.wallet.BodyIncreaseWallet
import com.example.storeapp.data.models.wallet.ResponseIncreaseWallet
import com.example.storeapp.data.repository.WalletRepository
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
class WalletViewModel @Inject constructor(private val repository: WalletRepository) : ViewModel() {

    //Wallet
    private val _walletData = MutableLiveData<NetworkRequest<ResponseWallet>>()
    val walletData: LiveData<NetworkRequest<ResponseWallet>> = _walletData

    fun callWalletApi() = viewModelScope.launch {
        _walletData.value = NetworkRequest.Loading()
        val response = repository.getWallet()
        _walletData.value = NetworkResponse(response).generateResponse()
    }

    //Increase
    private val _increaseWalletData = MutableLiveData<NetworkRequest<ResponseIncreaseWallet>>()
    val increaseWalletData: LiveData<NetworkRequest<ResponseIncreaseWallet>> = _increaseWalletData

    fun callIncreaseWalletApi(body: BodyIncreaseWallet) = viewModelScope.launch {
        _increaseWalletData.value = NetworkRequest.Loading()
        val response = repository.postIncreaseWallet(body)
        _increaseWalletData.value = NetworkResponse(response).generateResponse()
    }
}