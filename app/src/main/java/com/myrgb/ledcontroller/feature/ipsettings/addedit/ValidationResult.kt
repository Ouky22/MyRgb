package com.myrgb.ledcontroller.feature.ipsettings.addedit

import androidx.annotation.StringRes

data class ValidationResult(
    val isSuccessful: Boolean,
    @StringRes val errorMessageId: Int = -1
)