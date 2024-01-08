package com.example.task19.data.remote.network.model

import com.example.task19.domain.model.UserDomain

fun UserDto.toUser() = UserDomain(
    avatar = avatar,
    email = email,
    firstName = firstName,
    id = id,
    lastName = lastName
)