package com.hackwithsodiq.gsearch.repository

import com.hackwithsodiq.gsearch.api.GitHubAPI
import com.hackwithsodiq.gsearch.model.GithubUser
import com.hackwithsodiq.gsearch.model.UIObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UserRepository {
    fun queryGithubUsers(username: String): Flow<UIObject<GithubUser>>
}

class UserRepositoryImpl @Inject constructor(private val restAPI: GitHubAPI): UserRepository {
    override fun queryGithubUsers(username: String): Flow<UIObject<GithubUser>> = flow {
        val response = restAPI.fetchAUser(username)
        emit(UIObject(response.code(), uiData = response.body()))
    }
}