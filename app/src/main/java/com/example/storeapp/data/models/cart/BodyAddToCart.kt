package com.example.storeapp.data.models.cart

import com.google.gson.annotations.SerializedName

data class BodyAddToCart(
    @SerializedName("colorId")
    var colorId: String? = null // 2
)