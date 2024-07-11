package com.example.storeapp.data.repository

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class ProfileFavoritesRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProfileFavorites() = api.getProfileFavorites()
    suspend fun deleteProfileFavorite(id: Int) = api.deleteProfileFavorite(id)
}