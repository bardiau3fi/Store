package com.example.storeapp.data.repository

import com.example.storeapp.R
import com.example.storeapp.data.models.search_filter.FilterModel
import com.example.storeapp.utils.*
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchFilterRepository @Inject constructor(@ApplicationContext private val context: Context) {
    fun fillFilterData(): MutableList<FilterModel> {
        val list = mutableListOf<FilterModel>()
        list.add(FilterModel(1, context.getString(R.string.filterNew), NEW))
        list.add(FilterModel(2, context.getString(R.string.filterExpensive), EXPENSIVE))
        list.add(FilterModel(3, context.getString(R.string.filterCheep), CHEEP))
        list.add(FilterModel(4, context.getString(R.string.filterRate), RATE))
        list.add(FilterModel(5, context.getString(R.string.filterSell), SELL))
        list.add(FilterModel(6, context.getString(R.string.filterHits), HITS))
        return list
    }
}