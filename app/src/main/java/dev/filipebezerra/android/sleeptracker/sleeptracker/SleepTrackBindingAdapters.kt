package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.text.format.DateUtils
import android.text.format.DateUtils.getRelativeTimeSpanString
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.filipebezerra.android.sleeptracker.R
import dev.filipebezerra.android.sleeptracker.util.ext.convertLongToDateString
import dev.filipebezerra.android.sleeptracker.util.ext.convertNumericQualityToDrawableResource
import dev.filipebezerra.android.sleeptracker.util.ext.convertNumericQualityToString

@BindingAdapter("sleepQualityImage", "sleepQualityDescription", requireAll = true)
fun ImageView.bindSleepQualityImage(
    sleepQuality: Int?,
    startTimeMillis: Long?
) = sleepQuality?.let { quality ->
    startTimeMillis?.let { startTime ->
        context.resources.also { resources ->
            setImageResource(convertNumericQualityToDrawableResource(quality, resources))
            contentDescription = resources.getString(
                R.string.sleep_quality_description_format,
                convertNumericQualityToString(quality, resources),
                getRelativeTimeSpanString(
                    startTime,
                    System.currentTimeMillis(),
                    DateUtils.DAY_IN_MILLIS
                )
            )
        }
    }
}

@BindingAdapter("sleepQualityText")
fun TextView.bindSleepQualityText(sleepQuality: Int?) = sleepQuality?.let { quality ->
    text = convertNumericQualityToString(quality, context.resources)
}

@BindingAdapter("sleepLenght")
fun TextView.bindSleepLenght(startTimeMillis: Long?) = startTimeMillis?.let { lenght ->
    text = convertLongToDateString(lenght)
}