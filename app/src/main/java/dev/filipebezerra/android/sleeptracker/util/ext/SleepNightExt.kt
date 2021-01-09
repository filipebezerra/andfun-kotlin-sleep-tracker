package dev.filipebezerra.android.sleeptracker.util.ext

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import dev.filipebezerra.android.sleeptracker.R
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import java.text.SimpleDateFormat

/**
 * Returns a drawable resource representing the image quality rating.
 */
fun convertNumericQualityToDrawableResource(quality: Int): Int =
    when (quality) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_sleep_active
    }

/**
 * Returns a string representing the numeric quality rating.
 */
fun convertNumericQualityToString(quality: Int, resources: Resources): String {
    var qualityString = resources.getString(R.string.three_ok)
    when (quality) {
        -1 -> qualityString = "--"
        0 -> qualityString = resources.getString(R.string.zero_very_bad)
        1 -> qualityString = resources.getString(R.string.one_poor)
        2 -> qualityString = resources.getString(R.string.two_soso)
        4 -> qualityString = resources.getString(R.string.four_pretty_good)
        5 -> qualityString = resources.getString(R.string.five_excellent)
    }
    return qualityString
}

/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
            .format(systemTime).toString()
}

/**
 * Takes a list of SleepNights and converts and formats it into one string for display.
 *
 * For display in a TextView, we have to supply one string, and styles are per TextView, not
 * applicable per word. So, we build a formatted string using HTML. This is handy, but we will
 * learn a better way of displaying this data in a future lesson.
 *
 * @param   nights - List of all SleepNights in the database.
 * @param   resources - Resources object for all the resources defined for our app.
 *
 * @return  Spanned - An interface for text that has formatting attached to it.
 *           See: https://developer.android.com/reference/android/text/Spanned
 */
fun List<SleepNight>.formatNights(resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        this@formatNights.forEach {
            append(resources.getString(R.string.start_time))
            append("\t${convertLongToDateString(it.startTimeMillis)}<br>")
            if (it.endTimeMillis != it.startTimeMillis) {
                append(resources.getString(R.string.end_time))
                append("\t${convertLongToDateString(it.endTimeMillis)}<br>")
                append(resources.getString(R.string.quality))
                append("\t${convertNumericQualityToString(it.sleepQuality, resources)}<br>")
                append(resources.getString(R.string.hours_slept))
                // Hours
                append("\t ${it.endTimeMillis.minus(it.startTimeMillis) / 1000 / 60 / 60}:")
                // Minutes
                append("${it.endTimeMillis.minus(it.startTimeMillis) / 1000 / 60}:")
                // Seconds
                append("${it.endTimeMillis.minus(it.startTimeMillis) / 1000}")
            }
        }
    }
    return if (VERSION.SDK_INT >= VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
