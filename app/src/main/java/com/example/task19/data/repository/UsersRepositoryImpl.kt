package com.example.task19.data.repository

import com.example.task19.common.helper.annotations.MockyService
import com.example.task19.common.helper.annotations.ReqresService
import com.example.task19.common.helper.resource.Resource
import com.example.task19.common.helper.resource.ResponseHandler
import com.example.task19.common.helper.resource.mapToDomain
import com.example.task19.data.remote.network.UsersService
import com.example.task19.data.remote.network.model.toUser
import com.example.task19.domain.model.UserDomain
import com.example.task19.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    @MockyService private val usersServiceMocky: UsersService,
    @ReqresService private val usersService: UsersService,
    private val responseHandler: ResponseHandler
) : UsersRepository {


    override suspend fun getUsersList(): Flow<Resource<List<UserDomain>>> {
        return responseHandler.handleApiCall {
            usersServiceMocky.getUsers()
        }.mapToDomain { list ->
            list.map { it.toUser() }
        }

    }

    override suspend fun getUser(id: Int): Flow<Resource<UserDomain>> {
        return responseHandler.handleApiCall {
            usersService.getUserDetail(id)
        }.mapToDomain { userDtoWrapper ->
            userDtoWrapper.data.toUser()
        }

    }

    override suspend fun deleteUser(id: Int): Flow<Resource<Unit>> {
        return responseHandler.handleApiCall {
            usersService.deleteUser(id)
        }
    }
}
