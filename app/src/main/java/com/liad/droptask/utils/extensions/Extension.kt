package com.liad.droptask.utils.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import co.climacell.statefulLiveData.core.StatefulData
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

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun <T> MutableList<T>.clearAndAddAll(newData: List<T>) {
    clear()
    addAll(newData)
}

@MainThread
fun <T> LiveData<T>.observeOnceNullable(observer: Observer<T>, retainForLoadingState: Boolean = true) {
    this.observeForever(object : Observer<T> {
        override fun onChanged(t: T) {

            if (t !is StatefulData<*> || !retainForLoadingState || t !is StatefulData.Loading<*>) {
                removeObserver(this)
            }

            observer.onChanged(t)
        }
    })
}