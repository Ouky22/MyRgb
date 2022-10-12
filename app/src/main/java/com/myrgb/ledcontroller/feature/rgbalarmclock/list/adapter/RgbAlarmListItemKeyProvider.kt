package com.myrgb.ledcontroller.feature.rgbalarmclock.list.adapter

import androidx.recyclerview.selection.ItemKeyProvider

class RgbAlarmListItemKeyProvider(
    private val adapter: RgbAlarmListAdapter
) : ItemKeyProvider<Long>(SCOPE_CACHED) {

    override fun getKey(position: Int): Long =
        adapter.currentList[position].timeMinutesOfDay.toLong()

    override fun getPosition(key: Long): Int =
        adapter.currentList.indexOfFirst { it.timeMinutesOfDay.toLong() == key }
}