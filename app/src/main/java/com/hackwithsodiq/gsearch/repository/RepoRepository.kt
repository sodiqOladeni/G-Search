package com.hackwithsodiq.gsearch.repository

import com.hackwithsodiq.gsearch.api.GitHubAPI
import com.hackwithsodiq.gsearch.model.RepositoryItem
import com.hackwithsodiq.gsearch.model.UIObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

interface RepoRepository {
    fun fetchGithubRepositories(query: String): Flow<UIObject<List<RepositoryItem>>>
    fun fetchRepositoriesByUser(login: String): Flow<UIObject<List<RepositoryItem>>>
}

class RepoRepositoryImpl @Inject constructor(private val restAPI: GitHubAPI): RepoRepository {

    override fun fetchGithubRepositories(query: String): Flow<UIObject<List<RepositoryItem>>> = flow {
        try {
            val response = restAPI.searchRepos(query)
            emit(UIObject(response.code(), uiData = response.body()?.items))
        }catch (e:HttpException){
            emit(UIObject(e.code()))
        }
    }

    override fun fetchRepositoriesByUser(login: String): Flow<UIObject<List<RepositoryItem>>> = flow {
        try {
            val response = restAPI.fetchUserRepositories(login)
            emit(UIObject(response.code(), uiData = response.body()))
        }catch (e:HttpException){
            emit(UIObject(e.code()))
        }
    }
}