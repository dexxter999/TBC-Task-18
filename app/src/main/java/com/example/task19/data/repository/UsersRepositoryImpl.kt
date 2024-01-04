package com.example.task19.data.repository

import com.example.task19.common.helper.annotations.MockyService
import com.example.task19.common.helper.annotations.ReqresService
import com.example.task19.common.helper.resource.Resource
import com.example.task19.common.helper.resource.ResponseHandler
import com.example.task19.common.helper.resource.mapToDomain
import com.example.task19.data.remote.network.UsersService
import com.example.task19.data.remote.network.model.toUser
import com.example.task19.domain.model.User
import com.example.task19.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    @MockyService private val usersServiceMocky: UsersService,
    @ReqresService private val usersService: UsersService,
    private val responseHandler: ResponseHandler
) : UsersRepository {


    override suspend fun getUsersList(): Flow<Resource<List<User>>> {
        return flow {
            emit(responseHandler.handleApiCall {
                usersServiceMocky.getUsers()
            }.mapToDomain { list ->
                list?.map { it.toUser() } ?: emptyList()
            })
        }
    }

    override suspend fun getUser(id: Int): Flow<Resource<User>> {
        return flow {
//            responseHandler.handleApiCall {
//                usersService.getUserDetail(id)
//
//            }.mapToDomain { it?.data?.toUser() }
            try {
                val response = usersService.getUserDetail(id)
                if (response.isSuccessful) {
                    val userDto = response.body()?.data
                    if (userDto != null) {
                        emit(Resource.Success(data = userDto.toUser()))
                    } else {
                        val errorMessage = "HTTP error: ${response.code()} - ${response.message()}"
                        emit(Resource.Error(errorMessage))
                    }
                } else {
                    emit(Resource.Error(response.errorBody().toString()))
                }
            } catch (e: IOException) {
                emit(Resource.Error("network errror: ${e.message}"))
            } catch (e: HttpException) {
                emit(Resource.Error("HTTP exception: ${e.message}"))
            } catch (e: Exception) {
                emit(Resource.Error("other error: ${e.message}"))
            }

        }
    }
}