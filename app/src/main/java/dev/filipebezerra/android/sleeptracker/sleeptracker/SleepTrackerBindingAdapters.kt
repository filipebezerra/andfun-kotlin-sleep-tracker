package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.util.ext.formatNights

@BindingAdapter("sleepNight")
fun TextView.bindSleepNight(sleepNight: SleepNight?) = sleepNight?.apply {
    text = listOf(this).formatNights(context.resources)
}

@BindingAdapter("sleepNightList")
fun RecyclerView.bindSleepNightList(sleepNightList: List<SleepNight>?) =
    (adapter as SleepNightAdapter).submitListWithHeader(sleepNightList)