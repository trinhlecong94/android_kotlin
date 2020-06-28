package com.ctr.hotelreservations.ui.wedget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.ctr.hotelreservations.extension.hideKeyboard


/**
 * Created by at-trinhnguyen2 on 31/03/2020.
 */
@SuppressLint("AppCompatCustomView")
open class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : EditText(context, attrs) {

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun onEditorAction(actionCode: Int) {
        if (actionCode == EditorInfo.IME_ACTION_DONE || actionCode == EditorInfo.IME_ACTION_NEXT) {
            hideKeyboard()
        }
        super.onEditorAction(actionCode)
    }
}
