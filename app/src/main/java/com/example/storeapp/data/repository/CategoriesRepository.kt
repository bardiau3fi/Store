package com.example.storeapp.data.repository

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getCategoriesList() = api.getCategoriesList()
}