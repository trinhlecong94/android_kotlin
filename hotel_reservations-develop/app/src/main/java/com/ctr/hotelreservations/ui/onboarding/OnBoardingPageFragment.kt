package com.ctr.hotelreservations.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_on_boarding_page.*

/**
 * Created by at-trinhnguyen2 on 2020/06/07
 */
class OnBoardingPageFragment : BaseFragment() {

    companion object {
        fun getInstance(page: Int) = OnBoardingPageFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_PAGE_NUMBER, page)
            }
        }

        internal const val KEY_PAGE_NUMBER = "key_page_number"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_on_boarding_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (arguments?.getInt(KEY_PAGE_NUMBER)) {
            0 -> {
                container.tag = 0
                imageView.setImageResource(R.drawable.bg_onboard1)
                textView.text =
                    "Welcome to Mường Thanh - Chuỗi khách sạn lớn nhất Đông Nam Á"
            }
            1 -> {
                container.tag = 1
                imageView.setImageResource(R.drawable.bg_onboard2)
                textView.text = "Trải nghiệm phong cách du lịch bản địa, sang trọng và đẳng cấp"
            }
        }
    }
}