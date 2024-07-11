package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.search.ResponseSearch
import com.example.storeapp.data.models.search_filter.FilterModel
import com.example.storeapp.data.repository.SearchFilterRepository
import com.example.storeapp.data.repository.SearchRepository
import com.example.storeapp.utils.Q
import com.example.storeapp.utils.SORT
import com.example.storeapp.utils.network.NetworkRequest
import com.example.storeapp.utils.network.NetworkResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository, private val filterRepository: SearchFilterRepository
) : ViewModel() {
    //Search
    fun searchQueries(search: String, sort: String): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Q] = search
        queries[SORT] = sort
        return queries
    }

    private val _searchData = MutableLiveData<NetworkRequest<ResponseSearch>>()
    val searchData: LiveData<NetworkRequest<ResponseSearch>> = _searchData

    fun callSearchApi(queries: Map<String, String>) = viewModelScope.launch {
        _searchData.value = NetworkRequest.Loading()
        val response = repository.getSearchList(queries)
        _searchData.value = NetworkResponse(response).generateResponse()
    }

    //Filter
    private val _filterData = MutableLiveData<MutableList<FilterModel>>()
    val filterData: LiveData<MutableList<FilterModel>> = _filterData

    fun getFilterData() = viewModelScope.launch {
        _filterData.value = filterRepository.fillFilterData()
    }

    //Filter selected
    private val _filterSelectedItem = MutableLiveData<String>()
    val filterSelectedItem: LiveData<String> = _filterSelectedItem

    fun sendSelectedFilterItem(item: String) {
        _filterSelectedItem.value = item
    }
}