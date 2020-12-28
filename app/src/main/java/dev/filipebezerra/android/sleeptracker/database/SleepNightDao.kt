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

package dev.filipebezerra.android.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepNightDao {
    @Insert
    suspend fun insert(sleepNight: SleepNight)

    @Update
    suspend fun update(sleepNight: SleepNight)

    @Query("DELETE FROM sleep_nights")
    suspend fun deleteAll()

    @Query("SELECT * FROM sleep_nights ORDER BY start_time_millis DESC")
    fun observeAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM sleep_nights where id = :nightId")
    fun observeNight(nightId: Long): LiveData<SleepNight>

    @Query("SELECT * FROM sleep_nights ORDER BY start_time_millis DESC LIMIT 1")
    fun observeLatestNight(): LiveData<SleepNight?>
}