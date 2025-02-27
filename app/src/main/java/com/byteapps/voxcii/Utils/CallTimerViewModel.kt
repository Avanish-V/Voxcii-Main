package com.byteapps.voxcii.Utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.voxcii.ui.theme.AppTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CallTimerViewModel : ViewModel() {
    private val _elapsedTime = MutableStateFlow(0) // Time in seconds
    val elapsedTime: StateFlow<Int> = _elapsedTime

    private var job: Job? = null

    fun startTimer() {
        if (job == null || job?.isActive == false) {
            job = viewModelScope.launch {
                while (true) {
                    delay(1000L)
                    _elapsedTime.value += 1
                }
            }
        }
    }

    fun stopTimer() {
        job?.cancel()
        job = null
    }

    fun resetTimer() {
        stopTimer()
        _elapsedTime.value = 0
    }
}

@Composable
fun CallTimerScreen(viewModel: CallTimerViewModel, isRunning: Boolean) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()

    LaunchedEffect(isRunning) {
        if (isRunning) viewModel.startTimer() else viewModel.stopTimer()
    }

    Box(modifier = Modifier.height(60.dp), contentAlignment = Alignment.Center) {

        if (isRunning){
            Text(
                text = "Connected : ${formatTime(elapsedTime)}",
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colorScheme.onTertiary
            )
        }else{
            Text(
                text = "Disconnected: ${formatTime(elapsedTime)}",
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colorScheme.onTertiary
            )
        }

    }

}

