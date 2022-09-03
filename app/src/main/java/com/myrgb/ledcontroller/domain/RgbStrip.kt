package com.myrgb.ledcontroller.domain

data class RgbStrip(
    val id: Int,
    val name: String,
    private var isOn: Boolean
) {
    @Transient
    private var onEnabledStatusChangedListener: ((enabled: Boolean) -> Unit)? = null

    var enabled: Boolean
        get() = isOn
        set(value) {
            isOn = value
            onEnabledStatusChangedListener?.invoke(isOn)
        }

    fun setEnabledStatusChangedListener(a: (isOn: Boolean) -> Unit) {
        onEnabledStatusChangedListener = a
        onEnabledStatusChangedListener?.invoke(isOn)
    }
}
