package com.example.storeapp.data.models.profile_favorite


import com.google.gson.annotations.SerializedName

data class ResponseProfileFavorites(
    @SerializedName("current_page")
    val currentPage: Int?, // 1
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("first_page_url")
    val firstPageUrl: String?,
    @SerializedName("from")
    val from: Int?, // 1
    @SerializedName("last_page")
    val lastPage: Int?, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String?,
    @SerializedName("links")
    val links: List<Link?>?,
    @SerializedName("next_page_url")
    val nextPageUrl: Any?, // null
    @SerializedName("path")
    val path: String?,
    @SerializedName("per_page")
    val perPage: Int?, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any?, // null
    @SerializedName("to")
    val to: Int?, // 5
    @SerializedName("total")
    val total: Int? // 5
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String?, // 2023-06-08T08:17:43.000000Z
        @SerializedName("id")
        val id: Int?, // 115
        @SerializedName("likeable")
        val likeable: Likeable?,
        @SerializedName("likeable_id")
        val likeableId: String?, // 62
        @SerializedName("likeable_type")
        val likeableType: String?, // App\Models\Product
        @SerializedName("updated_at")
        val updatedAt: String?, // 2023-06-08T08:17:43.000000Z
        @SerializedName("user_id")
        val userId: String? // 1
    ) {
        data class Likeable(
            @SerializedName("discount_rate")
            val discountRate: String?, // 25
            @SerializedName("discounted_price")
            val discountedPrice: Int?, // 0
            @SerializedName("end_time")
            val endTime: String?, // 2024-03-19T09:37:00.000000Z
            @SerializedName("final_price")
            val finalPrice: Int?, // 11190000
            @SerializedName("id")
            val id: Int?, // 62
            @SerializedName("image")
            val image: String?, // /storage/cache/600-0-0-width-qmwHY5OV12kOBAMlr5MpWW4VOdVoF5QsAMPt72vW.jpg
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

    data class Link(
        @SerializedName("active")
        val active: Boolean?, // false
        @SerializedName("label")
        val label: String?, // &laquo; قبلی
        @SerializedName("url")
        val url: String?
    )
}