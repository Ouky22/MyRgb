package com.myrgb.ledcontroller.feature.rgbalarmclock.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrgb.ledcontroller.databinding.AlarmItemBinding
import com.myrgb.ledcontroller.domain.RgbAlarm

class RgbAlarmListAdapter(
    private val itemClickListener: (RgbAlarm) -> Unit,
    private val onSwitchCheckedChangeListener: (alarmActivated: Boolean, RgbAlarm) -> Unit
) :
    ListAdapter<RgbAlarm, RgbAlarmListAdapter.AlarmViewHolder>(DiffCallback) {

    var tracker: SelectionTracker<Long>? = null

    override fun getItemId(position: Int): Long = position.toLong()

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

    inner class AlarmViewHolder(private val binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rgbAlarm: RgbAlarm) {
            binding.alarm = rgbAlarm
            binding.switchAlarm.setOnCheckedChangeListener { _, checked ->
                onSwitchCheckedChangeListener(checked, rgbAlarm)
            }
            itemView.setOnClickListener { itemClickListener(rgbAlarm) }

            tracker?.let {
                val itemSelected = it.isSelected(rgbAlarm.timeMinutesOfDay.toLong())
                binding.switchAlarm.visibility = if (itemSelected) View.GONE else View.VISIBLE
                binding.ivSelected.visibility = if (itemSelected) View.VISIBLE else View.GONE
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long =
                    getItem(bindingAdapterPosition).timeMinutesOfDay.toLong()
            }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RgbAlarm>() {
        override fun areItemsTheSame(oldItem: RgbAlarm, newItem: RgbAlarm): Boolean {
            return oldItem.timeMinutesOfDay == newItem.timeMinutesOfDay
        }

        override fun areContentsTheSame(oldItem: RgbAlarm, newItem: RgbAlarm): Boolean {
            return oldItem == newItem
        }
    }
}