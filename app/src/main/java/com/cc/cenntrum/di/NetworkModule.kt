package com.cc.cenntrum.di

import com.cc.cenntrum.retrofit.ApiService
import com.cc.cenntrum.utils.Constants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NonNls
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


//
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//
//    @JvmStatic
//    @Provides
//    @Singleton
//    fun provideApiInterface(): ApiService = ApiService.getInstance()
//
//}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {



    @JvmStatic
    @Provides
    @Singleton
    fun provideApiInterface(okHttpClient: OkHttpClient , gson: Gson): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://cenntrum.codecoyapps.com/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }




    @get:Provides
    val okHttpClient: OkHttpClient
        get() {
//            val okHttpClientBuilder = OkHttpClient.Builder()
//            okHttpClientBuilder.connectTimeout(APIConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
//            okHttpClientBuilder.readTimeout(APIConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
//            okHttpClientBuilder.writeTimeout(APIConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
//            okHttpClientBuilder.addInterceptor(RequestInterceptor())
//            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//            return okHttpClientBuilder.build()

            val okHttpClientBuilder = OkHttpClient.Builder()
            okHttpClientBuilder.readTimeout(100, TimeUnit.SECONDS);
            okHttpClientBuilder.connectTimeout(100, TimeUnit.SECONDS);
            okHttpClientBuilder.writeTimeout(100, TimeUnit.SECONDS);

//            val logger =
//                HttpLoggingInterceptor().apply {
//                    if (BuildConfig.DEBUG) {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    } else {
//                        level = HttpLoggingInterceptor.Level.NONE
//                    }
//                }
//
//            okHttpClientBuilder.addInterceptor(logger)
            return okHttpClientBuilder.build()
        }



    @get:Provides
    val gson: Gson
        get() {
            return  GsonBuilder()
                .setLenient()
                .create()

        }
}