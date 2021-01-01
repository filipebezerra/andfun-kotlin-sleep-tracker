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

package dev.filipebezerra.android.sleeptracker.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.database.SleepNightDao
import dev.filipebezerra.android.sleeptracker.util.event.Event
import dev.filipebezerra.android.sleeptracker.util.ext.postEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepQualityViewModel(
    private val sleepNight: SleepNight,
    private val dataSource: SleepNightDao,
) : ViewModel() {

    private val _navigateToSleepTracker = MutableLiveData<Event<Boolean>>()
    val navigateToSleepTracker: LiveData<Event<Boolean>>
        get() = _navigateToSleepTracker

    fun onSleepQualitySelected(@SleepQuality sleepQuality: Int) {
        viewModelScope.launch {
            sleepNight.sleepQuality = sleepQuality
            saveSleepNight(sleepNight)
            _navigateToSleepTracker.postEvent(true)
        }
    }

    private suspend fun saveSleepNight(sleepNight: SleepNight) =
        withContext(Dispatchers.IO) { dataSource.update(sleepNight) }
}