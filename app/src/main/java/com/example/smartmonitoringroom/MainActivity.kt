package com.example.smartmonitoringroom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.smartmonitoringroom.databinding.ActivityMainBinding
import com.example.smartmonitoringroom.viewmodel.SensorViewModel
import com.example.smartmonitoringroom.viewmodel.SensorViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: SensorViewModel by viewModels {
        SensorViewModelFactory()
    }

    private val DarkTreeshold = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.tvTemperature.text = state.temperature
                binding.tvHumidity.text = state.humidity
                binding.tvLight.text = state.light

                val lightValue = state.light.toIntOrNull() ?: 0
                if (lightValue == DarkTreeshold) {
                    binding.imgHeader.setImageResource(R.drawable.morning)
                } else {
                    binding.imgHeader.setImageResource(R.drawable.night)
                }
            }
        }
    }
}