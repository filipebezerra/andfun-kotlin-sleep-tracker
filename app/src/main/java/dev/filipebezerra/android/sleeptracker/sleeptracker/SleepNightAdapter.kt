package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightItemBinding
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightItemBinding.inflate

class SleepNightAdapter(
    private val sleepNightList: List<SleepNight>,
) : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.createFrom(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindTo(sleepNightList[position])

    override fun getItemCount(): Int = sleepNightList.size

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