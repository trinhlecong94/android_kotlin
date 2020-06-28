package com.ctr.hotelreservations.ui.wedget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.extension.dip
import com.ctr.hotelreservations.extension.gone
import com.ctr.hotelreservations.extension.hideKeyboard
import com.ctr.hotelreservations.extension.visible
import kotlinx.android.synthetic.main.input_group_layout.view.*

/**
 * Created by at-trinhnguyen2 on 20/03/2020.
 */
class InputGroupLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var isRequired: Boolean = true
    private var isDetailHiddenOnError: Boolean = true
    internal var hasDrawableRight: Boolean = false
    private var isValid: Boolean = true
    private var isElippsizeEnd = true
    private var isTrim = false
    private var maxline = 1
    private var maxLength = 0
    private var typeInput = EditorInfo.TYPE_NULL
    private var errorLabelText: String = ""
    private var errorLabelTextOnEmpty: String = ""
    internal var isNotFocusYet = true
    internal var onFocusChange: ((view: View, focus: Boolean) -> Unit)? = null
    internal var afterTextChange: (text: String) -> Unit = {}
    internal var validateData: (text: String) -> Boolean = { false }
    private var textcolor = 0
    private var edtErrorTextColor = 0
    private var hintText = ""

    companion object {
        private const val TYPE_PASSWORD = 129 /*flag name="textPassword" value="0x00000081"*/
    }

    init {
        initView(attrs)
        initState()
    }

    internal fun setValidState(isValid: Boolean) {
        this.isValid = isValid
        tvLabel.isSelected = inputField?.hasFocus() ?: false
        tvLabel.isActivated = isValid()
        breakLine?.isSelected = inputField?.hasFocus() ?: false
        breakLine?.isActivated = isValid()
        errorLabel?.isSelected = inputField?.hasFocus() ?: false
        errorLabel?.isActivated = isValid()
        errorLabel?.visibility = if (isValid()) View.GONE else View.VISIBLE
        if (isValid) {
            inputField.setTextColor(textcolor)
        } else {
            inputField.setTextColor(edtErrorTextColor)
        }
        inputField?.isActivated = !isValid
        updateDetailLabelState()
    }

    internal fun isValid() = isValid || !isRequired

    internal fun isValidateDataNotEmpty() = isValid && getText().isNotEmpty()

    internal fun resetView() {
        inputField.text.clear()
        setValidState(true)
    }

    internal fun setValue(text: String) {
        inputField.setText(text)
    }

    internal fun hideViewLineAndDisableInput() {
        inputField.isEnabled = false
        breakLine.gone()
    }

    internal fun getText() = inputField.text.toString().trim()

    internal fun getTextNotTrim() = inputField.text.toString().trim()

    private fun initView(attrs: AttributeSet?) {
        View.inflate(context, R.layout.input_group_layout, this)
        val styleAttrs = context.obtainStyledAttributes(attrs, R.styleable.InputGroupLayout)
        try {
            tvLabel.text = styleAttrs.getString(R.styleable.InputGroupLayout_label_text)
            isRequired = styleAttrs.getBoolean(R.styleable.InputGroupLayout_isRequired, true)
            isElippsizeEnd =
                styleAttrs.getBoolean(R.styleable.InputGroupLayout_is_elipsize_end, true)
            hasDrawableRight =
                styleAttrs.getBoolean(R.styleable.InputGroupLayout_label_hasDrawableRight, false)
            isTrim =
                styleAttrs.getBoolean(R.styleable.InputGroupLayout_is_trim, true)
            textcolor =
                styleAttrs.getColor(
                    R.styleable.InputGroupLayout_edtTextColor,
                    ContextCompat.getColor(context, R.color.colorTundora)
                )
            edtErrorTextColor =
                styleAttrs.getColor(R.styleable.InputGroupLayout_edtErrorTextColor, textcolor)
            inputField.setTextColor(textcolor)
            if (!isRequired || !hasDrawableRight) {
                tvLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            }
            when (styleAttrs.getInt(R.styleable.InputGroupLayout_label_visibility, 0)) {
                0 -> tvLabel.visibility = View.VISIBLE
                1 -> tvLabel.visibility = View.INVISIBLE
                2 -> tvLabel.visibility = View.GONE
            }
            hintText = styleAttrs.getString(R.styleable.InputGroupLayout_edit_hint) ?: ""
            inputField?.apply {
                setText(styleAttrs.getString(R.styleable.InputGroupLayout_edit_text))
                hint = hintText
                layoutParams?.height =
                    styleAttrs.getDimensionPixelSize(
                        R.styleable.InputGroupLayout_edit_height,
                        context.dip(26)
                    )
                maxline = styleAttrs.getInt(R.styleable.InputGroupLayout_edit_maxLines, 1)
                maxLength = styleAttrs.getInt(
                    R.styleable.InputGroupLayout_edit_maxLength,
                    maxLength
                )
                maxLines = maxline
                typeInput = styleAttrs.getInt(
                    R.styleable.InputGroupLayout_edit_inputType, EditorInfo.TYPE_NULL
                )
                inputType = typeInput
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        v: TextView?, actionId: Int, event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                            hideKeyboard()
                            return true
                        }
                        return false
                    }
                })
                if (maxline > 1) {
                    overScrollMode = View.OVER_SCROLL_ALWAYS
                    scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                    isVerticalScrollBarEnabled = true
                    setOnTouchListener { view, motionEvent ->
                        view.parent.requestDisallowInterceptTouchEvent(true)
                        when (motionEvent.action and MotionEvent.ACTION_MASK) {
                            MotionEvent.ACTION_SCROLL -> {
                                view.parent.requestDisallowInterceptTouchEvent(false)
                            }
                        }
                        false
                    }
                }
            }
            styleAttrs.getInt(R.styleable.InputGroupLayout_edit_maxLength, -1).let { maxLength ->
                if (maxLength >= 0) {
                    inputField?.filters = arrayOf(InputFilter.LengthFilter(maxLength))
                }
            }
            detailLabel?.text = styleAttrs.getString(R.styleable.InputGroupLayout_detail_text)
            isDetailHiddenOnError =
                styleAttrs.getBoolean(R.styleable.InputGroupLayout_detail_isHiddenOnError, true)
            updateDetailLabelState()
            errorLabelText = styleAttrs.getString(R.styleable.InputGroupLayout_error_text) ?: ""
            errorLabelTextOnEmpty =
                styleAttrs.getString(R.styleable.InputGroupLayout_error_textOnEmpty) ?: ""
            errorLabel?.text = errorLabelText
        } finally {
            styleAttrs.recycle()
        }
    }

    private fun initState() {
        tvLabel.isSelected = false
        tvLabel.isActivated = true
        errorLabel?.isSelected = false
        errorLabel?.isActivated = true
        breakLine?.isSelected = false
        breakLine?.isActivated = true
        inputField?.setOnFocusChangeListener { view, isFocus ->
            if (isFocus) {
                isNotFocusYet = false
            }
            if (isElippsizeEnd && maxline == 1 && typeInput != TYPE_PASSWORD) {
                if (!isFocus) {
                    inputField?.keyListener = null
                } else {
                    inputField?.inputType = InputType.TYPE_CLASS_TEXT
                }
            }
            setValidState(isValid())
            onFocusChange?.invoke(view, isFocus)
        }

        inputField?.addTextChangedListener(object : TextWatcher {
            var ignoreChange = false

            override fun afterTextChanged(s: Editable?) {
                var text = if (isTrim) {
                    s?.toString()?.trim() ?: ""
                } else {
                    s?.toString() ?: ""
                }
                if (typeInput == EditorInfo.TYPE_CLASS_NUMBER && !ignoreChange) {
                    var textDigit = ""
                    ignoreChange = true
                    if (text.length > maxLength && maxLength != 0) {
                        text = text.substring(0, maxLength)
                    }
                    text.forEach { char ->
                        if (char.isDigit()) {
                            textDigit += char
                        }
                    }
                    inputField?.setText(textDigit)
                    inputField?.setSelection(inputField.text.toString().length)
                    ignoreChange = false
                    setValidState(validateData(textDigit))
                    errorLabel?.text =
                        if (textDigit.isEmpty()) errorLabelTextOnEmpty else errorLabelText
                    afterTextChange.invoke(textDigit)
                } else {
                    if (typeInput != EditorInfo.TYPE_CLASS_NUMBER) {
                        setValidState(validateData(text))
                        errorLabel?.text =
                            if (text.isEmpty()) errorLabelTextOnEmpty else errorLabelText
                        afterTextChange.invoke(text)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        inputField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: TextView?, actionId: Int, event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard()
                    return true
                }
                return false
            }
        })
    }

    internal fun getErrorLabel() = errorLabel

    internal fun setTextErrorLabel(colorLabel: Int, colorLine: Int) {
        errorLabel.setTextColor(colorLabel)
        tvLabel.setTextColor(colorLabel)
        breakLine.setBackgroundColor(colorLine)
    }

    internal fun setLabelError(textError: String) {
        setValidState(false)
        errorLabel.apply {
            visibility = View.VISIBLE
            text = textError
        }
    }

    internal fun setText(text: String?) {
        text?.let {
            inputField.setText(text)
        }
    }

    internal fun setLable(text: String) {
        tvLabel.text = text
    }

    private fun updateDetailLabelState() {
        if (isDetailHiddenOnError) {
            if (isValid() && detailLabel.text.isNotEmpty()) {
                detailLabel.visible()
            } else {
                detailLabel.gone()
            }
        } else {
            if (detailLabel.text.isNotEmpty()) {
                detailLabel.visible()
            } else {
                detailLabel.gone()
            }
        }
    }
}
