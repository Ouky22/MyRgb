package com.myrgb.ledcontroller.feature.editipaddress

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentIpAddressListBinding
import javax.inject.Inject

class IpAddressListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<IpAddressViewModel> { viewModelFactory }

    private lateinit var binding: FragmentIpAddressListBinding

    private lateinit var listAdapter: IpAddressListAdapter

    private var addIpDialog: AlertDialog? = null


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

        binding.viewModel = viewModel

        listAdapter = IpAddressListAdapter { ipAddress ->
            createAreYouSureToDeleteDialog(ipAddress)
        }
        binding.rvIpAddresses.adapter = listAdapter

        binding.btnAddIp.setOnClickListener { createAddIpAddressDialog() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent.ipAddressComponent().create()
            .inject(this)
    }

    private fun createAddIpAddressDialog() {
        val builder = AlertDialog.Builder(
            requireActivity(), R.style.Widget_LedControllerV2_DialogTheme
        )
        builder.setView(layoutInflater.inflate(R.layout.add_ip_address_dialog, null))
        addIpDialog = builder.create()
        addIpDialog!!.show()

        addIpDialog!!.findViewById<MaterialButton>(R.id.btn_add_ip_dialog).setOnClickListener {
            addIpDialog?.findViewById<EditText>(R.id.edit_text_ip)?.let { editText ->
                val validIpAddress = viewModel.isValidIpAddress(editText.text.toString())
                if (validIpAddress) {
                    viewModel.addIpAddress(editText.text.toString())
                    addIpDialog?.dismiss()
                } else
                    editText.error = getString(R.string.invalid_ip)
            }
        }

        addIpDialog!!.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            addIpDialog?.dismiss()
        }
    }

    private fun createAreYouSureToDeleteDialog(ipAddress: String) {
        val builder = AlertDialog.Builder(
            requireActivity(), R.style.Widget_LedControllerV2_DialogTheme
        )
        builder.setTitle(getString(R.string.sure_to_delete_ip_address, ipAddress))
            .setPositiveButton(R.string.delete) { _, _ -> viewModel.removeIpAddress(ipAddress) }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }
}































