package com.ctr.hotelreservations.ui.wedget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.extension.gone
import com.ctr.hotelreservations.extension.invisible
import com.ctr.hotelreservations.extension.visible
import kotlinx.android.synthetic.main.input_time_layout.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
/**
 * Created by at-trinhnguyen2 on 20/03/2020.
 */
class InputTimeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var isRequired: Boolean = true
    private var hasDrawableRight: Boolean = false
    private var textcolor = 0
    internal var isValid: Boolean = false

    init {
        initView(attrs)
    }

    internal fun setValue(text: String) {
        tvContent.text = text
    }

    internal fun setLabel(text: Int) {
        tvTimeLabel.setText(text)
    }

    internal fun getText() = tvContent.text.toString().trim()

    internal fun getTextNotTrim() = tvContent.text.toString().trim()

    internal fun setValidState(isValid: Boolean) {
        this.isValid = isValid
        tvTimeLabel.isActivated = isValid
        viewTimeBreakLine?.isActivated = isValid
        tvTimeErrorLabel?.isActivated = isValid
        tvTimeErrorLabel?.visibility = if (isValid) View.GONE else View.VISIBLE
    }

    private fun initView(attrs: AttributeSet?) {
        View.inflate(context, R.layout.input_time_layout, this)
        val styleAttrs = context.obtainStyledAttributes(attrs, R.styleable.InputTimeLayout)
        try {
            tvTimeLabel.text = styleAttrs.getString(R.styleable.InputTimeLayout_label_text)
            tvTimeLabel.isActivated = true
            isRequired = styleAttrs.getBoolean(R.styleable.InputTimeLayout_isRequired, true)
            hasDrawableRight =
                styleAttrs.getBoolean(R.styleable.InputTimeLayout_label_hasDrawableRight, false)
            textcolor =
                styleAttrs.getColor(
                    R.styleable.InputTimeLayout_textColor,
                    ContextCompat.getColor(context, R.color.colorTundora)
                )
            tvContent.setTextColor(textcolor)
            if (!isRequired || !hasDrawableRight) {
                tvTimeLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            }
            when (styleAttrs.getInt(R.styleable.InputTimeLayout_label_visibility, 0)) {
                0 -> tvTimeLabel.visible()
                1 -> tvTimeLabel.invisible()
                2 -> tvTimeLabel.gone()
            }
            tvContent?.apply {
                text = styleAttrs.getString(R.styleable.InputTimeLayout_content_text)
                hint = styleAttrs.getString(R.styleable.InputTimeLayout_content_hint)
            }
            tvTimeDetailLabel?.text = styleAttrs.getString(R.styleable.InputTimeLayout_detail_text)
            viewTimeBreakLine.setBackgroundResource(R.color.colorTundoraLine)
        } finally {
            styleAttrs.recycle()
        }
    }
}
