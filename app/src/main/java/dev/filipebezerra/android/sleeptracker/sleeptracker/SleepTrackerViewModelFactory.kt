package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.filipebezerra.android.sleeptracker.database.SleepNightDao
import dev.filipebezerra.android.sleeptracker.repository.SleepNightRepository

/**
 * Provides the SleepDatabaseDao and context to the ViewModel.
 */
class SleepTrackerViewModelFactory(
    private val repository: SleepNightRepository,
    private val application: Application,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)) {
            return SleepTrackerViewModel(
                repository,
                application,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

