package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToImageResource
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter : RecyclerView.Adapter<SleepNightViewHolder>() {
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(
            holder: SleepNightViewHolder,
            position: Int
    ) = holder.bind(data[position])

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = SleepNightViewHolder.from(parent)
}

class SleepNightViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val resources: Resources by lazy { itemView.context.resources }
    private val qualityImage: ImageView by lazy { itemView.findViewById(R.id.quality_image) }
    private val sleepLengthText: TextView by lazy { itemView.findViewById(R.id.sleep_length_text) }
    private val qualityStringTextView: TextView by lazy { itemView.findViewById(R.id.quality_string_text) }

    fun bind(sleepNight: SleepNight) {
        val (_, startTimeMilli, endTimeMilli, sleepQuality) = sleepNight
        sleepLengthText.text = convertDurationToFormatted(startTimeMilli, endTimeMilli, resources)
        qualityStringTextView.text = convertNumericQualityToString(sleepQuality, resources)
        qualityImage.setImageResource(convertNumericQualityToImageResource(sleepQuality))
    }

    companion object {
        fun from(parent: ViewGroup): SleepNightViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_sleep_night, parent, false)
            return SleepNightViewHolder(view)
        }
    }
}
