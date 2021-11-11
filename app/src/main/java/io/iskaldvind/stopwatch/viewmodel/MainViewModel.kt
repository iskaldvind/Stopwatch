package io.iskaldvind.stopwatch.viewmodel

import androidx.lifecycle.ViewModel
import io.iskaldvind.stopwatch.model.ElapsedTimeCalculator
import io.iskaldvind.stopwatch.model.Repository
import io.iskaldvind.stopwatch.model.StopwatchStateCalculator
import io.iskaldvind.stopwatch.model.StopwatchStateHolder
import io.iskaldvind.stopwatch.support.TimestampMillisecondsFormatter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    repository: Repository = Repository()
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val elapsedTimeCalculator = ElapsedTimeCalculator(repository)
    private val orchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(repository, elapsedTimeCalculator),
            elapsedTimeCalculator,
            TimestampMillisecondsFormatter()
        ),
        scope
    )

    private val mutableTicker = MutableStateFlow("")
    val ticker: StateFlow<String> = mutableTicker

    fun start() {
        orchestrator.start()
    }

    fun pause() {
        orchestrator.pause()
    }

    fun stop() {
        orchestrator.stop()
    }

    inner class StopwatchListOrchestrator(
        private val stopwatchStateHolder: StopwatchStateHolder,
        private val scope: CoroutineScope,
    ) {

        private var job: Job? = null

        fun start() {
            if (job == null) startJob()
            stopwatchStateHolder.start()
        }

        private fun startJob() {
            scope.launch {
                while (isActive) {
                    mutableTicker.value = stopwatchStateHolder.getStringTimeRepresentation()
                    delay(20)
                }
            }
        }

        fun pause() {
            stopwatchStateHolder.pause()
            stopJob()
        }

        fun stop() {
            stopwatchStateHolder.stop()
            stopJob()
            clearValue()
        }

        private fun stopJob() {
            scope.coroutineContext.cancelChildren()
            job = null
        }

        private fun clearValue() {
            mutableTicker.value = "00:00:000"
        }
    }
}