package com.example.storeapp.data.models.cart

import com.google.gson.annotations.SerializedName

data class ResponseCartList(
    @SerializedName("delivery_price")
    val deliveryPrice: String?, // 0
    @SerializedName("discounted_rate")
    val discountedRate: Int?, // 18
    @SerializedName("final_price")
    val finalPrice: String?, // 25466500
    @SerializedName("id")
    val id: Int?, // 153
    @SerializedName("items_discount")
    val itemsDiscount: Int?, // 5692500
    @SerializedName("items_price")
    val itemsPrice: String?, // 25466500
    @SerializedName("order_items")
    val orderItems: List<OrderItem>?,
    @SerializedName("quantity")
    val quantity: String?, // 3
    @SerializedName("status")
    val status: String?, // add-to-cart
    @SerializedName("user_id")
    val userId: String? // 1
) {
    data class OrderItem(
        @SerializedName("approved")
        val approved: String?, // 1
        @SerializedName("color_hex_code")
        val colorHexCode: String?, // #c77b30
        @SerializedName("color_title")
        val colorTitle: String?, // برنز
        @SerializedName("discounted_price")
        val discountedPrice: String?, // 0
        @SerializedName("final_price")
        val finalPrice: String?, // 8389000
        @SerializedName("id")
        val id: Int?, // 317
        @SerializedName("order_id")
        val orderId: String?, // 153
        @SerializedName("price")
        val price: String?, // 8389000
        @SerializedName("product_guarantee")
        val productGuarantee: String?, // گارانتی ۱۸ ماهه سی تلکام
        @SerializedName("product_id")
        val productId: String?, // 26
        @SerializedName("product_image")
        val productImage: String?, // /storage/cache/600-0-0-width-bBuf5DUdzAjSYxENV6ssdGwGqckqzdZVnNVZcKhj.jpg
        @SerializedName("product_quantity")
        val productQuantity: String?, // 13
        @SerializedName("product_title")
        val productTitle: String?, // گوشی موبایل شیائومی مدل Redmi Note 10 pro M2101K6G
        @SerializedName("quantity")
        val quantity: String? // 1
    )
}