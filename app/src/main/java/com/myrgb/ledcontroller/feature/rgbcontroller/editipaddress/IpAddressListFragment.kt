package com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentIpAddressListBinding

class IpAddressListFragment : Fragment() {

    private lateinit var binding: FragmentIpAddressListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_ip_address_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}