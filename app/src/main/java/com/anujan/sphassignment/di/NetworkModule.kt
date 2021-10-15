package com.anujan.sphassignment.di

import com.anujan.sphassignment.api.ApiInterface
import com.anujan.sphassignment.util.EndPoints
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class NetworkModule {

    @Singleton
    @Provides
    open fun providesApiInterface(): ApiInterface {
        return retrofit().create(ApiInterface::class.java)
    }

    @Provides
    fun retrofit(): Retrofit {

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(EndPoints.BASE_URL)
            .client(httpClient)
            .build()
    }
}