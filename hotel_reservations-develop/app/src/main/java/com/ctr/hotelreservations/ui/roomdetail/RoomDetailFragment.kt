package com.ctr.hotelreservations.ui.roomdetail

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.PromoResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.booking.BookingActivity
import com.ctr.hotelreservations.ui.home.rooms.RoomFragment.Companion.KEY_BRAND
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity.Companion.KEY_END_DATE
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity.Companion.KEY_ROOM_TYPE_STATUS
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity.Companion.KEY_START_DATE
import com.ctr.hotelreservations.util.parseToCalendar
import com.ctr.hotelreservations.util.parseToString
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_room_detail.*
import kotlinx.android.synthetic.main.layout_header_room_detail.*
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/06/11
 */
class RoomDetailFragment : BaseFragment() {
    private lateinit var viewModel: RoomDetailVMContract
    private var brand: HotelResponse.Hotel.Brand? = null
    private var roomTypeStatus: RoomTypeResponse.RoomTypeStatus? = null
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var promoResponse: PromoResponse? = null

    companion object {
        fun newInstance() = RoomDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = RoomDetailViewModel(HotelRepository())
        initView()
        initRecyclerView()
        initListener()
        initSwipeRefresh()
    }

    private fun initRecyclerView() {
        rcvSale.let {
            it.setHasFixedSize(true)
            it.adapter = PromoAdapter(viewModel.getPromos())
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initView() {
        (activity as? RoomDetailActivity)?.intent?.extras?.apply {
            brand = getParcelable(KEY_BRAND) ?: HotelResponse.Hotel.Brand()
            roomTypeStatus = getParcelable(KEY_ROOM_TYPE_STATUS)
            startDate = getString(KEY_START_DATE)?.parseToCalendar()
            endDate = getString(KEY_END_DATE)?.parseToCalendar()
            getPromos(roomTypeStatus?.roomType?.id ?: -1, startDate ?: Calendar.getInstance())
        }

        roomTypeStatus?.let {
            context?.let { context ->
                Glide.with(context).load(it.roomType.thumbnail).into(imgBanner1)
                Glide.with(context).load(it.roomType.images[0].name).into(imgBanner2)
                Glide.with(context).load(it.roomType.images[1].name).into(imgBanner3)
                Glide.with(context).load(it.roomType.images[2].name).into(imgBanner4)
            }

            tvTypeRoom.text = it.roomType.name
            tvCodePlace.text = getString(R.string.room_code, it.roomType.id.toString())
            tvPerNight.text = getString(R.string.per_night, 1)
            tvRoomPrice.text = it.roomType.price.toString().getPriceFormat()
        }

        brand?.let {
            tvName.text = it.name
            tvAddress.text = brand?.address
        }
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }
        lnBooking.onClickDelayAction {
            BookingActivity.start(
                this,
                Gson().fromJson(
                    brand?.toJsonString(),
                    HotelResponse.Hotel.Brand::class.java
                ),
                roomTypeStatus ?: RoomTypeResponse.RoomTypeStatus(),
                startDate?.parseToString() ?: Calendar.getInstance().parseToString(),
                endDate?.parseToString() ?: Calendar.getInstance().parseToString(),
                viewModel.getMaxPromo()
            )
        }
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getPromos(
                roomTypeStatus?.roomType?.id ?: -1, startDate ?: Calendar.getInstance()
            )
        }
    }

    private fun getPromos(roomTypeId: Int, startDate: Calendar) {
        addDisposables(
            viewModel.getPromoByRoomType(roomTypeId, startDate)
                .observeOnUiThread()
                .subscribe({
                    promoResponse = it
                    rcvSale.adapter?.notifyDataSetChanged()
                    viewModel.getMaxPromo()?.let {
                        val promoPercent = 100 - it.percentDiscount
                        tvRoomPrice.text =
                            (roomTypeStatus?.roomType?.price?.apply { Log.d("--=", "+${this}") }
                                ?.times(promoPercent / 100.0)).toString()
                                .getPriceFormat()
                        tvOldPrice.visible()
                        tvOldPrice.text =
                            roomTypeStatus?.roomType?.price.toString().getPriceFormat()
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()
}
