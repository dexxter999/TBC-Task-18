package com.example.task19.common.helper.resource

fun <T, R> Resource<T>.mapToDomain(mapper: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(mapper(data))
        is Resource.Error -> Resource.Error(errorMessage)
    }
}



