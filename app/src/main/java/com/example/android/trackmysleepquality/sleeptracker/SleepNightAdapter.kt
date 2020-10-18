package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
    fun bind(sleepNight: SleepNight) {
        viewBinding.sleep = sleepNight
        viewBinding.executePendingBindings()
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