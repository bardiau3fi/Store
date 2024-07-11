package com.example.storeapp.data.models.detail

import com.google.gson.annotations.SerializedName

class ResponseProductPriceChart : ArrayList<ResponseProductPriceChart.ResponseProductPriceChartItem>(){
    data class ResponseProductPriceChartItem(
        @SerializedName("day")
        val day: String?, // 1402-06-13
        @SerializedName("price")
        val price: Int? // 6074100
    )
}