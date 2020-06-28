package com.ctr.hotelreservations.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_on_boarding_slider.*


/**
 * Created by at-trinhnguyen2 on 2020/06/07
 */
class OnBoardingSliderFragment : BaseFragment() {
    private val handler = Handler()
    private lateinit var runnable: Runnable

    companion object {
        fun getInstance() = OnBoardingSliderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_on_boarding_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = SliderAdapter(
                listOf(
                    resources.getDrawable(R.drawable.photo_sample),
                    resources.getDrawable(R.drawable.photo_sample_1),
                    resources.getDrawable(R.drawable.photo_sample_2)
                )
            )
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val firstItemVisible = linearLayoutManager.findFirstVisibleItemPosition()
                    if (firstItemVisible != 1 && firstItemVisible % 3 == 1) {
                        linearLayoutManager.scrollToPosition(1)
                    }
                    val firstCompletelyItemVisible =
                        linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (firstCompletelyItemVisible == 0) {
                        linearLayoutManager.scrollToPositionWithOffset(3, 0)
                    }
                }
            })
        }
        val speedScroll = 5L
        runnable = object : Runnable {
            override fun run() {
                recyclerView?.scrollBy(2, 0)
                handler.postDelayed(this, speedScroll)
            }
        }
        handler.postDelayed(runnable, speedScroll)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}