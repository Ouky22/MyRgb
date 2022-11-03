package com.myrgb.ledcontroller.extensions

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myrgb.ledcontroller.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun Fragment.showAreYouSureToDeleteAlertDialog(
    message: String,
    positiveButtonClickHandler: DialogInterface.OnClickListener,
    negativeButtonClickHandler: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ -> }
) {
    AlertDialog.Builder(requireActivity(), R.style.Widget_LedControllerV2_DialogTheme)
        .setMessage(message)
        .setPositiveButton(R.string.delete, positiveButtonClickHandler)
        .setNegativeButton(android.R.string.cancel, negativeButtonClickHandler)
        .create()
        .show()
}