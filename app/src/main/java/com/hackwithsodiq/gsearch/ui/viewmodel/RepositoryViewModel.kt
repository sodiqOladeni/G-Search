package com.hackwithsodiq.gsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackwithsodiq.gsearch.extensions.isSuccessful
import com.hackwithsodiq.gsearch.extensions.networkException
import com.hackwithsodiq.gsearch.repository.RepoRepository
import com.hackwithsodiq.gsearch.ui.viewmodel.state.RepoScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(private val repository: RepoRepository): ViewModel() {
    private var _repoScreenUiState: MutableStateFlow<RepoScreenState> =
        MutableStateFlow(RepoScreenState())
    val repoScreenUiState = _repoScreenUiState.asStateFlow()

    fun updateErrorMessage(){
        _repoScreenUiState.update { it.copy(errorMessage = "") }
    }

    fun queryRepositories(query: String) {
        _repoScreenUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.fetchGithubRepositories(query)
                .catch { error ->
                    // Extensive error handling
                    _repoScreenUiState.update { it.copy(isLoading = false, errorMessage = error.networkException()) }
                }
                .collect { uiObject ->
                    _repoScreenUiState.update { it.copy(isLoading = false, statusCode = uiObject.status) }
                    if (uiObject.status.isSuccessful())
                        _repoScreenUiState.update { it.copy(repos = uiObject.uiData) }
                }
        }
    }
}