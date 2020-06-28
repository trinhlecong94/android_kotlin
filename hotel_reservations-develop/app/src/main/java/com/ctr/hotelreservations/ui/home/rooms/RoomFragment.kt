package com.ctr.hotelreservations.ui.home.rooms

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.home.MainActivity
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity
import com.ctr.hotelreservations.ui.wedget.NumberPicker
import com.ctr.hotelreservations.util.DateUtil
import com.ctr.hotelreservations.util.DateUtil.FORMAT_DATE_TIME_CHECK_IN
import com.ctr.hotelreservations.util.DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK
import com.ctr.hotelreservations.util.compareDay
import com.ctr.hotelreservations.util.parseToString
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_room_of_brand.*
import kotlinx.android.synthetic.main.include_layout_select_date.*
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class RoomFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModel: RoomVMContract
    private lateinit var brand: HotelResponse.Hotel.Brand
    private val startDate = Calendar.getInstance()
    private val endDate = Calendar.getInstance().apply { add(Calendar.DATE, 1) }
    private var numOfGuest = 1
    private var numOfRoom = 1
    private lateinit var datePicker: DatePickerDialog

    companion object {
        internal const val KEY_BRAND = "key_brand"

        fun getInstance(brand: HotelResponse.Hotel.Brand) =
            RoomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        KEY_BRAND,
                        brand
                    )
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_of_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = RoomViewModel(
            HotelRepository()
        )
        brand = arguments?.getParcelable(KEY_BRAND)
            ?: HotelResponse.Hotel.Brand()
        brand.let {
            context?.let { context -> Glide.with(context).load(it.imgLink).into(ivBrand) }
            tvName.text = it.name
            tvDescription.text =
                if (it.desciption.isNullOrEmpty()) "Brand have no description" else it.desciption
        }
        getAllRoomStatus(
            brand.id,
            startDate.parseToString(),
            endDate.parseToString(),
            numOfGuest,
            numOfRoom
        )
        initListener()
        initRecyclerView()
        initSwipeRefresh()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setOnDateSetListener(this)
    }

    override fun isNeedPaddingTop() = true

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        viewSelected.onClickDelayAction {
            val calendar = Calendar.getInstance()
            datePicker = DatePickerDialog.newInstance(
                this,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.apply {
                isAutoHighlight = true
                setEndTitle("Check Out")
                setStartTitle("Check In")
                minDate = calendar
            }
            datePicker.selectableDays
            (activity as? MainActivity)?.showDatePickerDialog(datePicker)
        }

        lnGuests.onClickDelayAction {
            context?.let { context ->
                val dialog = Dialog(context)
                dialog.apply {
                    setContentView(R.layout.layout_dialog_number_picker)
                    val pickerGuestNo = findViewById<NumberPicker>(R.id.pickerGuestNo)
                    val pickerRoomNo = findViewById<NumberPicker>(R.id.pickerRoomNo)
                    pickerGuestNo.apply {
                        setMax(50)
                        setValue(numOfGuest)
                    }
                    pickerRoomNo.apply {
                        setMax(50)
                        setValue(numOfRoom)
                    }

                    setOnDismissListener {
                        Log.d("--=", "initListener: setOnDismissListener")
                        numOfGuest = pickerGuestNo.getValue()
                        numOfRoom = pickerRoomNo.getValue()
                        viewModel.filterRoomStatus(numOfGuest, numOfRoom)
                        this@RoomFragment.recyclerView.adapter?.notifyDataSetChanged()
                        this@RoomFragment.tvGuests.text =
                            if (numOfGuest < 10) "0$numOfGuest" else numOfGuest.toString()
                    }
                    show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = RoomAdapter(viewModel.getRoomTypes(), brand).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun handlerItemClick(room: RoomTypeResponse.RoomTypeStatus) {
        RoomDetailActivity.start(
            this,
            Gson().fromJson(brand.toJsonString(), HotelResponse.Hotel.Brand::class.java),
            room,
            startDate.parseToString(),
            endDate.parseToString()
        )
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getAllRoomStatus(
                brand.id,
                startDate.parseToString(),
                endDate.parseToString(),
                numOfGuest,
                numOfRoom
            )
        }
    }

    private fun getRooms(brandId: Int) {
        addDisposables(
            viewModel.getAllRoomByBrand(brandId)
                .observeOnUiThread()
                .subscribe({
                    recyclerView.adapter?.notifyDataSetChanged()
                }, {
                    handlerGetApiError(it)
                })
        )
    }

    private fun getAllRoomStatus(
        brandId: Int,
        startDate: String,
        endDate: String,
        numOfGuest: Int,
        numOfRoom: Int
    ) {
        addDisposables(
            viewModel.getAllRoomStatus(brandId, startDate, endDate, numOfGuest, numOfRoom)
                .observeOnUiThread()
                .subscribe({
                    recyclerView.adapter?.notifyDataSetChanged()
                }, {
                    handlerGetApiError(it)
                })
        )
    }


    private fun handlerGetApiError(throwable: Throwable) {
        activity?.showErrorDialog(throwable)
    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()

    override fun onDateSet(
        view: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        yearEnd: Int,
        monthOfYearEnd: Int,
        dayOfMonthEnd: Int
    ) {
        val start = Calendar.getInstance()
        start.set(year, monthOfYear, dayOfMonth, 14, 0, 0)
        val end = Calendar.getInstance()
        end.set(yearEnd, monthOfYearEnd, dayOfMonthEnd, 12, 0, 0)
        if (start.timeInMillis > end.timeInMillis) {
            activity?.showDialog(
                "Ops!",
                "Please select check out date greater than check in date!",
                "OK"
            )
        } else {
            startDate.set(year, monthOfYear, dayOfMonth, 14, 0, 0)
            endDate.set(yearEnd, monthOfYearEnd, dayOfMonthEnd, 12, 0, 0)
            lnSelected.invisible()
            lnStartDate.visible()
            tvRangeDate.visible()
            lnEndDate.visible()
            tvStartDayOfTheWeek.text = DateUtil.format(startDate, FORMAT_DATE_TIME_DAY_IN_WEEK)
            tvEndDayOfTheWeek.text = DateUtil.format(endDate, FORMAT_DATE_TIME_DAY_IN_WEEK)
            tvStartDate.text = DateUtil.format(startDate, FORMAT_DATE_TIME_CHECK_IN)
            tvEndDate.text = DateUtil.format(endDate, FORMAT_DATE_TIME_CHECK_IN)
            val dayNumber = startDate.compareDay(endDate)
            tvRangeDate.text = resources.getString(R.string.roomDayNumber, dayNumber)
            getAllRoomStatus(
                brand.id,
                startDate.parseToString(),
                endDate.parseToString(),
                numOfGuest,
                numOfRoom
            )
        }
    }
}
