package com.first.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.first.app.core.network.RetrofitClient
import com.first.app.core.storage.TokenDataStore
import com.first.app.data.models.Result
import com.first.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle    : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore  = TokenDataStore(application)
    private val api        = RetrofitClient.build(dataStore)
    private val repository = AuthRepository(api, dataStore)

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(
        fullName: String,
        email: String,
        msisdn: String,
        credentials: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = when (
                val result = repository.register(fullName, email, msisdn, credentials)
            ) {
                is Result.Success -> AuthUiState.Success
                is Result.Error   -> AuthUiState.Error(result.message)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = when (val result = repository.login(email, password)) {
                is Result.Success -> AuthUiState.Success
                is Result.Error   -> AuthUiState.Error(result.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState.Idle
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    fun checkLoginState(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            onResult(repository.isLoggedIn())
        }
    }
}