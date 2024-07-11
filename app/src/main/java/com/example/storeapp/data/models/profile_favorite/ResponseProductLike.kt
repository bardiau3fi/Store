package com.example.storeapp.data.models.profile_favorite


import com.google.gson.annotations.SerializedName

data class ResponseProductLike(
    @SerializedName("count")
    val count: Int?, // 1
    @SerializedName("message")
    val message: String? // add_favorite
)