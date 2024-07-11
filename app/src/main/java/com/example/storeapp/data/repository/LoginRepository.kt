package com.example.storeapp.data.repository

import com.example.storeapp.data.models.login.BodyLogin
import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class LoginRepository @Inject constructor(private val api: ApiServices) {
    suspend fun postLogin(body: BodyLogin) = api.postLogin(body)
    suspend fun postVerify(body: BodyLogin) = api.postVerify(body)
}