package com.ctr.hotelreservations.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.extension.replaceFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
abstract class HomeContainerBaseFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(
            R.id.container,
            getRootFragment(),
            addToBackStack = true
        )
    }

    override fun getContainerId() = R.id.container

    abstract fun getRootFragment(): BaseFragment
}
