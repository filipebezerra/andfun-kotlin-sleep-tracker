package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.database.SleepNightDao
import dev.filipebezerra.android.sleeptracker.util.ext.formatNights
import dev.filipebezerra.android.sleeptracker.util.event.Event
import dev.filipebezerra.android.sleeptracker.util.ext.postEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * ViewModel for SleepTrackerFragment
 */
class SleepTrackerViewModel(
    private val sleepNightDao: SleepNightDao,
    application: Application,
) : AndroidViewModel(application), LifecycleObserver {

//    private val _tonight: LiveData<SleepNight?> =
//        Transformations.map(sleepNightDao.observeLatestNight()) {
//            it?.let { sleepNight ->
//                return@map when {
//                    sleepNight.endTimeMillis != sleepNight.startTimeMillis -> null
//                    else -> sleepNight
//                }
//            }
//        }

    private var _tonight = MutableLiveData<SleepNight?>()

    private val _sleepNights: LiveData<List<SleepNight>> = sleepNightDao.observeAllNights()

    val emptySleepNights = Transformations.map(_sleepNights) { it.isEmpty() }

    val sleepNightsText = Transformations.map(_sleepNights) {
        it.formatNights(application.resources)
    }

    private val _navigateToSleepQuality = MutableLiveData<Event<SleepNight>>()
    val navigateToSleepQuality: LiveData<Event<SleepNight>>
        get() = _navigateToSleepQuality

    init {
        retrieveLatestNight()
    }

    private fun retrieveLatestNight() =
        viewModelScope.launch { _tonight.value = retrieveLatestNightFromDatabase() }

    private suspend fun retrieveLatestNightFromDatabase(): SleepNight? =
        withContext(Dispatchers.IO) {
            var latestNight = sleepNightDao.getLatestNight()
            if (latestNight?.endTimeMillis != latestNight?.startTimeMillis) {
                latestNight = null
            }
            latestNight
        }

    fun onStartTracking() {
        Timber.i("Starting sleep tracker")
        viewModelScope.launch {
            val sleepNight = SleepNight()
            saveSleepNight(sleepNight)
            _tonight.value = retrieveLatestNightFromDatabase()
        }
    }

    fun onStopTracking() {
        Timber.i("Stopping sleep tracker")
        viewModelScope.launch {
            val currentSleepNight = _tonight.value ?: return@launch
            currentSleepNight.endTimeMillis = System.currentTimeMillis()
            saveSleepNight(currentSleepNight)
            _navigateToSleepQuality.postEvent(currentSleepNight)
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

