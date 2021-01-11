package dev.filipebezerra.android.sleeptracker.sleeptracker

import androidx.recyclerview.widget.GridLayoutManager

const val SLEEP_NIGHT_GRID_SPAN_COUNT = 3

fun GridLayoutManager.withSpanSizeLookupForHeaderAndList() = apply {
    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int = when (position) {
            0 -> SLEEP_NIGHT_GRID_SPAN_COUNT
            else -> 1
        }
    }
}