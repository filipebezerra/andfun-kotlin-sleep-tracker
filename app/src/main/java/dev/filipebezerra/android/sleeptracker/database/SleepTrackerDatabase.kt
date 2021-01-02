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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [
            SleepNight::class
        ],
        version = 1,
        exportSchema = true,
)
abstract class SleepTrackerDatabase : RoomDatabase() {

    abstract val sleepNightDao: SleepNightDao

    companion object {

        @Volatile
        private var databaseInstance: SleepTrackerDatabase? = null

        fun getDatabase(context: Context): SleepTrackerDatabase =
            databaseInstance ?: buildDatabase(context)

        private fun buildDatabase(context: Context) =
            databaseInstance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SleepTrackerDatabase::class.java,
                    "SleepTracker.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        databaseInstance = it
                    }
            }
    }
}