package com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentIpAddressListBinding

class IpAddressListFragment : Fragment() {

    private lateinit var binding: FragmentIpAddressListBinding
    private lateinit var listAdapter: IpAddressListAdapter
    private lateinit var viewModel: IpAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_ip_address_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as App).appContainer.controllerContainer?.let {
            val vm: IpAddressViewModel by viewModels {
                it.ipAddressViewModelFactory
            }
            viewModel = vm
            binding.viewModel = viewModel
        }

        listAdapter = IpAddressListAdapter { ipAddress ->
            viewModel.removeIpAddress(ipAddress)
        }
        binding.rvIpAddresses.adapter = listAdapter
    }
}