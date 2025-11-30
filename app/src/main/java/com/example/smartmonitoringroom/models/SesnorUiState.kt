package com.example.smartmonitoringroom.models

data class SensorUiState(
    val temperature: String = "-- °C",
    val humidity: String = "-- %",
    val light: String = "--"
)
