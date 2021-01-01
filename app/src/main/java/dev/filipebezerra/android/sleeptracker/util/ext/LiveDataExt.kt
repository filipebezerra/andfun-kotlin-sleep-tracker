package dev.filipebezerra.android.sleeptracker.util.ext

import androidx.lifecycle.MutableLiveData
import dev.filipebezerra.android.sleeptracker.util.event.Event

fun <T> MutableLiveData<Event<T>>.postEvent(content: T) {
    postValue(Event(content))
}