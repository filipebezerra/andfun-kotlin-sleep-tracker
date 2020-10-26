package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ITEM_VIEW_TYPE_HEADER = 0
const val ITEM_VIEW_TYPE_ITEM = 1

class SleepNightAdapter(
        private val clickListener: SleepNightListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
        ITEM_VIEW_TYPE_ITEM -> SleepNightViewHolder.from(parent)
        else -> throw IllegalArgumentException("Unknown viewType $viewType")
    }

    override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int
    ) {
        when (holder) {
            is SleepNightViewHolder -> {
                val sleepNightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(sleepNightItem.sleepNight, clickListener)
            }
        }
    }

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val dataList = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(dataList)
            }
        }
    }
}

class SleepNightViewHolder private constructor(
        var viewBinding: ListItemSleepNightBinding
) : RecyclerView.ViewHolder(
        viewBinding.root
) {
    fun bind(sleepNight: SleepNight, clickListener: SleepNightListener) {
        viewBinding.sleep = sleepNight
        viewBinding.clickListener = clickListener
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

class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem) =
            oldItem == newItem
}

class SleepNightListener(val clickListener: (nightId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

class HeaderViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun from(parent: ViewGroup): HeaderViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.holder, parent, false)
            return HeaderViewHolder(itemView)
        }
    }
}

sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id = sleepNight.nightId
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}