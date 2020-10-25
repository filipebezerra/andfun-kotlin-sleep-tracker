package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight

class SleepDetailViewModel(
        sleepNightKey: Long,
        dataSource: SleepDatabaseDao
) : ViewModel() {

    private val night = MediatorLiveData<SleepNight>()

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    val navigateToSleepTracker: LiveData<Boolean?> = _navigateToSleepTracker
    init {
        night.addSource(dataSource.getSleepNightWithId(sleepNightKey), night::setValue)
    }

    fun getNight() = night

    fun onClose() {
        _navigateToSleepTracker.value = true
    }

    fun onSleepTrackerNavigated() {
        _navigateToSleepTracker.value = null
    }
}