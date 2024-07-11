package com.example.storeapp.data.models.detail

import com.google.gson.annotations.SerializedName

class ResponseProductFeatures : ArrayList<ResponseProductFeatures.ResponseProductFeaturesItem>(){
    data class ResponseProductFeaturesItem(
        @SerializedName("featureItem_title")
        val featureItemTitle: String?, // 6.0 اینچ و بزرگتر
        @SerializedName("feature_title")
        val featureTitle: String? // اندازه صفحه نمایش
    )
}