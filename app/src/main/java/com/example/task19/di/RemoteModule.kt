package com.example.task19.di

import com.example.task19.common.helper.Constants
import com.example.task19.common.helper.annotations.MockyRetrofit
import com.example.task19.common.helper.annotations.MockyService
import com.example.task19.common.helper.annotations.ReqresRetrofit
import com.example.task19.common.helper.annotations.ReqresService
import com.example.task19.data.remote.network.UsersService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    @MockyRetrofit
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
    }

    @Provides
    @Singleton
    @ReqresRetrofit
    fun provideReqresRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.URL_REQRES)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
    }

    @Provides
    @Singleton
    @MockyService
    fun provideUsersService(@MockyRetrofit retrofit: Retrofit): UsersService =
        retrofit.create(UsersService::class.java)


    @Provides
    @Singleton
    @ReqresService
    fun provideUsersServiceReqres(@ReqresRetrofit retrofit: Retrofit): UsersService =
        retrofit.create(UsersService::class.java)
}