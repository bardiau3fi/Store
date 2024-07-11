package com.example.storeapp.data.models.comments

import com.google.gson.annotations.SerializedName

data class BodySendComment(
    @SerializedName("comment")
    var comment: String? = null, // محصول خوبی بود، خیلی ممنون
    @SerializedName("rate")
    var rate: String? = null // 5
)