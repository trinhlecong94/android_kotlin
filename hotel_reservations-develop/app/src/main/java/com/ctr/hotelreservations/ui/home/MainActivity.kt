package com.ctr.hotelreservations.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseActivity
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.ui.home.account.AccountContainerFragment
import com.ctr.hotelreservations.ui.home.favotite.FavoriteContainerFragment
import com.ctr.hotelreservations.ui.home.hotels.HomeContainerFragment
import com.ctr.hotelreservations.ui.home.mybooking.MyBookingContainerFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
        internal const val PAGER_NUMBER = 4
        internal const val TAB_HOME_POSITION = 0
        internal const val TAB_SAVE_POSITION = 1
        internal const val TAB_MY_BOOKING_POSITION = 2
        internal const val TAB_ACCOUNT_POSITION = 3

        internal fun start(from: Activity) {
            MainActivity().apply {
                from.startActivity(Intent(from, MainActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewPager()
        initTab()
    }

    override fun getContainerId() = R.id.container

    private fun initTab() {
        val icons = mutableListOf(
            R.drawable.bg_icon_tab_home,
            R.drawable.bg_icon_tab_save,
            R.drawable.bg_icon_tab_my_booking,
            R.drawable.bg_icon_tab_account
        )
        for (i in 0 until PAGER_NUMBER) {
            val tab =
                View.inflate(this@MainActivity, R.layout.item_tab_layout, null) as? ConstraintLayout
            (tab?.findViewById(R.id.ivIconTab) as? ImageView)?.setImageResource(icons[i])
            (tab?.findViewById(R.id.tvTitleTab) as? TextView)?.text =
                viewPager.adapter?.getPageTitle(i)
            tabLayout.getTabAt(i)?.customView = tab
        }
    }

    private fun initViewPager() {
        viewPager.apply {
            adapter = HomeFragmentPagerAdapter(
                supportFragmentManager,
                resources.getStringArray(R.array.main_tab_title)
            )
            offscreenPageLimit = PAGER_NUMBER
            tabLayout.setupWithViewPager(this)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    val currentFragment = getFragmentInsideViewPager()
                    while ((currentFragment?.childFragmentManager?.backStackEntryCount ?: 0) > 1) {
                        currentFragment?.childFragmentManager?.popBackStackImmediate()
                    }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) = Unit

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    p0?.let {
                    }
                }
            })
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {}

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(p0: Int) {
                }
            })
        }
    }

    internal fun getFragmentInsideViewPager(position: Int = viewPager.currentItem) =
        viewPager.adapter?.instantiateItem(viewPager, position) as? BaseFragment

    /**
     * HomeFragmentPagerAdapter
     */
    class HomeFragmentPagerAdapter(fm: FragmentManager, private val titles: Array<String>) :
        FragmentStatePagerAdapter(fm) {

        override fun getItem(p0: Int): Fragment {
            return when (p0) {
                TAB_HOME_POSITION -> HomeContainerFragment.getNewInstance()
                TAB_SAVE_POSITION -> FavoriteContainerFragment.getNewInstance()
                TAB_MY_BOOKING_POSITION -> MyBookingContainerFragment.getNewInstance()
                else -> AccountContainerFragment.getNewInstance()
            }
        }

        override fun getCount(): Int = PAGER_NUMBER

        override fun getPageTitle(position: Int) = titles[position]
    }

    override fun onBackPressed() {
        val fragment = getFragmentInsideViewPager()
        fragment?.let {
            if (it.childFragmentManager.backStackEntryCount > 1) {
                it.childFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

    internal fun showDatePickerDialog(dialog: DatePickerDialog) {
        dialog.show(fragmentManager, "DatePickerDialog")
    }

    internal fun setOnDateSetListener(dialog: DatePickerDialog.OnDateSetListener) {
        val datePickerDialog =
            fragmentManager?.findFragmentByTag("DatePickerDialog") as? DatePickerDialog
        datePickerDialog?.setOnDateSetListener(dialog)
    }

    internal fun setTabSelection(position: Int) {
        tabLayout.getTabAt(position)?.select()
    }
}
