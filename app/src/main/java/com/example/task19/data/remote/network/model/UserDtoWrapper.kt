package com.example.task19.data.remote.network.model

import com.squareup.moshi.Json

data class UserDtoWrapper(
    @Json(name = "data")
    val data: UserDto
)