package com.example.storeapp.data.models.wallet

import com.google.gson.annotations.SerializedName

data class ResponseIncreaseWallet(
    @SerializedName("Authority")
    val authority: String?, // 000000000000000000000000000001164547
    @SerializedName("StartPay")
    val startPay: String?, // https://sandbox.zarinpal.com/pg/StartPay/000000000000000000000000000001164547
    @SerializedName("message")
    val message: String?
)