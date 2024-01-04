package com.example.task19.domain.repository

import com.example.task19.common.helper.resource.Resource
import com.example.task19.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getUsersList(): Flow<Resource<List<User>>>
    suspend fun getUser(id: Int): Flow<Resource<User>>
}