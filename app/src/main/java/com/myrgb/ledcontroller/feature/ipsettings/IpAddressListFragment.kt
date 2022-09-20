package com.myrgb.ledcontroller.feature.ipsettings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentIpAddressListBinding
import javax.inject.Inject

class IpAddressListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<IpAddressListViewModel> { viewModelFactory }

    private lateinit var binding: FragmentIpAddressListBinding

    private lateinit var listAdapter: IpAddressListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentIpAddressListBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        setupRecyclerViewAdapter()

        binding.btnAddIp.setOnClickListener {
            findNavController().navigate(
                IpAddressListFragmentDirections.actionIpAddressListDestToAddIpAddressDialog()
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent.ipAddressComponent().create()
            .inject(this)
    }

    private fun setupRecyclerViewAdapter() {
        listAdapter = IpAddressListAdapter(
            itemDeleteClickListener = { ipAddressNamePair ->
                createAreYouSureToDeleteDialog(ipAddressNamePair)
            },
            itemClickListener = { ipAddressNamePair ->
                val action =
                    IpAddressListFragmentDirections.actionIpAddressListDestToAddIpAddressDialog(
                        ipAddressNamePair.ipAddress
                    )
                findNavController().navigate(action)
            }
        )
        binding.rvIpAddresses.adapter = listAdapter
    }

    private fun createAreYouSureToDeleteDialog(ipAddressNamePair: IpAddressNamePair) {
        AlertDialog.Builder(requireActivity(), R.style.Widget_LedControllerV2_DialogTheme)
            .setMessage(
                getString(
                    R.string.sure_to_delete_ip_address,
                    "\"${ipAddressNamePair.name}\" (${ipAddressNamePair.ipAddress})"
                )
            )
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.removeIpAddressNamePair(ipAddressNamePair)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }
}































