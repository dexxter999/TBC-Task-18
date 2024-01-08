package com.example.task19.domain.model

import com.example.task19.presentation.model.User

fun UserDomain.toPresentation() = User(
    avatar = avatar,
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email
)