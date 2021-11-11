package io.iskaldvind.stopwatch.model

class Repository(
    private val dataSource: DataSource = DataSource()
) {
    fun getData(): Long = dataSource.getData()
}