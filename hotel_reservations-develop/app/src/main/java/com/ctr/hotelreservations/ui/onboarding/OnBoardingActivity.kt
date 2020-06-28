package com.ctr.hotelreservations.ui.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.ui.auth.AuthActivity
import com.ctr.hotelreservations.util.SharedReferencesUtil
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by at-trinhnguyen2 on 2020/06/07
 */
class OnBoardingActivity : AppCompatActivity() {
    companion object {
        private const val MIN_SCALE = 0.85f
        private const val NUM_PAGES = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        initViewPager()
        initListener()
    }

    private fun initListener() {
        btnNext.onClickDelayAction {
            if (viewPager.currentItem == NUM_PAGES - 1) {
                SharedReferencesUtil.setBoolean(
                    this,
                    SharedReferencesUtil.KEY_IS_FIRST_LAUNCH,
                    false
                )
                AuthActivity.start(this, isOpenLogin = true, isShowButtonBack = false)
                finishAffinity()
            }
            viewPager.currentItem = viewPager.currentItem + 1
        }
    }

    private fun initViewPager() {
        viewPager.apply {
            adapter = HomeViewPagerAdapter(supportFragmentManager)
            setPageTransformer(
                false
            ) { page, position ->
                when {
                    position < -1 -> {    // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        page.alpha = 0f

                    }
                    position <= 0 -> {    // [-1,0]
                        page.alpha = 1f
                        page.translationX = 0f
                        page.scaleX = 1f
                        page.scaleY = 1f

                    }
                    position <= 1 -> {    // (0,1]
                        page.translationX = -position * page.width
                        page.alpha = 1 - abs(position)
                        page.scaleX = max(1 - abs(position), MIN_SCALE)
                        page.scaleY = max(1 - abs(position), MIN_SCALE)

                    }
                    else -> {    // (1,+Infinity]
                        // This page is way off-screen to the right.
                        page.alpha = 0f
                    }
                }
            }
        }
        indicator.apply {
            setViewPager(viewPager)
        }
    }

    class HomeViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int = NUM_PAGES

        override fun getItem(i: Int): BaseFragment {
            return when (i) {
                0, 1 -> {
                    OnBoardingPageFragment.getInstance(i)
                }
                else -> OnBoardingSliderFragment.getInstance()
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "OBJECT ${(position + 1)}"
        }
    }
}

