/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.database.SleepNightDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    private val sleepNightDao: SleepNightDao,
    application: Application
) : AndroidViewModel(application) {

    private val _tonight: LiveData<SleepNight?> =
        Transformations.map(sleepNightDao.observeLatestNight()) {
            it?.let { sleepNight ->
                when {
                    sleepNight.endTimeMillis != sleepNight.startTimeMillis -> {
                        Timber.i("Latest night is completed, we need a new one")
                        return@map null
                    }
                    else -> {
                        Timber.i("Latest night is $sleepNight")
                        return@map sleepNight
                    }
                }
            } ?: Timber.i("No latest night yet").run { null }
        }

    private val _nights: LiveData<List<SleepNight>> = sleepNightDao.observeAllNights()

    val emptySleepNights = Transformations.map(_nights) { it.isEmpty() }

    fun onStartTracking() {
        Timber.i("Starting sleep tracker")
        viewModelScope.launch {
            val sleepNight = SleepNight()
            saveSleepNight(sleepNight)
        }
    }

    fun onStopTracking() {
        Timber.i("Stopping sleep tracker")
        viewModelScope.launch {
            val currentSleepNight = _tonight.value ?: return@launch
            currentSleepNight.endTimeMillis = System.currentTimeMillis()
            saveSleepNight(currentSleepNight)
        }
    }

    private suspend fun saveSleepNight(sleepNight: SleepNight) {
        withContext(Dispatchers.IO) {
            with(sleepNight) {
                if (nightId == 0L) {
                    Timber.i("Saving new sleep night")
                    sleepNightDao.insert(sleepNight)
                } else {
                    Timber.i("Updating sleep night $sleepNight")
                    sleepNightDao.update(sleepNight)
                }
            }
        }
    }

    // TODO: Improve it: run with WorkManager and notify user
    fun onClearSleepData() {
        Timber.i("Clearing sleep night")
        viewModelScope.launch { deleteAllSleepNights() }
    }

    private suspend fun deleteAllSleepNights() {
        withContext(Dispatchers.IO) { sleepNightDao.deleteAll() }
    }
}

