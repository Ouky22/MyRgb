package com.myrgb.ledcontroller.feature.rgbcontroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentControllerBinding
import com.myrgb.ledcontroller.di.ControllerContainer

class ControllerFragment : Fragment() {

    private lateinit var viewModel: ControllerViewModel

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

        val app = requireActivity().application as App
        if (app.appContainer.controllerContainer == null)
            app.appContainer.controllerContainer =
                ControllerContainer(app.appContainer.controllerRepository)
        app.appContainer.controllerContainer?.let {
            val vm: ControllerViewModel by viewModels {
                it.controllerViewModelFactory
            }
            viewModel = vm
            binding.viewModel = viewModel
        }

        binding.root.setOnTouchListener { v, e ->
            v.performClick()

            if (e.action != MotionEvent.ACTION_DOWN && e.action != MotionEvent.ACTION_MOVE)
                return@setOnTouchListener true

            val touchPositionX = e.x.toInt()
            val touchPositionY = e.y.toInt()

            // only change color if touch position inside of the rgb circle image view
            if (touchPositionY > binding.ivRgbCircle.top && touchPositionY < binding.ivRgbCircle.bottom)
                viewModel.onRgbCircleTouch(touchPositionX, touchPositionY)

            return@setOnTouchListener true
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            viewModel.rgbCircleCenterX = binding.ivRgbCircle.left + (binding.ivRgbCircle.width / 2)
            viewModel.rgbCircleCenterY = binding.ivRgbCircle.top + (binding.ivRgbCircle.height / 2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity().application as App).appContainer.controllerContainer = null
    }
}




























