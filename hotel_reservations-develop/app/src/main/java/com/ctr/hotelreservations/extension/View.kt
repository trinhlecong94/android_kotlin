package com.ctr.hotelreservations.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.ctr.hotelreservations.util.DelayAction

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
internal fun View.showKeyboard() {
    requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

internal fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0
    )
}

internal fun View.onClickDelayAction(
    delayTime: Int = DelayAction.DEFAULT_DELAY_TIME,
    click: () -> Unit
) {
    setOnClickListener {
        DelayAction.action(delayTime, click)
    }
}

internal fun View.dimen(id: Int) = context.dimen(id)

internal fun View.backgroundColor(resourceId: Int) =
    setBackgroundColor(ContextCompat.getColor(context, resourceId))

internal fun View.visible() {
    visibility = View.VISIBLE
}

internal fun View.gone() {
    visibility = View.GONE
}

internal fun View.invisible() {
    visibility = View.INVISIBLE
}

internal fun View.isVisible() = visibility == View.VISIBLE

internal fun View.isGone() = visibility == View.GONE

internal fun Context.getActivity(): Activity? {
    (this as? ContextWrapper)?.let { contextWrapper ->
        (contextWrapper as? Activity)?.let {
            return it
        }
        return contextWrapper.baseContext?.getActivity()
    }
    return null
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.ellipsizeEnd() {
    val keyListeners = keyListener
    this.keyListener = null
    this.ellipsize = TextUtils.TruncateAt.END
    onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            this.keyListener = null
            ellipsize = TextUtils.TruncateAt.END
        } else {
            if (this@ellipsizeEnd.keyListener == null) {
                this@ellipsizeEnd.keyListener = keyListeners
                ellipsize = null
            }
            showKeyboard()
        }
    }
}
