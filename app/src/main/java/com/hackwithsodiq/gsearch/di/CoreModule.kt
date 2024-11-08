package com.hackwithsodiq.gsearch.di

import com.hackwithsodiq.gsearch.api.ApiService
import com.hackwithsodiq.gsearch.api.GitHubAPI
import com.hackwithsodiq.gsearch.model.Constants
import com.hackwithsodiq.gsearch.repository.RepoRepository
import com.hackwithsodiq.gsearch.repository.RepoRepositoryImpl
import com.hackwithsodiq.gsearch.repository.UserRepository
import com.hackwithsodiq.gsearch.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun okHttpClient() = ApiService().create<GitHubAPI>(Constants.BASE_URL)

    @Provides
    @Singleton
    fun provideUserRepository(gitHubAPI: GitHubAPI): UserRepository = UserRepositoryImpl(gitHubAPI)

    @Provides
    @Singleton
    fun provideRepoRepository(gitHubAPI: GitHubAPI): RepoRepository = RepoRepositoryImpl(gitHubAPI)
}