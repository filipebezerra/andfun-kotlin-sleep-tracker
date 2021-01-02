package dev.filipebezerra.android.sleeptracker.util.ext

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import dev.filipebezerra.android.sleeptracker.R
import dev.filipebezerra.android.sleeptracker.util.event.Event
import dev.filipebezerra.android.sleeptracker.util.event.EventObserver

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(
    snackbarText: String,
    timeLength: Int,
    anchorView: View? = null,
    onDismiss: (() -> Unit)? = null,
) {
    Snackbar.make(this, snackbarText, timeLength)
        .apply {
            animationMode = ANIMATION_MODE_SLIDE
            setAnchorView(anchorView)
            setBackgroundTint(ContextCompat.getColor(context, R.color.snackbar_background))
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    onDismiss?.invoke()
                }
            })
        }
        .run { show() }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int,
    anchorView: View? = null,
    onDismiss: (() -> Unit)? = null
) = snackbarEvent.observe(lifecycleOwner, EventObserver {
    showSnackbar(context.getString(it), timeLength, anchorView, onDismiss)
})