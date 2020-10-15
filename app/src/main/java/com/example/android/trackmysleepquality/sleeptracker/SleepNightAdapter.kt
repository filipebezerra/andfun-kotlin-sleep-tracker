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

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SleepNightViewHolder, position: Int) {
        val (_, startTimeMilli, endTimeMilli, sleepQuality) = data[position]
        holder.sleepLengthText.text = convertDurationToFormatted(startTimeMilli, endTimeMilli, holder.resources)
        holder.qualityStringTextView.text = convertNumericQualityToString(sleepQuality, holder.resources)
        holder.qualityImage.setImageResource(convertNumericQualityToImageResource(sleepQuality))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
        return SleepNightViewHolder(view)
    }
}

class SleepNightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val resources: Resources by lazy { itemView.context.resources }
    val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)
    val sleepLengthText: TextView = itemView.findViewById(R.id.sleep_length_text)
    val qualityStringTextView: TextView = itemView.findViewById(R.id.quality_string_text)
}
