package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightItemBinding
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightGridItemBinding

class SleepNightAdapter : ListAdapter<SleepNight, RecyclerView.ViewHolder>(SleepNightItemDiff()) {
    private var viewStyle: ViewStyle = ViewStyle.LIST

    fun changeViewStyle(viewStyle: ViewStyle) = apply {
        this.viewStyle = viewStyle
        notifyDataSetChanged()
    }

    fun getViewStyle() = viewStyle

    override fun getItemViewType(position: Int): Int = when(viewStyle) {
        ViewStyle.LIST -> ViewStyle.LIST.ordinal
        ViewStyle.GRID -> ViewStyle.GRID.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            ViewStyle.LIST.ordinal -> ListViewHolder.createFrom(parent)
            ViewStyle.GRID.ordinal -> GridViewHolder.createFrom(parent)
            else -> throw IllegalStateException("Unknown ViewType $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when(viewStyle) {
            ViewStyle.LIST -> (holder as ListViewHolder).bindTo(getItem(position))
            ViewStyle.GRID -> (holder as GridViewHolder).bindTo(getItem(position))
        }

    class ListViewHolder private constructor(
        private val itemBinding: SleepNightItemBinding,
    ): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(item: SleepNight) =
            with(itemBinding) {
                sleepNight = item
                executePendingBindings()
            }

        companion object {
            fun createFrom(parent: ViewGroup): RecyclerView.ViewHolder =
                ListViewHolder(SleepNightItemBinding.inflate(
                    from(parent.context),
                    parent,
                    false
                ))
        }
    }

    class GridViewHolder private constructor(
        private val itemBinding: SleepNightGridItemBinding,
    ): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(item: SleepNight) =
            with(itemBinding) {
                sleepNight = item
                executePendingBindings()
            }

        companion object {
            fun createFrom(parent: ViewGroup): RecyclerView.ViewHolder =
                GridViewHolder(SleepNightGridItemBinding.inflate(
                    from(parent.context),
                    parent,
                    false
                ))
        }
    }
}

class SleepNightItemDiff : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean = oldItem.nightId == newItem.nightId
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean = oldItem == newItem
}

enum class ViewStyle { LIST, GRID }