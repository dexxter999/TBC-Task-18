package com.example.task19.di

import com.example.task19.data.repository.UsersRepositoryImpl
import com.example.task19.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindUserRepository(repositoryImpl: UsersRepositoryImpl): UsersRepository

}