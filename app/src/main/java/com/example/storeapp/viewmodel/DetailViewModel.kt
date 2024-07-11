package com.example.storeapp.viewmodel

import com.example.storeapp.data.models.SimpleResponse
import com.example.storeapp.data.models.comments.BodySendComment
import com.example.storeapp.data.models.comments.ResponseCommentsList
import com.example.storeapp.data.models.detail.ResponseDetail
import com.example.storeapp.data.models.detail.ResponseProductFeatures
import com.example.storeapp.data.models.detail.ResponseProductPriceChart
import com.example.storeapp.data.models.profile_favorite.ResponseProductLike
import com.example.storeapp.data.repository.DetailRepository
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
class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel() {
    //Detail
    private val _detailData = MutableLiveData<NetworkRequest<ResponseDetail>>()
    val detailData: LiveData<NetworkRequest<ResponseDetail>> = _detailData

    fun callDetailApi(id: Int) = viewModelScope.launch {
        _detailData.value = NetworkRequest.Loading()
        val response = repository.getDetailProduct(id)
        _detailData.value = NetworkResponse(response).generateResponse()
    }

    //Product ID
    private val _productID = MutableLiveData<Int>()
    val productID: LiveData<Int> = _productID

    fun setProductId(id: Int) {
        _productID.value = id
    }

    //Like
    private val _likeData = MutableLiveData<NetworkRequest<ResponseProductLike>>()
    val likeData: LiveData<NetworkRequest<ResponseProductLike>> = _likeData

    fun callProductLike(id: Int) = viewModelScope.launch {
        _likeData.value = NetworkRequest.Loading()
        val response = repository.postLikeProduct(id)
        _likeData.value = NetworkResponse(response).generateResponse()
    }

    //Features
    private val _featuresData = MutableLiveData<NetworkRequest<ResponseProductFeatures>>()
    val featuresData: LiveData<NetworkRequest<ResponseProductFeatures>> = _featuresData

    fun callProductFeatures(id: Int) = viewModelScope.launch {
        _featuresData.value = NetworkRequest.Loading()
        val response = repository.getDetailFeatures(id)
        _featuresData.value = NetworkResponse(response).generateResponse()
    }

    //Comments
    private val _commentsData = MutableLiveData<NetworkRequest<ResponseCommentsList>>()
    val commentsData: LiveData<NetworkRequest<ResponseCommentsList>> = _commentsData

    fun callProductComments(id: Int) = viewModelScope.launch {
        _commentsData.value = NetworkRequest.Loading()
        val response = repository.getDetailComments(id)
        _commentsData.value = NetworkResponse(response).generateResponse()
    }

    //Send Comments
    private val _sendCommentsData = MutableLiveData<NetworkRequest<SimpleResponse>>()
    val sendCommentsData: LiveData<NetworkRequest<SimpleResponse>> = _sendCommentsData

    fun callAddNewComment(id: Int, body: BodySendComment) = viewModelScope.launch {
        _sendCommentsData.value = NetworkRequest.Loading()
        val response = repository.postAddNewComment(id, body)
        _sendCommentsData.value = NetworkResponse(response).generateResponse()
    }

    //Price chart
    private val _priceChartData = MutableLiveData<NetworkRequest<ResponseProductPriceChart>>()
    val priceChartData: LiveData<NetworkRequest<ResponseProductPriceChart>> = _priceChartData

    fun callProductPriceChart(id: Int) = viewModelScope.launch {
        _priceChartData.value = NetworkRequest.Loading()
        val response = repository.getDetailPriceChart(id)
        _priceChartData.value = NetworkResponse(response).generateResponse()
    }
}