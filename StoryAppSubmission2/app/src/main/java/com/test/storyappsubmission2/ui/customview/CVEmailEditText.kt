package com.test.storyappsubmission2.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.test.storyappsubmission2.R

class CVEmailEditText : AppCompatEditText {
    private lateinit var iconFormInput: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        iconFormInput = ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24_bl_600) as Drawable
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                showIconFormInput()
                error = if (s.isNotEmpty()) {
                    if (!s.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                        context.getString(R.string.label_invalid_email)
                    } else null
                } else null
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    private fun showIconFormInput() {
        setButtonDrawables(startOfTheText = iconFormInput)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        context.apply {
            setTextColor(ContextCompat.getColor(this, R.color.blue_500))
            setHintTextColor(ContextCompat.getColor(this, R.color.blue_500))
            background = ContextCompat.getDrawable(this, R.drawable.custom_view_form_input)
        }
        isSingleLine = true
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}