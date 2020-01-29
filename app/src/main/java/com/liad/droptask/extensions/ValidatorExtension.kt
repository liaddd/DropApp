package com.liad.droptask.extensions

import android.text.TextUtils
import com.google.android.material.textfield.TextInputLayout
import com.liad.droptask.R

fun TextInputLayout.validate(): Boolean {
    var isValid = false
    if (!TextUtils.isEmpty(editText?.text)) {
        when (editText?.tag) {
            resources.getString(R.string.full_name) -> {
                if (editText?.text!!.contains(" ")) {
                    isValid = true
                } else {
                    error = "please insert both First name and Last name"
                    editText?.requestFocus()
                }
            }
            resources.getString(R.string.phone_number) -> {
                isValid = true
                /*if (Patterns.PHONE.matcher(editText?.text).matches() && editText?.text.toString().length >= 10) {
                    isValid = true
                } else {
                    error = "Phone number doesn't allowed\nplease check the number you've inserted"
                    editText?.requestFocus()
                }*/
            }
            resources.getString(R.string.street_address) -> {

            }
            resources.getString(R.string.city) -> {

            }
            resources.getString(R.string.country) -> {

            }
            else -> return false //TODO("couldn't find tag")
        }
        if (isValid) isErrorEnabled = false
    } else {
        error = "${editText?.hint} cannot be empty!"
        editText?.requestFocus()
    }
    return isValid
}