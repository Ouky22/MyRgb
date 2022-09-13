package com.myrgb.ledcontroller.feature.editipaddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.databinding.IpAddressItemBinding

class IpAddressListAdapter(private val itemDeleteClickListener: (IpAddressNamePair) -> Unit) :
    ListAdapter<IpAddressNamePair, IpAddressListAdapter.IpAddressNamePairViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<IpAddressNamePair>() {
        override fun areItemsTheSame(oldItem: IpAddressNamePair, newItem: IpAddressNamePair) =
            oldItem.ipAddress == newItem.ipAddress

        override fun areContentsTheSame(oldItem: IpAddressNamePair, newItem: IpAddressNamePair) =
            oldItem == newItem
    }

    inner class IpAddressNamePairViewHolder(private val binding: IpAddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ipAddressNamePair: IpAddressNamePair) {
            binding.tvIpAddress.text = ipAddressNamePair.ipAddress
            binding.tvIpAddressName.text = ipAddressNamePair.name
            binding.btnDeleteIp.setOnClickListener { itemDeleteClickListener(ipAddressNamePair) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IpAddressNamePairViewHolder {
        return IpAddressNamePairViewHolder(
            IpAddressItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: IpAddressNamePairViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}