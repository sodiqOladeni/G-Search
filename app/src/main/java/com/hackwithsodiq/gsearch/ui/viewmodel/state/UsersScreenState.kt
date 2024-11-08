package com.hackwithsodiq.gsearch.ui.viewmodel.state

import com.hackwithsodiq.gsearch.model.GithubUser

data class UsersScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val statusCode: Int = 0,
    val user: GithubUser? = null
)