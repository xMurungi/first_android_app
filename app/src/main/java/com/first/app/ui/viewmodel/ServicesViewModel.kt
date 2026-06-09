package com.first.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.first.app.core.network.RetrofitClient
import com.first.app.core.storage.TokenDataStore
import com.first.app.data.models.Result
import com.first.app.data.models.ServiceDto
import com.first.app.data.models.SubscriptionDto
import com.first.app.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ServicesUiState {
    object Loading : ServicesUiState()
    data class Success(val services: List<ServiceDto>) : ServicesUiState()
    data class Error(val message: String) : ServicesUiState()
}

sealed class SubscribeUiState {
    object Idle    : SubscribeUiState()
    object Loading : SubscribeUiState()
    data class Success(val message: String) : SubscribeUiState()
    data class Error(val message: String)   : SubscribeUiState()
}

sealed class SubscriptionsUiState {
    object Loading : SubscriptionsUiState()
    data class Success(val subscriptions: List<SubscriptionDto>) : SubscriptionsUiState()
    data class Error(val message: String) : SubscriptionsUiState()
}

class ServicesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore  = TokenDataStore(application)
    private val api        = RetrofitClient.build(dataStore)
    private val repository = ServiceRepository(api, dataStore)

    private val _servicesState = MutableStateFlow<ServicesUiState>(ServicesUiState.Loading)
    val servicesState: StateFlow<ServicesUiState> = _servicesState.asStateFlow()

    private val _subscribeState = MutableStateFlow<SubscribeUiState>(SubscribeUiState.Idle)
    val subscribeState: StateFlow<SubscribeUiState> = _subscribeState.asStateFlow()

    private val _subscriptionsState = MutableStateFlow<SubscriptionsUiState>(SubscriptionsUiState.Loading)
    val subscriptionsState: StateFlow<SubscriptionsUiState> = _subscriptionsState.asStateFlow()

    // tracks which service card is currently loading
    private val _loadingServiceId = MutableStateFlow<Long?>(null)
    val loadingServiceId: StateFlow<Long?> = _loadingServiceId.asStateFlow()

    init { loadServices() }

    fun loadServices() {
        viewModelScope.launch {
            _servicesState.value = ServicesUiState.Loading
            _servicesState.value = when (val result = repository.getServices()) {
                is Result.Success -> ServicesUiState.Success(result.data)
                is Result.Error   -> ServicesUiState.Error(result.message)
            }
        }
    }

    fun subscribe(service: ServiceDto) {
        viewModelScope.launch {
            _loadingServiceId.value = service.id
            _subscribeState.value   = SubscribeUiState.Loading
            _subscribeState.value = when (val result = repository.subscribe(service)) {
                is Result.Success -> SubscribeUiState.Success(result.data)
                is Result.Error   -> SubscribeUiState.Error(result.message)
            }
            _loadingServiceId.value = null
        }
    }

    fun loadSubscriptions() {
        viewModelScope.launch {
            _subscriptionsState.value = SubscriptionsUiState.Loading
            _subscriptionsState.value = when (val result = repository.getSubscriptions()) {
                is Result.Success -> SubscriptionsUiState.Success(result.data)
                is Result.Error   -> SubscriptionsUiState.Error(result.message)
            }
        }
    }

    fun resetSubscribeState() {
        _subscribeState.value = SubscribeUiState.Idle
    }
}