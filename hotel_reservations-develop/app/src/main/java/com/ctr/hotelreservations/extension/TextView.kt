package com.ctr.hotelreservations.extension

import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ctr.hotelreservations.util.DelayAction

/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */
internal fun TextView.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

internal fun TextView.textColor(resourceId: Int) =
    setTextColor(ContextCompat.getColor(context, resourceId))

internal fun TextView.setFontFromAsset(name: String) {
    typeface = Typeface.createFromAsset(context.assets, name)
}

internal fun TextView.onClickSpannable(
    link: String,
    isUnderline: Boolean = false,
    action: () -> Unit
) {
    val spannableString = SpannableString(this.text)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            DelayAction.action(DelayAction.DEFAULT_DELAY_TIME, action)
        }

        override fun updateDrawState(textPaint: TextPaint) {
            super.updateDrawState(textPaint)
            textPaint.isUnderlineText = isUnderline
        }
    }
    val startIndexOfLink = this.text.toString().indexOf(link)
    spannableString.setSpan(
        clickableSpan, startIndexOfLink, startIndexOfLink + link.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.apply {
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
        text = spannableString
    }
}
