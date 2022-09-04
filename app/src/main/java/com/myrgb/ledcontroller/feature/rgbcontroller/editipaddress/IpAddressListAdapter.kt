package com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrgb.ledcontroller.databinding.IpAddressItemBinding

class IpAddressListAdapter(private val itemDeleteClickListener: (String) -> Unit) :
    ListAdapter<String, IpAddressListAdapter.IpAddressViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem === newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }

    inner class IpAddressViewHolder(private val binding: IpAddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ipAddress: String) {
            binding.tvIpAddress.text = ipAddress
            binding.btnDeleteIp.setOnClickListener { itemDeleteClickListener(ipAddress) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IpAddressViewHolder {
        return IpAddressViewHolder(
            IpAddressItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: IpAddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}