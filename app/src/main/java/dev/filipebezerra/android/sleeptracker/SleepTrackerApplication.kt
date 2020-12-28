package dev.filipebezerra.android.sleeptracker

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class SleepTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BuildConfig.DEBUG.takeIf { it }?.run { Timber.plant(DebugTree()) }
    }
}