package com.example.storeapp.data.repository

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class ProfileOrdersRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProfileOrdersList(status: String) = api.getProfileOrdersList(status)
}