package com.hackwithsodiq.gsearch.ui.viewmodel.state

import com.hackwithsodiq.gsearch.model.GithubUser
import com.hackwithsodiq.gsearch.model.RepositoryItem

data class UserDetailsScreenState(
    val isLoading: Boolean = false,
    val statusCode: Int = 0,
    val errorMessage: String = "",
    val user: GithubUser? = null,
    val userRepos: List<RepositoryItem>? = null
)