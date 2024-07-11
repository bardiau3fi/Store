package com.example.storeapp.data.repository

import com.example.storeapp.data.models.cart.BodyAddToCart
import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class CartRepository @Inject constructor(private val api: ApiServices) {
    suspend fun postAddToCart(id: Int, body: BodyAddToCart) = api.postAddToCart(id, body)
    suspend fun getCartList() = api.getCartList()
    suspend fun putIncrementCart(id: Int) = api.putIncrementCart(id)
    suspend fun putDecrementCart(id: Int) = api.putDecrementCart(id)
    suspend fun deleteProductFromCart(id: Int) = api.deleteProductFromCart(id)
    suspend fun getCartContinueList() = api.getCartContinueList()
}