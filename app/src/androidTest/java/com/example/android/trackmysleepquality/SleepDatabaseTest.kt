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

package com.example.android.trackmysleepquality

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.lifecycle.util.observeOnce
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {
    private lateinit var db: SleepDatabase
    private lateinit var sleepDao: SleepDatabaseDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun given_correct_sleep_night_when_adding_to_db_then_insert_sleep_night() {
        val sleepNight = SleepNight(
                startTimeMilli = System.currentTimeMillis(),
                endTimeMilli = System.currentTimeMillis().plus(
                        TimeUnit.HOURS.convert(4L, TimeUnit.MILLISECONDS)),
                sleepQuality = 1
        )
        sleepDao.insert(sleepNight)
        val tonight = sleepDao.getTonight()
        assertNotNull(tonight?.nightId)
        assertEquals(sleepNight.startTimeMilli, tonight?.startTimeMilli)
        assertEquals(sleepNight.endTimeMilli, tonight?.endTimeMilli)
        assertEquals(1, tonight?.sleepQuality)
    }

    @Test
    @Throws(IOException::class)
    fun given_correct_sleep_night_when_updating_to_db_then_update_sleep_night() {
        val sleepNight = SleepNight()
        sleepDao.insert(sleepNight)
        val tonight = sleepDao.getTonight()
        tonight!!.sleepQuality = 2
        sleepDao.update(tonight)
        assertEquals(2, sleepDao.getTonight()?.sleepQuality)
    }

    @Test
    @Throws(IOException::class)
    fun given_existing_night_id_when_getting_from_db_then_get_sleep_night() {
        val sleepNight = SleepNight()
        sleepDao.insert(sleepNight)
        val fromDb = sleepDao.get(1)
        assertNotNull(fromDb)
        assertEquals(-1, fromDb!!.sleepQuality)
    }

    @Test
    @Throws(IOException::class)
    fun given_various_sleep_nights_when_inserting_and_cleaning_then_sleep_night_table_is_empty() {
        val firstSleepNight = SleepNight(
                startTimeMilli = System.currentTimeMillis().minus(
                        TimeUnit.DAYS.convert(2, TimeUnit.MILLISECONDS)),
                sleepQuality = 1
        )
        val secondSleepNight = SleepNight(
                startTimeMilli = System.currentTimeMillis().minus(
                        TimeUnit.DAYS.convert(1, TimeUnit.MILLISECONDS)),
                sleepQuality = 2
        )
        val thirdSleepNight = SleepNight(
                sleepQuality = 3
        )
        sleepDao.insert(firstSleepNight)
        sleepDao.insert(secondSleepNight)
        sleepDao.insert(thirdSleepNight)
        sleepDao.clear()

        sleepDao.getAllSleepNights().observeOnce {
            assertEquals(0, it.size)
        }
    }

    @Test
    @Throws(IOException::class)
    fun given_various_sleep_nights_when_inserting_then_get_all_sleep_nights() {
        val firstSleepNight = SleepNight(
                startTimeMilli = System.currentTimeMillis().minus(
                        TimeUnit.DAYS.convert(2, TimeUnit.MILLISECONDS)),
                endTimeMilli = System.currentTimeMillis().plus(
                        TimeUnit.HOURS.convert(2L, TimeUnit.MILLISECONDS)),
                sleepQuality = 1
        )
        val secondSleepNight = SleepNight(
                startTimeMilli = System.currentTimeMillis().minus(
                        TimeUnit.DAYS.convert(1, TimeUnit.MILLISECONDS)),
                endTimeMilli = System.currentTimeMillis().plus(
                        TimeUnit.HOURS.convert(3L, TimeUnit.MILLISECONDS)),
                sleepQuality = 2
        )
        val thirdSleepNight = SleepNight(
                endTimeMilli = System.currentTimeMillis().plus(
                        TimeUnit.HOURS.convert(4L, TimeUnit.MILLISECONDS)),
                sleepQuality = 3
        )
        sleepDao.insert(firstSleepNight)
        sleepDao.insert(secondSleepNight)
        sleepDao.insert(thirdSleepNight)

        sleepDao.getAllSleepNights().observeOnce {
            assertEquals(3, it.size)
        }
    }

    @Test
    @Throws(IOException::class)
    fun given_correct_sleep_night_when_adding_to_db_then_get_tonight() {
        val sleepNight = SleepNight(
                startTimeMilli = System.currentTimeMillis(),
                endTimeMilli = System.currentTimeMillis().plus(
                        TimeUnit.MINUTES.convert(30L, TimeUnit.MILLISECONDS))
        )
        sleepDao.insert(sleepNight)
        val tonight = sleepDao.getTonight()
        assertNotNull(tonight)
        assertEquals(sleepNight.startTimeMilli, tonight?.startTimeMilli)
        assertEquals(sleepNight.endTimeMilli, tonight?.endTimeMilli)
        assertEquals(-1, tonight!!.sleepQuality)
    }
}