package io.iskaldvind.stopwatch.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DataSource(
    private val dataProvider: IDataProvider = DataProviderImpl(),
    private val refreshIntervalMs: Long = 20
) {
    val data: Flow<Long> = flow {
        while (true) {
            val receivedData = dataProvider.getMilliseconds()
            emit(receivedData)
            kotlinx.coroutines.delay(refreshIntervalMs)
        }
    }
        .flowOn(Dispatchers.Default)
        .catch { e ->
            println(e.message)
        }
}