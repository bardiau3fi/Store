package com.example.storeapp.data.models

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("message")
    val message: String? // با موفقیت به سبد خرید اضافه شد.
)