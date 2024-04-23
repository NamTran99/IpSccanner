@file:Suppress("DEPRECATION")

package com.victorb.androidnetworkscanner.extension

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.lang.reflect.ParameterizedType

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun Number.toDp() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_PX,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()


/**
 * @return Pair of window width and window height (not count status bar and navigation bar)
 * */
fun Fragment.getScreenSize(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels to displayMetrics.heightPixels
}

@Suppress("UNCHECKED_CAST")
fun <VB : ViewDataBinding> getBaseFragmentBinding(
    clazz: Class<*>,
    inflater: LayoutInflater,
    container: ViewGroup?,
): VB? {
    return try {
        (clazz.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments
            ?.getOrNull(0)
            ?.let {
                (it as Class<*>).getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                ).let { method ->
                    method.invoke(null, inflater, container, false) as VB
                }
            }
    } catch (e: Exception) {
        null
    }
}


@Suppress("UNCHECKED_CAST")
fun <VB : ViewDataBinding> getViewHolderBinding(
    clazz: Class<*>,
    inflater: LayoutInflater,
    container: ViewGroup?,
): VB? {
    return try {
        (clazz.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments
            ?.getOrNull(1)
            ?.let {
                (it as Class<*>).getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                ).let { method ->
                    method.invoke(null, inflater, container, false) as VB
                }
            }
    } catch (e: Exception) {
        null
    }
}
