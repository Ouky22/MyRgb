package com.example.ledcontroller.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.ledcontroller.R
import com.example.ledcontroller.databinding.FragmentControllerBinding
import com.example.ledcontroller.viewmodel.ControllerViewModel

class ControllerFragment : Fragment() {
    private val viewModel: ControllerViewModel by viewModels()

    private lateinit var binding: FragmentControllerBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_controller, container, false)


        binding.root.setOnTouchListener { v, event ->
            // get touch position
            val x = event.x.toInt()
            val y = event.y.toInt()

            // get position of the center of the rgb circle
            val locationArr = IntArray(2)
            binding.ivRgbCircle.getLocationOnScreen(locationArr)
            val rgbCircleCenterX = locationArr[0] + (binding.ivRgbCircle.width / 2)
            val rgbCircleCenterY = locationArr[1] + (binding.ivRgbCircle.height / 2)

            val rgbValues = viewModel.getRgbColors(arrayOf(x, y), rgbCircleCenterX, rgbCircleCenterY)


            Log.d("ControllerFragment", "x: $x y: $y rgb: [${rgbValues[0]},${rgbValues[1]},${rgbValues[2]}]")

            true
        }

        return binding.root
    }


}