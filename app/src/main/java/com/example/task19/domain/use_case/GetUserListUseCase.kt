package com.example.task19.domain.use_case

import com.example.task19.common.helper.resource.Resource
import com.example.task19.domain.model.UserDomain
import com.example.task19.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(private val repository: UsersRepository) {
    suspend operator fun invoke(): Flow<Resource<List<UserDomain>>> {
        return repository.getUsersList()
    }
}