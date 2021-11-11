package io.iskaldvind.stopwatch.model

class ElapsedTimeCalculator(
    private val repository: Repository,
) {

    fun calculate(state: StopwatchState.Running): Long {
        val currentTimestamp = repository.getData()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            0
        }
        return timePassedSinceStart + state.elapsedTime
    }
}