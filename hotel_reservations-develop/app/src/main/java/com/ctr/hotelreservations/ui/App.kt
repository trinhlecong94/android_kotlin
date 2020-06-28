package com.ctr.hotelreservations.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import com.ctr.hotelreservations.data.source.LocalRepository
import com.ctr.hotelreservations.extension.hideKeyboard
import io.reactivex.plugins.RxJavaPlugins

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class App : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        @SuppressLint("StaticFieldLeak")
        internal lateinit var instance: App
            private set
    }

    internal lateinit var localRepository: LocalRepository
    internal var currentActivity: Activity? = null
    internal var isIgnoreAppGoToBackground = false
    private var currentActivityCount = 0
    private var isAppGoToBackground = false
    private var gotoBackgroundTime = 0L

    override fun onCreate() {
        super.onCreate()
        localRepository = LocalRepository(applicationContext)
        instance = this
        registerActivityLifecycleCallbacks(this)
        // Add here to prevent crash app in case when Rx error make crash app
        RxJavaPlugins.setErrorHandler {}
    }

    override fun onActivityStopped(p0: Activity?) {
        currentActivityCount--
        if (isIgnoreAppGoToBackground) {
            isIgnoreAppGoToBackground = false
            return
        }
        if (currentActivityCount == 0) {
            isAppGoToBackground = true
            gotoBackgroundTime = SystemClock.elapsedRealtime()
        }
    }

    override fun onActivityPaused(p0: Activity?) {
        p0?.currentFocus?.clearFocus()
        p0?.hideKeyboard()
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityResumed(p0: Activity?) {
        currentActivity = p0
    }

    override fun onActivityDestroyed(p0: Activity?) = Unit

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) = Unit

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) = Unit
}
