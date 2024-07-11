package com.example.storeapp.data.repository

import com.example.storeapp.data.models.wallet.BodyIncreaseWallet
import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class WalletRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getWallet() = api.getWallet()
    suspend fun postIncreaseWallet(body: BodyIncreaseWallet) = api.postIncreaseWallet(body)
}