package com.example.storeapp.data.models.address

import com.google.gson.annotations.SerializedName

data class ResponseSetAddressForShipping(
    @SerializedName("address")
    val address: Address?,
    @SerializedName("message")
    val message: String? // آدرس دریافت بروزرسانی شد.
) {
    data class Address(
        @SerializedName("id")
        val id: Int?, // 7
        @SerializedName("postal_address")
        val postalAddress: String?, // تهران - خیابان اینجا
        @SerializedName("receiver_cellphone")
        val receiverCellphone: String?, // 09121112233
        @SerializedName("receiver_firstname")
        val receiverFirstname: String?, // بردیا
        @SerializedName("receiver_lastname")
        val receiverLastname: String? // یوسفی
    )
}