package com.olsera.coreutil

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.zr.coreutil.R
import com.zr.coreutil.databinding.CustomEditTextBinding

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {

    private val binding : CustomEditTextBinding

    init {
        orientation = VERTICAL
        binding = CustomEditTextBinding.inflate(LayoutInflater.from(context), this)
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0).apply {
            try {
                val label = getString(R.styleable.CustomEditText_label)
                val text = getString(R.styleable.CustomEditText_text)
                binding.textTitle.text = label
                binding.inputText.setText(text)
            } finally {
                recycle()
            }
        }
    }

    fun setLabel(label: String) {
        binding.textTitle.text = label
    }

    fun setText(text: String) {
        binding.inputText.setText(text)
    }

    fun getText(): String = binding.inputText.text.toString()

}