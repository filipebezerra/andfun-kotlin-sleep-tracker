package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightItemBinding
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightItemBinding.inflate
import dev.filipebezerra.android.sleeptracker.sleeptracker.SleepNightAdapter.ViewHolder.Companion.createFrom

class SleepNightAdapter() : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = createFrom(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindTo(getItem(position))

    class ViewHolder private constructor(
        private val itemBinding: SleepNightItemBinding,
    ): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(item: SleepNight) =
            with(itemBinding) {
                sleepNight = item
                executePendingBindings()
            }
        companion object {
            fun createFrom(parent: ViewGroup): ViewHolder =
                ViewHolder(inflate(from(parent.context), parent, false))
        }
    }
}

class SleepNightItemDiff : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean =
        oldItem.nightId == newItem.nightId

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean =
        oldItem == newItem

}