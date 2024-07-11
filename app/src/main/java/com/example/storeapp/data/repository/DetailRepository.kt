package com.example.storeapp.data.repository

import com.example.storeapp.data.models.comments.BodySendComment
import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class DetailRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getDetailProduct(id: Int) = api.getDetailProduct(id)
    suspend fun postLikeProduct(id: Int) = api.postLikeProduct(id)
    suspend fun getDetailFeatures(id: Int) = api.getDetailFeatures(id)
    suspend fun getDetailComments(id: Int) = api.getDetailComments(id)
    suspend fun postAddNewComment(id: Int, body: BodySendComment) = api.postAddNewComment(id, body)
    suspend fun getDetailPriceChart(id: Int) = api.getDetailPriceChart(id)
}