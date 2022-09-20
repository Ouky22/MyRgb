package com.myrgb.ledcontroller.feature.ipsettings.addedit

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.DialogIpAddressAddEditBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class IpAddressAddEditDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: IpAddressAddEditViewModel by viewModels { viewModelFactory }

    private lateinit var binding: DialogIpAddressAddEditBinding

    private var stateUpdatesJob: Job? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogIpAddressAddEditBinding.inflate(layoutInflater)

        val navigationArgs: IpAddressAddEditDialogFragmentArgs by navArgs()
        if (navigationArgs.ipAddress.isNotEmpty())
            viewModel.setSelectedIpAddressNamePair(navigationArgs.ipAddress)

        val title =
            if (navigationArgs.ipAddress.isNotEmpty()) R.string.edit_rgb_controller
            else R.string.add_rgb_controller

        return AlertDialog.Builder(requireActivity(), R.style.Widget_LedControllerV2_DialogTheme)
            .setView(binding.root)
            .setTitle(getString(title))
            .create()
    }

    override fun onStart() {
        super.onStart()

        stateUpdatesJob = lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                setErrorText(state)
                if (state.ipAddressNamePairSuccessfullySaved)
                    dialog?.dismiss()
            }
        }

        binding.editTextIp.setText(viewModel.state.value.ipAddress)
        binding.editTextName.setText(viewModel.state.value.ipAddressName)
        binding.editTextIp.addTextChangedListener { viewModel.updateIpAddressTextInput(it.toString()) }
        binding.editTextName.addTextChangedListener { viewModel.updateIpAddressNameTextInput(it.toString()) }
        binding.btnDialogAddIp.setOnClickListener { viewModel.submitIpAddressSettingsData() }
        binding.btnCancel.setOnClickListener { dialog?.dismiss() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent.ipAddressAddEditComponent().create()
            .inject(this)
    }

    override fun onStop() {
        stateUpdatesJob?.cancel()
        super.onStop()
    }

    private fun setErrorText(state: IpAddressAddEditState) {
        if (state.hasIpAddressError)
            binding.editTextIp.error = getString(state.ipAddressErrorMessageId)
        if (state.hasIpAddressNameError)
            binding.editTextName.error = getString(state.ipAddressNameErrorMessageId)
    }
}



































