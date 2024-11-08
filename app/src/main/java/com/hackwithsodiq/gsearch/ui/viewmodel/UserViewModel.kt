package com.hackwithsodiq.gsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackwithsodiq.gsearch.extensions.isSuccessful
import com.hackwithsodiq.gsearch.extensions.networkException
import com.hackwithsodiq.gsearch.model.GithubUser
import com.hackwithsodiq.gsearch.repository.RepoRepository
import com.hackwithsodiq.gsearch.repository.UserRepository
import com.hackwithsodiq.gsearch.ui.viewmodel.state.UserDetailsScreenState
import com.hackwithsodiq.gsearch.ui.viewmodel.state.UsersScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repository: RepoRepository
) : ViewModel() {

    private var _usersScreenUiState: MutableStateFlow<UsersScreenState> =
        MutableStateFlow(UsersScreenState())
    val usersScreenUiState = _usersScreenUiState.asStateFlow()

    private var _userDetailsScreenUiState: MutableStateFlow<UserDetailsScreenState> =
        MutableStateFlow(UserDetailsScreenState())
    val userDetailsScreenUiState = _userDetailsScreenUiState.asStateFlow()

    fun setSelectedUser(user: GithubUser){
        _userDetailsScreenUiState.update { it.copy(user = user) }
    }

    fun updateUserScreenErrorMessage(){
        _usersScreenUiState.update { it.copy(errorMessage = "") }
    }

    fun updateUserDetailsScreenErrorMessage(){
        _usersScreenUiState.update { it.copy(errorMessage = "") }
    }

    fun queryUsers(query: String) {
        _usersScreenUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            userRepository.queryGithubUsers(query)
                .catch { error ->
                    // Extensive error handling
                    _usersScreenUiState.update { it.copy(isLoading = false, errorMessage = error.networkException()) }
                }
                .collect { uiObject ->
                    _usersScreenUiState.update { it.copy(isLoading = false, statusCode = uiObject.status) }
                    if (uiObject.status.isSuccessful())
                        _usersScreenUiState.update { it.copy(user = uiObject.uiData) }
                }
        }
    }

    fun fetchUserRepos(login: String) {
        _userDetailsScreenUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.fetchRepositoriesByUser(login)
                .catch { error ->
                    // Extensive error handling
                    _userDetailsScreenUiState.update { it.copy(isLoading = false, errorMessage = error.networkException()) }
                }
                .collect { uiObject ->
                    _userDetailsScreenUiState.update { it.copy(isLoading = false, statusCode = uiObject.status) }
                    if (uiObject.status.isSuccessful())
                        _userDetailsScreenUiState.update { it.copy(userRepos = uiObject.uiData) }
                }
        }
    }
}