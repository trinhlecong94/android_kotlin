package com.ctr.hotelreservations.ui.home.favotite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.ui.home.MainActivity
import com.ctr.hotelreservations.ui.home.MainActivity.Companion.TAB_HOME_POSITION
import kotlinx.android.synthetic.main.layout_view_no_data.*

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class FavoriteFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            FavoriteFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionMore.onClickDelayAction {
            (activity as? MainActivity)?.setTabSelection(TAB_HOME_POSITION)
        }
    }

    override fun isNeedPaddingTop() = true
}
