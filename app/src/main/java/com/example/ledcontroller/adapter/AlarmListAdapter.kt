package com.example.ledcontroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ledcontroller.databinding.AlarmItemBinding
import com.example.ledcontroller.model.RgbAlarm

class AlarmListAdapter(private val itemClickListener: (RgbAlarm) -> Unit) :
    ListAdapter<RgbAlarm, AlarmListAdapter.AlarmViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<RgbAlarm>() {
        override fun areItemsTheSame(oldItem: RgbAlarm, newItem: RgbAlarm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RgbAlarm, newItem: RgbAlarm): Boolean {
            return oldItem == newItem
        }
    }

    inner class AlarmViewHolder(private val binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rgbAlarm: RgbAlarm) {
            binding.alarm = rgbAlarm
            itemView.setOnClickListener { itemClickListener(rgbAlarm) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            AlarmItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}