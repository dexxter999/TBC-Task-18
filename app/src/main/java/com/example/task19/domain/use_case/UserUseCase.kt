package com.example.task19.domain.use_case

import javax.inject.Inject

class UserUseCase @Inject constructor(
    val getUsersList: GetUserListUseCase,
    val deleteUser: DeleteUserUseCase
)
