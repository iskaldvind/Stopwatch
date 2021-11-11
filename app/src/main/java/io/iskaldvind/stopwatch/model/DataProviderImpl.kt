package io.iskaldvind.stopwatch.model

internal class DataProviderImpl : IDataProvider {
    override fun getMilliseconds(): Long = System.currentTimeMillis()
}