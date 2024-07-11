package com.example.storeapp.data.models.categories

data class FilterCategoryModel(
    val sort: String? = null,
    val search: String? = null,
    val minPrice: String? = null,
    val maxPrice: String? = null,
    val available: Boolean? = null
)
