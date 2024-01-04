package com.example.task19.common.helper.resource

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val errorMessage: String?) : Resource<T>()
}