import android.util.Log
import com.example.smartmonitoringroom.models.SensorData
import com.example.smartmonitoringroom.mqtt.MqttClientManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

class SensorRepository(
    private val mqttClientManager: MqttClientManager
) {
    private val _sensorDataFlow = MutableStateFlow(
        SensorData(0.0, 0.0, 0)
    )
    val sensorDataFlow: StateFlow<SensorData> = _sensorDataFlow

    fun start() {
        mqttClientManager.connectMqtt(
            onConnected = {
                Log.d("REPO", "MQTT connected")
            },
            onError = { it.printStackTrace() },
            onMessageReceived = { jsonString ->
                parseAndEmit(jsonString)
            }
        )
    }

    private fun parseAndEmit(jsonString: String) {
        try {
            val json = JSONObject(jsonString)
            val data = SensorData(
                temperature = json.getDouble("temperature"),
                humidity = json.getDouble("humidity"),
                light = json.getInt("light")
            )
            _sensorDataFlow.value = data   // langsung update value terbaru
        } catch (e: Exception) {
            Log.e("REPO", "JSON error: ${e.message}", e)
        }
    }

    fun stop() {
        mqttClientManager.disconnectMqtt()
    }
}
