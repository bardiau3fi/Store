package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.profile_comments.ResponseDeleteComment
import com.example.storeapp.data.models.profile_favorite.ResponseProfileFavorites
import com.example.storeapp.data.repository.ProfileFavoritesRepository
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
class ProfileFavoritesViewModel @Inject constructor(private val repository: ProfileFavoritesRepository) : ViewModel() {
    //Favorites list
    private val _favoritesData = MutableLiveData<NetworkRequest<ResponseProfileFavorites>>()
    val favoritesData: LiveData<NetworkRequest<ResponseProfileFavorites>> = _favoritesData

    fun callFavoritesApi() = viewModelScope.launch {
        _favoritesData.value = NetworkRequest.Loading()
        val response = repository.getProfileFavorites()
        _favoritesData.value = NetworkResponse(response).generateResponse()
    }

    //Delete favorite
    private val _deleteFavoriteData = MutableLiveData<NetworkRequest<ResponseDeleteComment>>()
    val deleteFavoriteData: LiveData<NetworkRequest<ResponseDeleteComment>> = _deleteFavoriteData

    fun callDeleteFavoriteApi(id: Int) = viewModelScope.launch {
        _deleteFavoriteData.value = NetworkRequest.Loading()
        val response = repository.deleteProfileFavorite(id)
        _deleteFavoriteData.value = NetworkResponse(response).generateResponse()
    }
}