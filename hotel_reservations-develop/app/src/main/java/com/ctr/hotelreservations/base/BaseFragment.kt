package com.ctr.hotelreservations.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ctr.hotelreservations.extension.getStatusBarHeight
import com.ctr.hotelreservations.extension.observeOnUiThread
import com.ctr.hotelreservations.ui.wedget.ProgressBarDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
abstract class BaseFragment : Fragment() {

    companion object {
        internal const val STATUS_BAR_ICON_THEME_WHITE = 1
        internal const val STATUS_BAR_ICON_THEME_BLACK = 2
    }

    protected var isDataChanged = false
    private val compositeDisposables = CompositeDisposable()
    private var progressDialog: ProgressBarDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            progressDialog = ProgressBarDialog(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.isClickable = true
        if (isNeedPaddingTop()) {
            view.setPadding(
                0,
                context?.getStatusBarHeight() ?: 0,
                0,
                0
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (statusIconTheme()) {
                STATUS_BAR_ICON_THEME_BLACK -> {
                    view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }

                STATUS_BAR_ICON_THEME_WHITE -> {
                    view.systemUiVisibility = 0
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getProgressBarControlObservable()?.let {
            addDisposables(
                it.observeOnUiThread()
                    .subscribe(this::setProgressBarState)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposables.clear()
    }

    open fun getContainerId() = -1

    open fun getCurrentFragment(): BaseFragment? =
        childFragmentManager.findFragmentById(getContainerId()) as? BaseFragment

    open fun onComeBack(dataChanged: Boolean) = Unit

    open fun isNeedPaddingTop(): Boolean = false

    open fun statusIconTheme(): Int = STATUS_BAR_ICON_THEME_WHITE

    protected open fun getProgressBarControlObservable(): BehaviorSubject<Boolean>? = null

    internal fun isChangeData() = isDataChanged

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach {
            compositeDisposables.add(it)
        }
    }

    protected fun setProgressBarState(state: Boolean) {
        if (state) {
            progressDialog?.show()
        } else {
            progressDialog?.dismiss()
        }
    }
}
