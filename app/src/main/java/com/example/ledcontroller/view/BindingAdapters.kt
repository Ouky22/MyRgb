package com.example.ledcontroller.view

import androidx.databinding.BindingAdapter
import com.example.ledcontroller.R

@BindingAdapter("status_border")
fun statusBorder(button: com.google.android.material.button.MaterialButton, isActivated: Boolean) {
    if (isActivated)
        button.setStrokeColorResource(R.color.btn_color_on)
    else
        button.setStrokeColorResource(R.color.btn_color_off)
}