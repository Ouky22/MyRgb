package com.myrgb.ledcontroller.feature.rgbalarmclock.list.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class RgbAlarmListItemDetailsLookup(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null)
            return (recyclerView.getChildViewHolder(view) as RgbAlarmListAdapter.AlarmViewHolder).getItemDetails()
        return null
    }
}