package com.example.task19.common.helper.resource

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ResponseHandler @Inject constructor() {
    suspend fun <T : Any> handleApiCall(apiCall: suspend () -> Response<T>): Resource<T?> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error("Code: ${response.code()}, Something went wrong")
            }
        } catch (e: IOException) {
            Resource.Error(e.message)
        } catch (e: HttpException) {
            Resource.Error(e.message())
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
}