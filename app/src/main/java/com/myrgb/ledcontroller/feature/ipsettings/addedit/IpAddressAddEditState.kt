package com.myrgb.ledcontroller.feature.ipsettings.addedit

import androidx.annotation.StringRes

data class IpAddressAddEditState(
    val ipAddress: String = "",
    @StringRes val ipAddressErrorMessageId: Int = -1,

    val ipAddressName: String = "",
    @StringRes val ipAddressNameErrorMessageId: Int = -1,

    val ipAddressNamePairSuccessfullySaved: Boolean = false
) {
    val hasIpAddressError: Boolean
        get() = ipAddressErrorMessageId > -1
    val hasIpAddressNameError: Boolean
        get() = ipAddressNameErrorMessageId > -1
}
