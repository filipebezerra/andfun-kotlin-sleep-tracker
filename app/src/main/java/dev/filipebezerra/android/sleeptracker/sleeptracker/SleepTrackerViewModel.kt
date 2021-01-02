package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.repository.SleepNightRepository
import dev.filipebezerra.android.sleeptracker.util.event.Event
import dev.filipebezerra.android.sleeptracker.util.ext.formatNights
import dev.filipebezerra.android.sleeptracker.util.ext.postEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import dev.filipebezerra.android.sleeptracker.R
import dev.filipebezerra.android.sleeptracker.SleepTrackerApplication

/**
 * ViewModel for SleepTrackerFragment
 */
class SleepTrackerViewModel(
    private val repository: SleepNightRepository,
    application: Application,
) : AndroidViewModel(application), LifecycleObserver {

// TODO: Fix observing LiveData instead of retrieving it in the init block
//    private val _tonight: LiveData<SleepNight?> =
//        Transformations.map(sleepNightDao.observeLatestNight()) {
//            it?.let { sleepNight ->
//                return@map when {
//                    sleepNight.endTimeMillis != sleepNight.startTimeMillis -> null
//                    else -> sleepNight
//                }
//            }
//        }

//    private val _tonight: LiveData<SleepNight?> = repository.latestSleepNight.map {
//        when {
//            it?.endTimeMillis != it?.startTimeMillis -> null
//            else -> it
//        }
//    }.asLiveData()

    private var _tonight = MutableLiveData<SleepNight?>()

    private val _sleepNights: LiveData<List<SleepNight>> = repository.allSleepNights.asLiveData()

    val emptySleepNights = Transformations.map(_sleepNights) { it.isEmpty() }

    val isCurrentlyTrackingSleep = Transformations.map(_tonight) { it != null }

    val sleepNightsText = Transformations.map(_sleepNights) {
        it.formatNights(application.resources)
    }

    private val _navigateToSleepQuality = MutableLiveData<Event<SleepNight>>()
    val navigateToSleepQuality: LiveData<Event<SleepNight>>
        get() = _navigateToSleepQuality

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>>
        get() = _snackbarText

    init {
        retrieveLatestNight()
    }

    private fun retrieveLatestNight() =
        viewModelScope.launch { _tonight.value = retrieveLatestNightFromDatabase() }

    private suspend fun retrieveLatestNightFromDatabase(): SleepNight? =
        repository.getLatestNight()
            .apply {
                return@retrieveLatestNightFromDatabase when {
                    this?.endTimeMillis != this?.startTimeMillis -> null
                    else -> this
                }
            }

    fun onStartTracking() = viewModelScope.launch {
        Timber.i("Starting sleep tracker")
        val sleepNight = SleepNight()
        saveSleepNight(sleepNight)
        _tonight.value = retrieveLatestNightFromDatabase()
    }

    fun onStopTracking() = viewModelScope.launch {
        val currentSleepNight = _tonight.value ?: return@launch
        Timber.i("Stopping sleep tracker")
        currentSleepNight.endTimeMillis = System.currentTimeMillis()
        saveSleepNight(currentSleepNight)
        _navigateToSleepQuality.postEvent(currentSleepNight)
    }

    private suspend fun saveSleepNight(sleepNight: SleepNight) {
        sleepNight.takeIf { it.nightId == 0L }?.run {
            Timber.i("Saving new sleep night")
            repository.insert(sleepNight)
        }
        sleepNight.takeUnless { it.nightId == 0L }?.run {
            Timber.i("Updating sleep night $sleepNight")
            repository.update(sleepNight)
        }
    }

    // TODO: Improve it: run with WorkManager and notify user
    fun onClearSleepData() = viewModelScope.launch {
        Timber.i("Clearing sleep night")
        repository.deleteAll()
        _snackbarText.postEvent(R.string.cleared_messages)
    }
}

