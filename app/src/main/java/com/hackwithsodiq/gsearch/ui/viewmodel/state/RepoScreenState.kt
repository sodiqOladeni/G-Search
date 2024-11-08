package com.hackwithsodiq.gsearch.ui.viewmodel.state

import com.hackwithsodiq.gsearch.model.RepositoryItem

data class RepoScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val statusCode: Int = 0,
    val repos: List<RepositoryItem>? = null
)