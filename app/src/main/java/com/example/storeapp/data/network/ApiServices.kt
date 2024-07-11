package com.example.storeapp.data.network

import com.example.storeapp.data.models.SimpleResponse
import com.example.storeapp.data.models.address.BodySetAddressForShipping
import com.example.storeapp.data.models.address.BodySubmitAddress
import com.example.storeapp.data.models.address.ResponseProfileAddresses
import com.example.storeapp.data.models.address.ResponseProvinceList
import com.example.storeapp.data.models.address.ResponseSetAddressForShipping
import com.example.storeapp.data.models.address.ResponseSubmitAddress
import com.example.storeapp.data.models.cart.BodyAddToCart
import com.example.storeapp.data.models.cart.ResponseCartList
import com.example.storeapp.data.models.cart.ResponseUpdateCart
import com.example.storeapp.data.models.categories.ResponseCategories
import com.example.storeapp.data.models.comments.BodySendComment
import com.example.storeapp.data.models.comments.ResponseCommentsList
import com.example.storeapp.data.models.detail.ResponseDetail
import com.example.storeapp.data.models.detail.ResponseProductFeatures
import com.example.storeapp.data.models.detail.ResponseProductPriceChart
import com.example.storeapp.data.models.home.ResponseBanners
import com.example.storeapp.data.models.home.ResponseDiscount
import com.example.storeapp.data.models.home.ResponseProducts
import com.example.storeapp.data.models.login.BodyLogin
import com.example.storeapp.data.models.login.ResponseLogin
import com.example.storeapp.data.models.login.ResponseVerify
import com.example.storeapp.data.models.profile.BodyUpdateProfile
import com.example.storeapp.data.models.profile.ResponseProfile
import com.example.storeapp.data.models.profile.ResponseWallet
import com.example.storeapp.data.models.profile_comments.ResponseDeleteComment
import com.example.storeapp.data.models.profile_comments.ResponseProfileComments
import com.example.storeapp.data.models.profile_favorite.ResponseProductLike
import com.example.storeapp.data.models.profile_favorite.ResponseProfileFavorites
import com.example.storeapp.data.models.profile_order.ResponseProfileOrdersList
import com.example.storeapp.data.models.search.ResponseSearch
import com.example.storeapp.data.models.shipping.BodyCoupon
import com.example.storeapp.data.models.shipping.ResponseCoupon
import com.example.storeapp.data.models.shipping.ResponseShipping
import com.example.storeapp.data.models.wallet.BodyIncreaseWallet
import com.example.storeapp.data.models.wallet.ResponseIncreaseWallet
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiServices {
    @POST("auth/login")
    suspend fun postLogin(@Body body: BodyLogin): Response<ResponseLogin>

    @POST("auth/login/verify")
    suspend fun postVerify(@Body body: BodyLogin): Response<ResponseVerify>

    @GET("auth/detail")
    suspend fun getProfileData(): Response<ResponseProfile>

    @GET("ad/swiper/{slug}")
    suspend fun getBannersList(@Path("slug") slug: String): Response<ResponseBanners>

    @GET("offers/discount/{slug}")
    suspend fun getDiscountList(@Path("slug") slug: String): Response<ResponseDiscount>

    @GET("category/pro/{slug}")
    suspend fun getProductsList(@Path("slug") slug: String, @QueryMap queries: Map<String, String>): Response<ResponseProducts>

    @GET("search")
    suspend fun getSearchList(@QueryMap queries: Map<String, String>): Response<ResponseSearch>

    @GET("menu")
    suspend fun getCategoriesList(): Response<ResponseCategories>

    @GET("auth/wallet")
    suspend fun getWallet(): Response<ResponseWallet>

    @POST("auth/avatar")
    suspend fun postUploadAvatar(@Body body: RequestBody): Response<Unit>

    @PUT("auth/update")
    suspend fun postUploadProfile(@Body body: BodyUpdateProfile): Response<ResponseProfile>

    @POST("auth/wallet")
    suspend fun postIncreaseWallet(@Body body: BodyIncreaseWallet): Response<ResponseIncreaseWallet>

    @GET("auth/comments")
    suspend fun getProfileComments(): Response<ResponseProfileComments>

    @DELETE("auth/comment/{id}")
    suspend fun deleteProfileComment(@Path("id") id: Int): Response<ResponseDeleteComment>

    @GET("auth/favorites")
    suspend fun getProfileFavorites(): Response<ResponseProfileFavorites>

    @DELETE("auth/favorite/{id}")
    suspend fun deleteProfileFavorite(@Path("id") id: Int): Response<ResponseDeleteComment>

    @GET("auth/addresses")
    suspend fun getProfileAddressesList(): Response<ResponseProfileAddresses>

    @GET("auth/address/provinces")
    suspend fun getProvinceList(): Response<ResponseProvinceList>

    @GET("auth/address/provinces")
    suspend fun getCityList(@Query("provinceId") id: Int): Response<ResponseProvinceList>

    @POST("auth/address")
    suspend fun postSubmitAddress(@Body body: BodySubmitAddress): Response<ResponseSubmitAddress>

    @DELETE("auth/address/remove/{id}")
    suspend fun deleteProfileAddress(@Path("id") id: Int): Response<ResponseDeleteComment>

    @GET("auth/orders")
    suspend fun getProfileOrdersList(@Query("status") status: String): Response<ResponseProfileOrdersList>

    @GET("product/{id}")
    suspend fun getDetailProduct(@Path("id") id: Int): Response<ResponseDetail>

    @POST("product/{id}/like")
    suspend fun postLikeProduct(@Path("id") id: Int): Response<ResponseProductLike>

    @POST("product/{id}/add_to_cart")
    suspend fun postAddToCart(@Path("id") id: Int, @Body body: BodyAddToCart): Response<SimpleResponse>

    @GET("product/{id}/features")
    suspend fun getDetailFeatures(@Path("id") id: Int): Response<ResponseProductFeatures>

    @GET("product/{id}/comments")
    suspend fun getDetailComments(@Path("id") id: Int): Response<ResponseCommentsList>

    @POST("product/{id}/comments")
    suspend fun postAddNewComment(@Path("id") id: Int, @Body body: BodySendComment): Response<SimpleResponse>

    @GET("product/{id}/price-chart")
    suspend fun getDetailPriceChart(@Path("id") id: Int): Response<ResponseProductPriceChart>

    @GET("cart")
    suspend fun getCartList(): Response<ResponseCartList>

    @PUT("cart/{id}/increment")
    suspend fun putIncrementCart(@Path("id") id: Int): Response<ResponseUpdateCart>

    @PUT("cart/{id}/decrement")
    suspend fun putDecrementCart(@Path("id") id: Int): Response<ResponseUpdateCart>

    @DELETE("cart/{id}")
    suspend fun deleteProductFromCart(@Path("id") id: Int): Response<ResponseUpdateCart>

    @GET("cart/continue")
    suspend fun getCartContinueList(): Response<ResponseCartList>

    @GET("shipping")
    suspend fun getShipping(): Response<ResponseShipping>

    @PUT("shipping/set/address")
    suspend fun putSetAddress(@Body body: BodySetAddressForShipping): Response<ResponseSetAddressForShipping>

    @POST("check/coupon")
    suspend fun postCoupon(@Body body: BodyCoupon): Response<ResponseCoupon>

    @POST("payment")
    suspend fun postPayment(@Body body: BodyCoupon): Response<ResponseIncreaseWallet>
}