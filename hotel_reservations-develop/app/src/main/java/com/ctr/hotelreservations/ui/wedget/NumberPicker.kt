package com.ctr.hotelreservations.ui.wedget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.extension.afterTextChanged
import kotlinx.android.synthetic.main.layout_number_picker.view.*

class NumberPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var min = 1
    private var max = 5
    private var value = min
    internal var onValueChange: ((value: Int) -> Unit)? = null

    internal fun setValue(_value: Int) {
        value = _value
        refresh()
    }

    internal fun getValue(): Int = value

    internal fun setTitle(value: String) {
        tvTitle.text = value
    }

    internal fun setMin(value: Int) {
        min = value
        refresh()
    }

    internal fun setMax(value: Int) {
        max = value
        refresh()
    }

    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        View.inflate(context, R.layout.layout_number_picker, this)

        val styleAttrs = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker)
        try {
            tvTitle.text = styleAttrs.getString(R.styleable.NumberPicker_label)
        } finally {
            styleAttrs.recycle()
        }

        ivPlus.isEnabled = false
        ivSubtract.isEnabled = false
        refresh()

        tvNumber.afterTextChanged {
            value = it.toInt()
            onValueChange?.invoke(value)
            ivPlus.isEnabled = value != max
            ivSubtract.isEnabled = value != min
        }

        ivPlus.setOnClickListener {
            if (value < max) {
                value++
                tvNumber.text = value.toString()
            }
        }

        ivSubtract.setOnClickListener {
            if (value > min) {
                value--
                tvNumber.text = value.toString()
            }
        }
    }

    private fun refresh() {
        tvNumber.text = value.toString()
        if (min == max && max == value) {
            ivPlus.isEnabled = false
            ivSubtract.isEnabled = false
        }

        if (value == min) ivSubtract.isEnabled = false
        if (value < max) ivPlus.isEnabled = true
    }

}
