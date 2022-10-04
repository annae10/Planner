package com.ann.planner.presentation

import androidx.databinding.BindingAdapter
import com.ann.planner.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputTitle")
fun bindErrorInputTitle(textInputLayout: TextInputLayout, isError: Boolean){
    val message = if (isError){
        textInputLayout.context.getString(R.string.error_input_title)
    } else { null}
    textInputLayout.error = message
}