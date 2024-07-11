package com.example.storeapp.utils.di


import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.data.stored.SessionManager
import com.example.storeapp.utils.ACCEPT
import com.example.storeapp.utils.APPLICATION_JSON
import com.example.storeapp.utils.AUTHORIZATION
import com.example.storeapp.utils.BASE_URL
import com.example.storeapp.utils.BEARER
import com.example.storeapp.utils.CONNECTION_TIME
import com.example.storeapp.utils.NAMED_PING
import com.example.storeapp.utils.PING_INTERVAL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideClient(
        timeout: Long, @Named(NAMED_PING) ping: Long, session: SessionManager, interceptor: HttpLoggingInterceptor ) = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = runBlocking {
            session.getToken.first().toString()}

            chain.proceed(chain.request().newBuilder().also {
                it.addHeader(AUTHORIZATION, "$BEARER $token")
                it.addHeader(ACCEPT, APPLICATION_JSON)
            }.build())
        }.also { client ->
            client.addInterceptor(interceptor)
        }
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .pingInterval(ping, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level =  HttpLoggingInterceptor.Level.BODY 

    }

    @Provides
    @Singleton
    fun provideTimeout() = CONNECTION_TIME

    @Provides
    @Singleton
    @Named(NAMED_PING)
    fun providePingInterval() = PING_INTERVAL
}