package dev.filipebezerra.android.sleeptracker.sleeptracker

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.filipebezerra.android.sleeptracker.R
import dev.filipebezerra.android.sleeptracker.database.SleepNight
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightGridItemBinding
import dev.filipebezerra.android.sleeptracker.databinding.SleepNightListItemBinding
import dev.filipebezerra.android.sleeptracker.sleeptracker.SleepNightAdapterItem.SleepNightDataItem
import dev.filipebezerra.android.sleeptracker.sleeptracker.SleepNightAdapterItem.SleepNightHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val HEADER_VIEW_TYPE = 2

enum class ListViewStyle { LIST, GRID }

class SleepNightAdapter(
    private val sleepNightListener: SleepNightListener,
) : ListAdapter<SleepNightAdapterItem, RecyclerView.ViewHolder>(SleepNightItemDiff()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    var listViewStyle: ListViewStyle = ListViewStyle.LIST
        private set

    fun changeViewStyle(listViewStyle: ListViewStyle) = apply {
        this.listViewStyle = listViewStyle
        notifyDataSetChanged()
    }

    fun submitListWithHeader(list: List<SleepNight>?) =
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(SleepNightHeader)
                else ->
                    listOf(SleepNightHeader) + list.map {
                        SleepNightDataItem(
                            it
                        )
                    }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SleepNightHeader -> HEADER_VIEW_TYPE
        is SleepNightDataItem -> {
            when (listViewStyle) {
                ListViewStyle.LIST -> ListViewStyle.LIST.ordinal
                ListViewStyle.GRID -> ListViewStyle.GRID.ordinal
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ListViewStyle.LIST.ordinal -> ListViewHolder.createFrom(parent)
            ListViewStyle.GRID.ordinal -> GridViewHolder.createFrom(parent)
            HEADER_VIEW_TYPE -> HeaderViewHolder.createFrom(parent)
            else -> throw IllegalStateException("Unknown ViewType $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> holder.bindTo(
                (getItem(position) as SleepNightDataItem).sleepNight,
                sleepNightListener
            )
            is GridViewHolder -> holder.bindTo(
                (getItem(position) as SleepNightDataItem).sleepNight,
                sleepNightListener
            )
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun createFrom(parent: ViewGroup): HeaderViewHolder =
                HeaderViewHolder(
                    from(parent.context).inflate(
                        R.layout.sleep_night_header,
                        parent,
                        false
                    )
                )

        }
    }

    class ListViewHolder private constructor(
        private val itemBinding: SleepNightListItemBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(item: SleepNight, sleepNightListener: SleepNightListener) =
            with(itemBinding) {
                sleepNight = item
                clickListener = sleepNightListener
                executePendingBindings()
            }

        companion object {
            fun createFrom(parent: ViewGroup): RecyclerView.ViewHolder =
                ListViewHolder(SleepNightListItemBinding.inflate(
                    from(parent.context),
                    parent,
                    false
                ))
        }
    }

    class GridViewHolder private constructor(
        private val itemBinding: SleepNightGridItemBinding,
    ): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(item: SleepNight, sleepNightListener: SleepNightListener) =
            with(itemBinding) {
                sleepNight = item
                clickListener = sleepNightListener
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

class SleepNightItemDiff : DiffUtil.ItemCallback<SleepNightAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: SleepNightAdapterItem,
        newItem: SleepNightAdapterItem
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: SleepNightAdapterItem,
        newItem: SleepNightAdapterItem
    ): Boolean =
        oldItem == newItem
}

class SleepNightListener(val clickCallback: (sleepNight: SleepNight) -> Unit) {
    fun onClick(sleepNight: SleepNight) = clickCallback(sleepNight)
}

sealed class SleepNightAdapterItem {
    abstract val id: Long

    data class SleepNightDataItem(val sleepNight: SleepNight) : SleepNightAdapterItem() {
        override val id = sleepNight.nightId
    }

    object SleepNightHeader : SleepNightAdapterItem() {
        override val id = Long.MIN_VALUE
    }
}