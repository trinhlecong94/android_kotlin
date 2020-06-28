package com.ctr.hotelreservations.ui.splash

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.LocalRepository

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class SplashFragment : BaseFragment() {
    private var viewModel: SplashVMContract? = null

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = context?.let { SplashViewModel(LocalRepository(it)) }

        Handler().postDelayed({
            viewModel?.let {
                if (it.isFirstLunch()) {
                    (activity as? SplashActivity)?.startOnBoardingActivity()
                } else {
                    it.getLoginToken().apply { Log.d("--=", "getLoginToken+${this}") }
                    it.getUserId().apply { Log.d("--=", "getUserId+${this}") }
                    if (it.getLoginToken().isNullOrEmpty()) {
                        (activity as? SplashActivity)?.startAuthActivity(this)
                    } else {
                        (activity as? SplashActivity)?.startHomeActivity()
                    }
                }
            }
        }, 1000L)
    }
}
