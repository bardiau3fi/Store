package com.example.storeapp.data.models.home

import com.google.gson.annotations.SerializedName

class ResponseDiscount : ArrayList<ResponseDiscount.ResponseDiscountItem>(){
    data class ResponseDiscountItem(
        @SerializedName("discount_rate")
        val discountRate: String?, // 25
        @SerializedName("discounted_price")
        val discountedPrice: String?, // 3730000
        @SerializedName("end_time")
        val endTime: String?, // 2024-03-19T09:37:00.000000Z
        @SerializedName("final_price")
        val finalPrice: Int?, // 11190000
        @SerializedName("id")
        val id: Int?, // 62
        @SerializedName("image")
        val image: String?, // /storage/cache/400-0-0-width-qmwHY5OV12kOBAMlr5MpWW4VOdVoF5QsAMPt72vW.jpg
        @SerializedName("product_price")
        val productPrice: String?, // 14920000
        @SerializedName("quantity")
        val quantity: String?, // 5
        @SerializedName("status")
        val status: String?, // discount
        @SerializedName("title")
        val title: String? // مانیتور ایسوس مدل VG27WQ سایز 27 اینچ
    )
}