package com.liad.droptask.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.liad.droptask.R


fun changeFragment(
    fragmentManager: FragmentManager, @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    val fragmentTransaction = fragmentManager.beginTransaction()
    if (addToBackStack) fragmentTransaction.addToBackStack(null)
    fragmentTransaction.setCustomAnimations(
        R.anim.abc_fade_in,
        R.anim.abc_shrink_fade_out_from_bottom,
        R.anim.abc_grow_fade_in_from_bottom,
        R.anim.abc_popup_exit
    )
    fragmentTransaction.replace(containerId, fragment, fragment::class.java.simpleName).commit()
}

fun showKeyboard(context: Context, editText: EditText?) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}