package com.myrgb.ledcontroller.feature.rgbcontroller

import android.content.Context
import android.os.Bundle
import android.view.*
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
import com.myrgb.ledcontroller.extensions.collectLatestLifecycleFlow
import com.myrgb.ledcontroller.extensions.coordinateIsInsideView
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

        binding.root.setOnTouchListener { v, e ->
            v.performClick()

            if (e.action != MotionEvent.ACTION_DOWN && e.action != MotionEvent.ACTION_MOVE)
                return@setOnTouchListener true

            val touchPositionX = e.x
            val touchPositionY = e.y
            if (binding.ivRgbCircle.coordinateIsInsideView(touchPositionX, touchPositionY))
                viewModel.onRgbCircleTouch(touchPositionX.toInt(), touchPositionY.toInt())

            return@setOnTouchListener true
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            viewModel.rgbCircleCenterX = binding.ivRgbCircle.left + (binding.ivRgbCircle.width / 2)
            viewModel.rgbCircleCenterY = binding.ivRgbCircle.top + (binding.ivRgbCircle.height / 2)
        }

        setupMenu()

        viewModel.settingsLoadingStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                SettingsLoadingStatus.DONE -> {} // TODO hide loading spinner
                SettingsLoadingStatus.LOADING -> {} //  TODO display loading spinner
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




























