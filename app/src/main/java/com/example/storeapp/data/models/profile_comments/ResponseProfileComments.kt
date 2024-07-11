package com.example.storeapp.data.models.profile_comments


import com.google.gson.annotations.SerializedName

data class ResponseProfileComments(
    @SerializedName("current_page")
    val currentPage: Int?, // 1
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("first_page_url")
    val firstPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/comments?page=1
    @SerializedName("from")
    val from: Int?, // 1
    @SerializedName("last_page")
    val lastPage: Int?, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/comments?page=1
    @SerializedName("links")
    val links: List<Link?>?,
    @SerializedName("next_page_url")
    val nextPageUrl: Any?, // null
    @SerializedName("path")
    val path: String?, // https://shop.nouri-api.ir/api/v1/auth/comments
    @SerializedName("per_page")
    val perPage: Int?, // 12
    @SerializedName("prev_page_url")
    val prevPageUrl: Any?, // null
    @SerializedName("to")
    val to: Int?, // 11
    @SerializedName("total")
    val total: Int? // 11
) {
    data class Data(
        @SerializedName("approved")
        val approved: String?, // 0
        @SerializedName("comment")
        val comment: String?, // خوبه
        @SerializedName("created_at")
        val createdAt: String?, // 2023-06-08T08:18:56.000000Z
        @SerializedName("id")
        val id: Int?, // 58
        @SerializedName("product")
        val product: Product?,
        @SerializedName("product_id")
        val productId: String?, // 62
        @SerializedName("rate")
        val rate: String?, // 4
        @SerializedName("updated_at")
        val updatedAt: String?, // 2023-06-08T08:18:56.000000Z
        @SerializedName("user_id")
        val userId: String? // 1
    ) {
        data class Product(
            @SerializedName("id")
            val id: Int?, // 62
            @SerializedName("image")
            val image: String?, // /storage/cache/600-0-0-width-600-0-0-width-600-0-0-width-qmwHY5OV12kOBAMlr5MpWW4VOdVoF5QsAMPt72vW.jpg
            @SerializedName("title")
            val title: String? // مانیتور ایسوس مدل VG27WQ سایز 27 اینچ
        )
    }

    data class Link(
        @SerializedName("active")
        val active: Boolean?, // false
        @SerializedName("label")
        val label: String?, // &laquo; قبلی
        @SerializedName("url")
        val url: String? // https://shop.nouri-api.ir/api/v1/auth/comments?page=1
    )
}