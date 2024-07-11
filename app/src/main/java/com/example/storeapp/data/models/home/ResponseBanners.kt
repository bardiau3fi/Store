package com.example.storeapp.data.models.home

import com.google.gson.annotations.SerializedName

class ResponseBanners : ArrayList<ResponseBanners.ResponseBannersItem>(){
    data class ResponseBannersItem(
        @SerializedName("image")
        val image: String?, // media/images/banners/YO2JkbTL0NQvlBMmpYjYVGo5LnlIQvQsGrcxdfqa.jpg
        @SerializedName("link")
        val link: String?, // product
        @SerializedName("link_id")
        val linkId: String?, // 61
        @SerializedName("title")
        val title: String?, // مانیتور ایسوس مدل XG32VC
        @SerializedName("url_link")
        val urlLink: UrlLink?
    ) {
        data class UrlLink(
            @SerializedName("id")
            val id: Int?, // 61
            @SerializedName("parent_id")
            val parentId: String?, // 1
            @SerializedName("slug")
            val slug: String?, // category-mobile
            @SerializedName("title")
            val title: String? // مانیتور ایسوس مدل XG32VC سایز 31.5 اینچ
        )
    }
}