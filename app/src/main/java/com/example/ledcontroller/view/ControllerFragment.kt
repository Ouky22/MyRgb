package com.example.ledcontroller.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_controller, container, false)

        bulbDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_bulb)!!
        // wrap the drawable so that tinting call work on pre-v21 devices
        bulbDrawable = bulbDrawable.let { DrawableCompat.wrap(it) }
        // set tint mode
        DrawableCompat.setTintMode(bulbDrawable, PorterDuff.Mode.MULTIPLY)
        // set tint color
        DrawableCompat.setTint(bulbDrawable, Color.BLUE)
        // set drawable of bulb imageview to the bulbDrawable
        binding.ivBulb.setImageDrawable(bulbDrawable)


        binding.root.setOnTouchListener { _, event ->
            val touchPositionX = event.x.toInt()
            val touchPositionY = event.y.toInt()

            // only change color if touch position is in the rectangle of the rgb circle image view
            if (touchPositionY < binding.ivRgbCircle.bottom && touchPositionY > binding.ivRgbCircle.top) {
                val rgbColor = viewModel.getRgbColorAtTouchPosition(touchPositionX, touchPositionY)
                DrawableCompat.setTint(bulbDrawable, rgbColor.toRgbInt())
            }
            return@setOnTouchListener true
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            viewModel.rgbCircleCenterX = binding.ivRgbCircle.left + (binding.ivRgbCircle.width / 2)
            viewModel.rgbCircleCenterY = binding.ivRgbCircle.top + (binding.ivRgbCircle.height / 2)
        }

        return binding.root
    }
}




























