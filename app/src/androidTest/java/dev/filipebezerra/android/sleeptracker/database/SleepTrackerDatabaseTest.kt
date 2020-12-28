package dev.filipebezerra.android.sleeptracker.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.filipebezerra.android.sleeptracker.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SleepTrackerDatabaseTest {

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var database: SleepTrackerDatabase
    private lateinit var sleepNightDao: SleepNightDao

    // https://github.com/android/architecture-components-samples/blob/master/LiveDataSample/app/src/test/java/com/android/example/livedatabuilder/LiveDataViewModelTest.kt
    // https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04
    // https://coroutinedispatcher.com/posts/room-and-coroutines-testing/
    // https://developer.android.com/training/testing/set-up-project
    // https://developer.android.com/training/data-storage/room/testing-db

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
                context,
                SleepTrackerDatabase::class.java
        )
                .allowMainThreadQueries()
                .build()
        sleepNightDao = database.sleepNightDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun given_new_sleep_night_when_insert_then_ensure_exists() = runBlocking {
        val sleepNight = SleepNight()
        sleepNightDao.insert(sleepNight)
                .run {
                    val latestNight = sleepNightDao.observeLatestNight().getOrAwaitValue()
                    assertEquals(-1, latestNight?.sleepQuality)
                }
    }
}