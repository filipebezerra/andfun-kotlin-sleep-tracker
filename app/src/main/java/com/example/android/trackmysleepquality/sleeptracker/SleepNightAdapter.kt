package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToImageResource
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightViewHolder>(SleepNightDiffCallback()) {
    override fun onBindViewHolder(
            holder: SleepNightViewHolder,
            position: Int
    ) = holder.bind(getItem(position))

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = SleepNightViewHolder.from(parent)
}

class SleepNightViewHolder private constructor(
        var viewBinding: ListItemSleepNightBinding
) : RecyclerView.ViewHolder(
        viewBinding.root
) {
    private val resources: Resources by lazy { itemView.context.resources }

    fun bind(sleepNight: SleepNight) {
        val (_, startTimeMilli, endTimeMilli, sleepQuality) = sleepNight
        viewBinding.sleepLengthText.text = convertDurationToFormatted(startTimeMilli, endTimeMilli, resources)
        viewBinding.qualityStringText.text = convertNumericQualityToString(sleepQuality, resources)
        viewBinding.qualityImage.setImageResource(convertNumericQualityToImageResource(sleepQuality))
    }

    companion object {
        fun from(parent: ViewGroup): SleepNightViewHolder {
            val viewBinding = ListItemSleepNightBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return SleepNightViewHolder(viewBinding)
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight) =
            oldItem.nightId == newItem.nightId

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight) =
            oldItem == newItem
}