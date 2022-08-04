package com.example.ledcontroller.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.ledcontroller.R
import com.example.ledcontroller.databinding.FragmentRgbShowBinding
import com.example.ledcontroller.viewmodel.RgbShowViewModel

class RgbShowFragment : Fragment() {

    private val viewModel: RgbShowViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRgbShowBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rgb_show, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }
}