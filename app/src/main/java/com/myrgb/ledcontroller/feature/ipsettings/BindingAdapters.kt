package com.myrgb.ledcontroller.feature.ipsettings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrgb.ledcontroller.IpAddressNamePair

@BindingAdapter("ip_address_list_data")
fun ipAddressData(recyclerView: RecyclerView, ipAddressNamePairs: List<IpAddressNamePair>?) {
    (recyclerView.adapter as IpAddressListAdapter).submitList(ipAddressNamePairs)
}