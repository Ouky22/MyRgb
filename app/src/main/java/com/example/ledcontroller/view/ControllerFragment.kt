package com.example.ledcontroller.view

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ledcontroller.R
import com.example.ledcontroller.databinding.FragmentControllerBinding
import com.example.ledcontroller.viewmodel.ControllerViewModel

class ControllerFragment : Fragment() {
    private val viewModel: ControllerViewModel by viewModels()
    private lateinit var bulbDrawable: Drawable
    private lateinit var binding: FragmentControllerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_controller, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bulbDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_bulb)!!
        // wrap the drawable so that tinting call work on pre-v21 devices
        bulbDrawable = bulbDrawable.let { DrawableCompat.wrap(it) }
        DrawableCompat.setTintMode(bulbDrawable, PorterDuff.Mode.MULTIPLY)
        binding.ivBulb.setImageDrawable(bulbDrawable)

        binding.root.setOnTouchListener { v, e ->
            v.performClick()

            if (e.action != MotionEvent.ACTION_DOWN && e.action != MotionEvent.ACTION_MOVE)
                return@setOnTouchListener true

            val touchPositionX = e.x.toInt()
            val touchPositionY = e.y.toInt()

            // only change color if touch position inside of the rgb circle image view
            if (touchPositionY > binding.ivRgbCircle.top && touchPositionY < binding.ivRgbCircle.bottom) {
                val rgbColor = viewModel.getRgbColorAtTouchPosition(touchPositionX, touchPositionY)
                DrawableCompat.setTint(bulbDrawable, rgbColor.toRgbInt())
            }
            return@setOnTouchListener true
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            viewModel.rgbCircleCenterX = binding.ivRgbCircle.left + (binding.ivRgbCircle.width / 2)
            viewModel.rgbCircleCenterY = binding.ivRgbCircle.top + (binding.ivRgbCircle.height / 2)
        }
    }
}




























