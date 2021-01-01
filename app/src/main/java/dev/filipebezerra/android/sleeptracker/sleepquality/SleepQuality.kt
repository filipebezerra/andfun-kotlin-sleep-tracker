package dev.filipebezerra.android.sleeptracker.sleepquality

import androidx.annotation.IntDef
import dev.filipebezerra.android.sleeptracker.sleepquality.SleepQuality.Companion.QUALITY_EXCELLENT
import dev.filipebezerra.android.sleeptracker.sleepquality.SleepQuality.Companion.QUALITY_OK
import dev.filipebezerra.android.sleeptracker.sleepquality.SleepQuality.Companion.QUALITY_POOR
import dev.filipebezerra.android.sleeptracker.sleepquality.SleepQuality.Companion.QUALITY_PRETTY_GOOD
import dev.filipebezerra.android.sleeptracker.sleepquality.SleepQuality.Companion.QUALITY_SO_SO
import dev.filipebezerra.android.sleeptracker.sleepquality.SleepQuality.Companion.QUALITY_VERY_BAD


@IntDef(
    QUALITY_VERY_BAD,
    QUALITY_POOR,
    QUALITY_SO_SO,
    QUALITY_OK,
    QUALITY_PRETTY_GOOD,
    QUALITY_EXCELLENT
)
@Retention(AnnotationRetention.SOURCE)
annotation class SleepQuality {
    companion object {
        const val QUALITY_VERY_BAD = 0
        const val QUALITY_POOR = 1
        const val QUALITY_SO_SO = 2
        const val QUALITY_OK = 3
        const val QUALITY_PRETTY_GOOD = 4
        const val QUALITY_EXCELLENT = 5
    }
}