package com.example.smartmonitoringroom.viewmodel

import SensorRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartmonitoringroom.mqtt.MqttClientManager


class SensorViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val mqttManager = MqttClientManager()
        val repo = SensorRepository(mqttManager)

        @Suppress("UNCHECKED_CAST")
        return SensorViewModel(repo) as T
    }
}