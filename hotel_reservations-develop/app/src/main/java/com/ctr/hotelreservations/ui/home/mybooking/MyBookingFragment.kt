package com.ctr.hotelreservations.ui.home.mybooking

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.bus.RxBus
import com.ctr.hotelreservations.data.model.UpdateMyBooking
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.response.MyBookingResponse
import com.ctr.hotelreservations.extension.invisible
import com.ctr.hotelreservations.extension.observeOnUiThread
import com.ctr.hotelreservations.extension.showErrorDialog
import com.ctr.hotelreservations.extension.visible
import kotlinx.android.synthetic.main.fragment_home.swipeRefresh
import kotlinx.android.synthetic.main.fragment_my_booking.*
import kotlinx.android.synthetic.main.layout_view_no_data.*

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingFragment : BaseFragment() {
    private lateinit var viewModel: MyBookingVMContract

    companion object {
        fun newInstance() =
            MyBookingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            MyBookingViewModel(
                HotelRepository()
            )
        getMyBookings()
        initView()
        initRecyclerView()
        initSwipeRefresh()
    }

    override fun onResume() {
        super.onResume()
        addDisposables(RxBus.listen(UpdateMyBooking::class.java)
            .observeOnUiThread()
            .subscribe {
                Log.d("--=", "onResume: ${it.isNeedUpdate}")
                if (it.isNeedUpdate) {
                    getMyBookings()
                }
            })
    }

    private fun initView() {
        ivNoData.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_empty_data))
        tvNoData.text = resources.getString(R.string.no_data_booking)
        tvActionMore.invisible()
    }

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        rclBooking.let {
            it.setHasFixedSize(true)
            it.adapter = MyBookingAdapter(
                viewModel.getBookings()
            ).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun handlerItemClick(booking: MyBookingResponse.MyBooking) {
        (parentFragment as? MyBookingContainerFragment)?.openPaymentFragment(booking)
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getMyBookings()
        }
    }

    internal fun getMyBookings() {
        Log.d("--=", "getMyBookings: ")
        addDisposables(
            viewModel.getBookingHistory()
                .observeOnUiThread()
                .subscribe({
                    if (it.length == 0) {
                        llNoData.visible()
                    } else {
                        llNoData.invisible()
                    }
                    rclBooking.adapter?.notifyDataSetChanged()
                }, {
                    handlerGetApiError(it)
                })
        )
    }


    private fun handlerGetApiError(throwable: Throwable) {
        activity?.showErrorDialog(throwable)
    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()
}
