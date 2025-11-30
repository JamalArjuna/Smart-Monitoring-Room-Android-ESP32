package com.example.smartmonitoringroom.mqtt

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.*

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttClientManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val clientId = MqttConfig.CLIENT_ID_PREFIX + System.currentTimeMillis()

    private val mqttClient: MqttClient by lazy {
        MqttClient(
            MqttConfig.SERVER_URL,
            clientId,
            MemoryPersistence()
        )
    }

    fun connectMqtt(
        onConnected: () -> Unit,
        onError: (Throwable) -> Unit,
        onMessageReceived: (String) -> Unit
    ) {
        scope.launch {
            try {
                val options = MqttConnectOptions().apply {
                    isAutomaticReconnect = true
                    isCleanSession = true
                    mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
                    keepAliveInterval = 60
                }

                mqttClient.setCallback(object : MqttCallback {
                    override fun connectionLost(cause: Throwable?) {
                        Log.e("MQTT", "connectionLost: ${cause?.message}", cause)
                    }

                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        val msg = message?.toString() ?: return
                        Log.d("MQTT", "Message arrived from $topic: $msg")
                        onMessageReceived(msg)
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {}
                })

                Log.d("MQTT", "Connecting to ${MqttConfig.SERVER_URL} ...")
                mqttClient.connect(options)
                Log.d("MQTT", "Connected OK, subscribing to ${MqttConfig.TOPIC} ...")
                mqttClient.subscribe(MqttConfig.TOPIC, MqttConfig.QOS)
                Log.d("MQTT", "Subscribed")

                onConnected()

            } catch (e: Exception) {
                Log.e("MQTT", "Error connect/subscribe: ${e.message}", e)
                onError(e)
            }
        }
    }

    fun disconnectMqtt() {
        scope.launch {
            try {
                if (mqttClient.isConnected) {
                    mqttClient.disconnect()
                }
            } catch (e: MqttException) {
                Log.e("MQTT", "disconnect error: ", e)
            }
        }
    }
}