package com.myrgb.ledcontroller.feature.rgbcontroller

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress.IpAddressListAdapter

@BindingAdapter("ip_address_list_data")
fun ipAddressData(recyclerView: RecyclerView, ipAddresses: List<String>?) {
    (recyclerView.adapter as IpAddressListAdapter).submitList(ipAddresses)
}