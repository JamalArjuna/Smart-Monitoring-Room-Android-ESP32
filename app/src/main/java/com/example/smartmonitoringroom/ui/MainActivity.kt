package com.example.smartmonitoringroom.ui

import android.R.id.message
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.smartmonitoringroom.R
import com.example.smartmonitoringroom.databinding.ActivityMainBinding
import com.example.smartmonitoringroom.viewmodel.SensorViewModel
import com.example.smartmonitoringroom.viewmodel.SensorViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastNotified: Long = 0
    private val viewModel: SensorViewModel by viewModels {
        SensorViewModelFactory()
    }
    private val DarkTreeshold = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChanel()

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.tvTemperature.text = state.temperature
                binding.tvHumidity.text = state.humidity
                binding.tvLight.text = state.light

                val tempValue = state.temperature
                    .substringBefore(" ")
                    .toFloatOrNull()
                if (tempValue != null){
                    handleTemp(tempValue)
                }

                val lightValue = state.light.toIntOrNull() ?: 0
                if (lightValue == DarkTreeshold) {
                    binding.imgHeader.setImageResource(R.drawable.morning)
                } else {
                    binding.imgHeader.setImageResource(R.drawable.night)
                }
            }
        }
    }

    private fun createNotificationChanel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val chanel = NotificationChannel(
                "TempNotification",
                "Temperature Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(chanel)
        }
    }

    private fun showNotification(temp: Float) {
        val notification = NotificationCompat.Builder(this, "TempNotification")
            .setContentTitle("Peringatan Suhu Tinggi")
            .setContentText("Suhu ruangan naik $temp °C")
            .setSmallIcon(R.drawable.ic_dangerous)
            .setColor(getColor(R.color.red_danger))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        with(NotificationManagerCompat.from(this)) {
            if (NotificationManagerCompat.from(this@MainActivity)
                    .areNotificationsEnabled()
            ) {
                notify(1002, notification)
            }
        }
    }

    private fun handleTemp(temp: Float){
        val now = System.currentTimeMillis()
        if (temp > 31.5f && now - lastNotified > 30_000L){
            showNotification(temp)
            lastNotified = now
        }
    }


}