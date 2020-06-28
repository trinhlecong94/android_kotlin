package com.ctr.hotelreservations.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.bus.RxBus
import com.ctr.hotelreservations.data.model.KeyBoardEvent
import com.ctr.hotelreservations.extension.getStatusBarHeight
import com.ctr.hotelreservations.extension.hideKeyboard
import com.ctr.hotelreservations.extension.isKeyboardOpened
import com.ctr.hotelreservations.extension.observeOnUiThread
import com.ctr.hotelreservations.ui.wedget.ProgressBarDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject


/**
 * Created by at-trinhnguyen2 on 2020/05/30
 */
abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private const val DELAY_FOR_HIDE_KEYBOARD = 100L
    }

    private val location = IntArray(2)
    private var isKeyboardShowed = false
    private var isKeyboardListenerAttach = false
    private val compositeDisposables = CompositeDisposable()
    private var progressDialog: ProgressBarDialog? = null

    private val keyBoardListener = ViewTreeObserver.OnGlobalLayoutListener {
        Handler().postDelayed({
            val isShow = isKeyboardOpened()
            if (isShow != isKeyboardShowed) {
                isKeyboardShowed = isShow
                RxBus.publish(KeyBoardEvent(isKeyboardShowed))
            }
        }, DELAY_FOR_HIDE_KEYBOARD)
    }

    open fun getContainerId(): Int = -1

    open fun getCurrentFragment(): Fragment? =
        supportFragmentManager.findFragmentById(getContainerId())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (getAppearAnimType()) {
            AppearAnim.SLIDE_UP -> {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_no_animation)
            }

            AppearAnim.SLIDE_FROM_RIGHT -> {
                overridePendingTransition(R.anim.anim_slide_right_in, R.anim.anim_no_animation)
            }
        }
        fullScreenActivity()
        progressDialog = ProgressBarDialog(this)
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
        hideKeyboard()
        super.onPause()
        compositeDisposables.clear()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_UP ->
                if (view is EditText) {
                    if (currentFocus !is EditText) {
                        currentFocus?.clearFocus()
                        view.hideKeyboard()
                    } else {
                        currentFocus?.let {
                            it.getLocationOnScreen(location)
                            val x = event.rawX + it.left - location[0]
                            val y = event.rawY + it.top - location[1]
                            if (x < it.left || x >= it.right || y < it.top || y > it.bottom) {
                                it.clearFocus()
                                it.hideKeyboard()
                            }
                        }
                    }
                }
        }
        return ret
    }

    override fun finish() {
        super.finish()
        when (getAppearAnimType()) {
            AppearAnim.SLIDE_UP -> {
                overridePendingTransition(R.anim.anim_no_animation, R.anim.anim_slide_down)
            }

            AppearAnim.SLIDE_FROM_RIGHT -> {
                overridePendingTransition(R.anim.anim_no_animation, R.anim.anim_slide_right_out)
            }
        }
    }

    open fun getAppearAnimType() = AppearAnim.SLIDE_FROM_RIGHT

//    internal fun onUnauthorizedError() {
//        SharedReferencesUtil.remove(this, SharedReferencesUtil.KEY_AUTO_LOGIN_TOKEN)
//        val intent = Intent(this, SplashActivity::class.java)
//        startActivity(intent)
//        finishAffinity()
//    }

    protected fun attachKeyboardListener(rootView: ViewGroup) {
        if (isKeyboardListenerAttach) {
            return
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(keyBoardListener)
        isKeyboardListenerAttach = true
    }

    internal fun removeKeyboardListener(rootView: ViewGroup) {
        if (isKeyboardListenerAttach) {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(keyBoardListener)
        }
    }

    protected fun paddingTop(vararg view: View) {
        view.forEach { it.setPadding(0, getStatusBarHeight(), 0, 0) }
    }

    protected open fun getProgressBarControlObservable(): BehaviorSubject<Boolean>? = null

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach {
            compositeDisposables.add(it)
        }
    }

    protected fun fullScreenActivity() {
        setTransparent(this)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
    }

    protected fun setProgressBarState(state: Boolean) {
        if (state) {
            progressDialog?.show()
        } else {
            progressDialog?.dismiss()
        }
    }

    enum class AppearAnim(val type: Int) {
        NO_ANIM(0), SLIDE_UP(1), SLIDE_FROM_RIGHT(2)
    }

    private fun setTransparent(activity: Activity) {
        transparentStatusBar(activity)
        setRootView(activity)
    }


    private fun setRootView(activity: Activity) {
        val parent =
            activity.findViewById<View>(android.R.id.content) as ViewGroup
        var i = 0
        val count = parent.childCount
        while (i < count) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(true)
                childView.clipToPadding = true
            }
            i++
        }
    }

    private fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}
