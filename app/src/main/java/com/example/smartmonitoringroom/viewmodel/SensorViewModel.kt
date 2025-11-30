package com.example.smartmonitoringroom.viewmodel


import SensorRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmonitoringroom.models.SensorUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SensorViewModel(
    private val repository: SensorRepository
) : ViewModel() {

    val uiState: StateFlow<SensorUiState> =
        repository.sensorDataFlow
            .map { data ->
                SensorUiState(
                    temperature = "${data.temperature} °C",
                    humidity = "${data.humidity} %",
                    light = data.light.toString()
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                SensorUiState()
            )

    init {
        viewModelScope.launch {
            repository.start()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.stop()
    }
}