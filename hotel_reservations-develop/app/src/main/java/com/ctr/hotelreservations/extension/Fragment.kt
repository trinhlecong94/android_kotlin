package com.ctr.hotelreservations.extension

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ctr.hotelreservations.R
import com.google.android.material.snackbar.Snackbar

/**
 * Use this extension for Fragment
 */

internal fun Fragment.addFragment(
    containerId: Int,
    fragment: Fragment,
    transactionCallback: ((transaction: FragmentTransaction) -> Unit)? = null,
    addToBackStack: Boolean = false,
    tag: String? = null
) {
    if (childFragmentManager.findFragmentByTag(tag ?: fragment.javaClass.simpleName) == null) {
        val transaction = childFragmentManager.beginTransaction()
        transactionCallback?.invoke(transaction)
        transaction.add(containerId, fragment, tag ?: fragment.javaClass.simpleName)
        if (addToBackStack) {
            transaction.addToBackStack(tag ?: fragment.javaClass.simpleName)
        }
        transaction.commit()
    }
}

internal fun Fragment.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    transactionCallback: ((transaction: FragmentTransaction) -> Unit)? = null,
    addToBackStack: Boolean = false,
    tag: String? = null
) {
    if (childFragmentManager.findFragmentByTag(tag ?: fragment.javaClass.simpleName) == null) {
        val transaction = childFragmentManager.beginTransaction()
        transactionCallback?.invoke(transaction)
        transaction.replace(containerId, fragment, tag ?: fragment.javaClass.simpleName)
        if (addToBackStack) {
            transaction.addToBackStack(tag ?: fragment.javaClass.simpleName)
        }
        transaction.commit()
    }
}

internal fun Fragment.showSnackBar(
    rootView: View,
    message: String,
    actionText: String? = null,
    actionTextColor: Int? = null,
    rightButtonOnclickListener: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    actionText?.let { action ->
        snackbar.setAction(action) {
            rightButtonOnclickListener?.invoke()
        }
        actionTextColor?.let { color ->
            snackbar.setActionTextColor(color)
        }
    }
    val layoutParams = (snackbar.view.layoutParams as? FrameLayout.LayoutParams)
        ?.apply {
            leftMargin = resources.getDimensionPixelSize(R.dimen.snackbar_margin)
            rightMargin = resources.getDimensionPixelSize(R.dimen.snackbar_margin)
            bottomMargin = resources.getDimensionPixelSize(R.dimen.snackbar_margin)
        }
    snackbar.view.layoutParams = layoutParams
    snackbar.show()
}

internal fun Fragment.sortInputFieldAction(vararg views: EditText) {
    (0 until views.size - 1).forEach { index ->
        (views[index] as? EditText)?.let {
            it.imeOptions = EditorInfo.IME_ACTION_NEXT
            it.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    (index + 1 until views.size).forEach {
                        val nextView = views[it]
                        if (nextView.isEnabled && nextView.visibility == View.VISIBLE) {
                            nextView.requestFocus()
                            return@setOnEditorActionListener true
                        }
                    }
                }
                true
            }
        }
    }
}
