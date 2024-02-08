package com.myrgb.ledcontroller.feature.rgbcontroller

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentControllerBinding
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.extensions.collectLatestLifecycleFlow
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import javax.inject.Inject

class ControllerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ControllerViewModel> { viewModelFactory }

    private lateinit var binding: FragmentControllerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_controller, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.colorPickerView.setColorListener(object : ColorEnvelopeListener {
            override fun onColorSelected(colorEnvelope: ColorEnvelope, fromUser: Boolean) {
                if (fromUser)
                    viewModel.onColorChange(
                        RgbTriplet(
                            colorEnvelope.color.red,
                            colorEnvelope.color.green,
                            colorEnvelope.color.blue
                        )
                    )
            }
        })
        binding.colorPickerView.attachBrightnessSlider(binding.brightnessSlide)

        setupMenu()

        viewModel.settingsLoadingStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                SettingsLoadingStatus.DONE -> {
                    setInitialColor()
                }

                SettingsLoadingStatus.LOADING -> {}
                else -> {}
            }
        }

        collectLatestLifecycleFlow(viewModel.rgbStrips) { createStripButtons(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent.controllerComponent().create()
            .inject(this)
    }

    private fun setInitialColor() {
        val currentColor = viewModel.currentlySelectedColor.value ?: return
        val initialColor = Color.valueOf(
            currentColor.red / 255f,
            currentColor.green / 255f,
            currentColor.blue / 255f
        )
        binding.colorPickerView.setInitialColor(initialColor.toArgb())
    }

    private fun setupMenu() {
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.ip_address_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                findNavController().navigate(
                    ControllerFragmentDirections.actionControllerDestToIpAddressListFragment()
                )
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun createStripButtons(rgbStrips: List<RgbStrip>) {
        binding.linearLayoutButtons.removeAllViews()

        rgbStrips.forEach { strip ->
            val button = requireActivity().layoutInflater.inflate(
                R.layout.strip_button, binding.linearLayoutButtons, false
            ) as MaterialButton
            button.text = strip.name

            strip.setEnabledStatusChangedListener { enabled ->
                if (enabled) button.setStrokeColorResource(R.color.btn_color_on)
                else button.setStrokeColorResource(R.color.btn_color_off)
            }

            button.setOnClickListener { viewModel.toggleEnabledStatusOf(strip) }

            binding.linearLayoutButtons.addView(button)
        }
    }
}




























