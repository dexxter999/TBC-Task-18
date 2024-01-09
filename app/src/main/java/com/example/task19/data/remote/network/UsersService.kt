package com.example.task19.data.remote.network

import com.example.task19.common.helper.Constants
import com.example.task19.data.remote.network.model.UserDto
import com.example.task19.data.remote.network.model.UserDtoWrapper
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersService {
    @GET(Constants.USERS)
    suspend fun getUsers(): Response<List<UserDto>>

    @GET(Constants.USERS_DETAIL)
    suspend fun getUserDetail(@Path("id") id: Int): Response<UserDtoWrapper>

    @DELETE(Constants.USERS_DETAIL)
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}