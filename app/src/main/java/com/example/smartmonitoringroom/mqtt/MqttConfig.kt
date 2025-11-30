package com.example.smartmonitoringroom.mqtt

object MqttConfig {
    const val SERVER_URL = "tcp://broker.hivemq.com:1883"
    const val CLIENT_ID_PREFIX = "SmartMonitoringRoom_"
    const val TOPIC = "SMR/Android/Data"
    const val QOS = 1
}