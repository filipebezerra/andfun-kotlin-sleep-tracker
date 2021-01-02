package dev.filipebezerra.android.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepNightDao {
    @Insert
    suspend fun insert(sleepNight: SleepNight)

    @Update
    suspend fun update(sleepNight: SleepNight)

    @Query("DELETE FROM sleep_nights")
    suspend fun deleteAll()

    @Query("SELECT * FROM sleep_nights ORDER BY start_time_millis DESC")
    fun observeAllNights(): Flow<List<SleepNight>>

    @Query("SELECT * FROM sleep_nights where id = :nightId")
    fun observeNight(nightId: Long): LiveData<SleepNight?>

    @Query("SELECT * FROM sleep_nights where id = :nightId")
    suspend fun getNight(nightId: Long): SleepNight?

    @Query("SELECT * FROM sleep_nights ORDER BY start_time_millis DESC LIMIT 1")
    fun observeLatestNight(): Flow<SleepNight?>

    @Query("SELECT * FROM sleep_nights ORDER BY start_time_millis DESC LIMIT 1")
    suspend fun getLatestNight(): SleepNight?
}