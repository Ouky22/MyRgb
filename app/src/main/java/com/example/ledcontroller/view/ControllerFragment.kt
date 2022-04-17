package com.example.ledcontroller.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            // get touch position
            val x = event.x.toInt()
            val y = event.y.toInt()

            // if touch position is in the rectangle of the rgb circle image view, change color
            if (y < binding.ivRgbCircle.bottom && y > binding.ivRgbCircle.top) {
                changeRgbCircleColor(x, y)
            }
            true
        }

        return binding.root
    }

    private fun changeRgbCircleColor(touchX: Int, touchY: Int) {
        // get position of the center of the rgb circle
        val rgbCircleCenterX = binding.ivRgbCircle.left + (binding.ivRgbCircle.width / 2)
        val rgbCircleCenterY = binding.ivRgbCircle.top + (binding.ivRgbCircle.height / 2)

        val rgbValues =
            viewModel.getRgbColors(arrayOf(touchX, touchY), rgbCircleCenterX, rgbCircleCenterY)
        // set color of bulb
        DrawableCompat.setTint(
            bulbDrawable,
            Color.rgb(rgbValues[0], rgbValues[1], rgbValues[2])
        )
    }
}