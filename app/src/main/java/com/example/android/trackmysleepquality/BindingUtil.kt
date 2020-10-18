package com.example.android.trackmysleepquality

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.database.SleepNight

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(sleepNight: SleepNight?) =
        sleepNight?.let {
            setImageResource(convertNumericQualityToImageResource(sleepNight.sleepQuality))
        }

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(sleepNight: SleepNight?) =
        sleepNight?.let {
            val (_, startTimeMilli, endTimeMilli, _) = sleepNight
            text = convertDurationToFormatted(startTimeMilli, endTimeMilli, resources)
        }

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(sleepNight: SleepNight?) =
        sleepNight?.let {
            text = convertNumericQualityToString(sleepNight.sleepQuality, resources)
        }