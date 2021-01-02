package dev.filipebezerra.android.sleeptracker.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.database.SleepNightDao
import kotlinx.coroutines.flow.Flow

class SleepNightRepository(val sleepNightDao: SleepNightDao) {

    val allSleepNights: Flow<List<SleepNight>> = sleepNightDao.observeAllNights()

    val latestSleepNight: Flow<SleepNight?> = sleepNightDao.observeLatestNight()

    @WorkerThread
    suspend fun insert(sleepNight: SleepNight) = sleepNightDao.insert(sleepNight)

    @WorkerThread
    suspend fun update(sleepNight: SleepNight) = sleepNightDao.update(sleepNight)

    @WorkerThread
    suspend fun deleteAll() = sleepNightDao.deleteAll()

    fun observeNight(nightId: Long) = sleepNightDao.observeNight(nightId)

    @WorkerThread
    suspend fun getNight(nightId: Long) = sleepNightDao.getNight(nightId)

    @WorkerThread
    suspend fun getLatestNight() = sleepNightDao.getLatestNight()
}