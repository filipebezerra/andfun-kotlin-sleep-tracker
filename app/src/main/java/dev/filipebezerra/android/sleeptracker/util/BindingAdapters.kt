package dev.filipebezerra.android.sleeptracker.util

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isGone")
fun View.bindIsGone(isGone: Boolean) = run { isVisible = isGone.not() }