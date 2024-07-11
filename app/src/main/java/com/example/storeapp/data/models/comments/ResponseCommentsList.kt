package com.example.storeapp.data.models.comments

import com.google.gson.annotations.SerializedName

data class ResponseCommentsList(
    @SerializedName("current_page")
    val currentPage: Int?, // 1
    @SerializedName("data")
    val `data`: List<Data>?,
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
    val perPage: Int?, // 8
    @SerializedName("prev_page_url")
    val prevPageUrl: Any?, // null
    @SerializedName("to")
    val to: Int?, // 8
    @SerializedName("total")
    val total: Int? // 8
) {
    data class Data(
        @SerializedName("comment")
        val comment: String?, // موبایل خوبیه
        @SerializedName("created_at")
        val createdAt: String?, // 2023-06-05T13:41:47.000000Z
        @SerializedName("id")
        val id: Int?, // 53
        @SerializedName("likes_count")
        val likesCount: String?, // 0
        @SerializedName("rate")
        val rate: String?, // 3
        @SerializedName("user")
        val user: User?,
        @SerializedName("user_id")
        val userId: String?, // 1
        @SerializedName("user_like")
        val userLike: String? // 0
    ) {
        data class User(
            @SerializedName("firstname")
            val firstname: String?, // بردیا
            @SerializedName("id")
            val id: Int?, // 1
            @SerializedName("lastname")
            val lastname: String? // یوسفی
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