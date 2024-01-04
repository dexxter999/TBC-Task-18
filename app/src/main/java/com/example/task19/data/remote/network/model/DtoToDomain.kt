package com.example.task19.data.remote.network.model

import com.example.task19.domain.model.User

fun UserDto.toUser() = User(
    avatar = avatar,
    email = email,
    firstName = firstName,
    id = id,
    lastName = lastName
)