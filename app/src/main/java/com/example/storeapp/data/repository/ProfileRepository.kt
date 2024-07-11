package com.example.storeapp.data.repository

import com.example.storeapp.data.models.profile.BodyUpdateProfile
import com.example.storeapp.data.network.ApiServices
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProfileData() = api.getProfileData()
    suspend fun postUploadAvatar(body: RequestBody) = api.postUploadAvatar(body)
    suspend fun postUploadProfile(body: BodyUpdateProfile) = api.postUploadProfile(body)
}