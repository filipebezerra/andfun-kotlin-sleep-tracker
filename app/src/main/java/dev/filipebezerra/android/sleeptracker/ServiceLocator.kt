package dev.filipebezerra.android.sleeptracker

import android.content.Context
import dev.filipebezerra.android.sleeptracker.database.SleepTrackerDatabase
import dev.filipebezerra.android.sleeptracker.repository.SleepNightRepository

object ServiceLocator {
    @Volatile
    private var sleepNightRepository: SleepNightRepository? = null

    fun provideSleepNightRepository(context: Context) =
        sleepNightRepository ?: buildSleepNightRepository(context)

    private fun buildSleepNightRepository(context: Context) =
        sleepNightRepository ?: synchronized(this) {
            SleepTrackerDatabase.getDatabase(context)
                .run { SleepNightRepository(this.sleepNightDao) }
                .also { sleepNightRepository = it }
        }
}