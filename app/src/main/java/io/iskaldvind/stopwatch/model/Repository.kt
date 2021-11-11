package io.iskaldvind.stopwatch.model

import kotlinx.coroutines.flow.Flow

class Repository(
    private val dataSource: DataSource = DataSource()
) {
    val data: Flow<Long> = dataSource.data
}