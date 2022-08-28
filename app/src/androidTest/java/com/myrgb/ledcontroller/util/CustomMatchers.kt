package com.myrgb.ledcontroller.util

import android.graphics.Color.*
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.button.MaterialButton
import com.myrgb.ledcontroller.domain.RgbTriplet
import org.hamcrest.Description


fun withTint(tint: RgbTriplet): BoundedMatcher<View, ImageView> {
    return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText(tint.toString())
        }

        override fun matchesSafely(imageView: ImageView) =
            imageView.imageTintList?.defaultColor == tint.toRgbInt()

        override fun describeMismatch(item: Any, description: Description) {
            val imageView = item as ImageView
            val imageViewColor = imageView.imageTintList?.defaultColor ?: 0
            val imageViewRgbTriplet =
                RgbTriplet(red(imageViewColor), green(imageViewColor), blue(imageViewColor))
            description.appendText(imageViewRgbTriplet.toString())
            super.describeMismatch(item, description)
        }
    }
}

fun withSeekbarProgress(progress: Int): BoundedMatcher<View, SeekBar> {
    return object : BoundedMatcher<View, SeekBar>(SeekBar::class.java) {
        override fun describeTo(description: Description) {
            description.appendText(progress.toString())
        }

        override fun matchesSafely(seekBar: SeekBar) =
            seekBar.progress == progress
    }
}

fun withStrokeColor(@ColorRes strokeColorResource: Int): BoundedMatcher<View, MaterialButton> {
    return object : BoundedMatcher<View, MaterialButton>(MaterialButton::class.java) {
        override fun describeTo(description: Description) {
            description.appendText(strokeColorResource.toString())
        }

        override fun matchesSafely(button: MaterialButton) =
            button.strokeColor.defaultColor ==
                    ContextCompat.getColor(button.context, strokeColorResource)
    }
}