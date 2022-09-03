package com.myrgb.ledcontroller.feature.rgbshow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbShowBinding
import com.myrgb.ledcontroller.di.RgbShowContainer

class RgbShowFragment : Fragment() {

    private lateinit var viewModel: RgbShowViewModel
    private lateinit var binding: FragmentRgbShowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rgb_show, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = (requireActivity().application as App)
        if (app.appContainer.rgbShowContainer == null)
            app.appContainer.rgbShowContainer = RgbShowContainer(
                app.appContainer.rgbRequestRepository,
                app.appContainer.ipAddressStorage
            )
        app.appContainer.rgbShowContainer?.let {
            val vm: RgbShowViewModel by viewModels {
                it.rgbShowViewModelFactory
            }
            viewModel = vm
            binding.viewModel = viewModel
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity().application as App).appContainer.rgbShowContainer = null
    }
}