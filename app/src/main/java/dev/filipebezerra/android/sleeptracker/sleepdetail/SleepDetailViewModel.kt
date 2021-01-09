package dev.filipebezerra.android.sleeptracker.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.repository.SleepNightRepository

class SleepDetailViewModel(
    sleepNightId: Long,
    repository: SleepNightRepository
) : ViewModel() {

    val sleepNight: LiveData<SleepNight?> = repository.observeNight(sleepNightId)

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(
            sleepNightId: Long,
            sleepNightRepository: SleepNightRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                SleepDetailViewModel(sleepNightId, sleepNightRepository) as T
        }
    }
}