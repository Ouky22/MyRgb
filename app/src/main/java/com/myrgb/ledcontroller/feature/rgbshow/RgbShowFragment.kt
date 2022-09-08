package com.myrgb.ledcontroller.feature.rgbshow

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbShowBinding
import javax.inject.Inject

class RgbShowFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<RgbShowViewModel> { viewModelFactory }

    private lateinit var binding: FragmentRgbShowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rgb_show, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.rgbShowComponent().create().inject(this)
    }
}