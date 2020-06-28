package com.ctr.hotelreservations.ui.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.bus.RxBus
import com.ctr.hotelreservations.data.model.BookingStatus
import com.ctr.hotelreservations.data.model.UpdateMyBooking
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.UserRepository
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.PromoResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.App
import com.ctr.hotelreservations.ui.booking.BookingActivity.Companion.KEY_PROMO
import com.ctr.hotelreservations.ui.home.rooms.RoomFragment
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity
import com.ctr.hotelreservations.util.DateUtil
import com.ctr.hotelreservations.util.compareDay
import com.ctr.hotelreservations.util.parseToCalendar
import com.ctr.hotelreservations.util.parseToString
import kotlinx.android.synthetic.main.fragment_booking.*
import kotlinx.android.synthetic.main.layout_room_detail_booking.*
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
class BookingFragment : BaseFragment() {
    private lateinit var viewModel: BookingVMContract
    private var brand: HotelResponse.Hotel.Brand? = null
    private var roomTypeStatus: RoomTypeResponse.RoomTypeStatus? = null
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var promo: PromoResponse.Promo? = null
    private var numberOfDays = 1
    private var numberOfRooms = 1
    private var prize = 0.0
    private lateinit var datePicker: DatePickerDialog

    companion object {
        fun newInstance() = BookingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = BookingViewModel(
            App.instance.localRepository,
            HotelRepository(),
            UserRepository()
        )
        initView()
        initRecyclerView()
        initListener()
        getUserInfo()
    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        rcvStepBooking.apply {
            PagerSnapHelper().attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = BookingStepAdapter(
                listOf(
                    StepBookingUI("Booking"),
                    StepBookingUI("Payment"),
                    StepBookingUI("Check in"),
                    StepBookingUI("Review")
                )
            ).apply { setSelectedPosition(0) }
        }
    }

    private fun initView() {
        (activity as? BookingActivity)?.intent?.extras?.apply {
            brand = getParcelable(RoomFragment.KEY_BRAND) ?: HotelResponse.Hotel.Brand()
            roomTypeStatus = getParcelable(RoomDetailActivity.KEY_ROOM_TYPE_STATUS)
            startDate = getString(RoomDetailActivity.KEY_START_DATE)?.parseToCalendar()
            endDate = getString(RoomDetailActivity.KEY_END_DATE)?.parseToCalendar()
            promo = getParcelable(KEY_PROMO)
        }
        tvBookNow.isEnabled = false

        roomTypeStatus?.let {
            context?.let { context ->
                Glide.with(context).load(it.roomType.thumbnail).into(ivRoomThumb)
            }
            tvRoomTitle.text = it.roomType.name
            tvRoomInfo.text = it.roomType.getRoomTypeInfo()
            pickerRoomNo.setMax(it.totalRoomAvailable)
            pickerGuestNo.setMax(it.roomType.capacity)

            brand?.let { brand ->
                tvBrand.text = brand.name
                tvRoomAddress.text = brand.address
            }

            numberOfDays = startDate.compareDay(endDate)
            prize = it.roomType.price.toDouble()

            updateUI(startDate, endDate, numberOfDays, numberOfRooms, prize)

            viewModel.getRoomsReservationBody().let { body ->
                body.brandId = brand?.id
                body.roomTypeId = roomTypeStatus?.roomType?.id
                body.status = BookingStatus.PENDING.toString()
                body.id = viewModel.getUserId()
            }
        }
    }

    private fun updateUI(
        startDate: Calendar?,
        endDate: Calendar?,
        numberOfDays: Int,
        numberOfRooms: Int,
        prize: Double
    ) {
        tvStartDate.text = startDate?.parseToString(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
        tvEndDate.text = endDate?.parseToString(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
        tvCheckinTime.text = startDate?.parseToString(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvCheckOutTime.text = endDate?.parseToString(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvRangeDate.text = "${numberOfDays}D"

        viewModel.getRoomsReservationBody().let { body ->
            body.startDate = startDate?.parseToString(DateUtil.FORMAT_DATE_TIME_FROM_API_3)
            body.endDate = endDate?.parseToString(DateUtil.FORMAT_DATE_TIME_FROM_API_3)
        }

        updateTotalFee(numberOfDays, numberOfRooms, prize, promo?.percentDiscount ?: 0)
    }

    private fun updateTotalFee(
        numberOfDays: Int,
        numberOfRooms: Int,
        prize: Double,
        promoPercent: Int
    ) {
        if (promoPercent == 0) llPromo.gone() else llPromo.visible()
        tvTitlePerNoNight.text = "Price per $numberOfDays night"
        val pricePerNoNight = prize * numberOfDays
        tvPerNoNight.text = "${prize.toString().getPriceFormat()} x $numberOfDays"

        tvTitlePerNoRoom.visibility = if (numberOfRooms == 1) View.GONE else View.VISIBLE
        tvPerNoRoom.visibility = if (numberOfRooms == 1) View.GONE else View.VISIBLE
        tvTitlePerNoRoom.text = "Price per $numberOfRooms room"
        val pricePerNoRoom = pricePerNoNight * numberOfRooms
        tvPerNoRoom.text = "${pricePerNoNight.toString().getPriceFormat()} x $numberOfRooms"
        tvRoomPrice.text = pricePerNoRoom.toString().getPriceFormat()

        tvDiscount.text = "${pricePerNoRoom.toString().getPriceFormat()} x $promoPercent%"
        val pricePromo = pricePerNoRoom * promoPercent / 100.0
        tvPromo.text = "-${pricePromo.toString().getPriceFormat()}"

        val priceTax = (pricePerNoRoom - pricePromo) * 10 / 100.0
        tvTaxPercent.text = "${(pricePerNoRoom - pricePromo).toString().getPriceFormat()} x 10%"
        tvTax.text = priceTax.toString().getPriceFormat()

        tvTotalFee.text = (pricePerNoRoom - pricePromo + priceTax).toString().getPriceFormat()
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        inputFirstName.validateData = {
            it.trim().isNotEmpty()
        }

        inputLastName.validateData = {
            it.trim().isNotEmpty()
        }

        inputPhone.validateData = {
            it.trim().length == 10
        }

        inputEmail.validateData = {
            it.trim().isNotEmpty() && it.isEmailValid()
        }

        inputFirstName.afterTextChange = {
            viewModel.getRoomsReservationBody().firstName = it
            updateNextButtonState()
        }

        inputLastName.afterTextChange = {
            viewModel.getRoomsReservationBody().lastName = it
            updateNextButtonState()
        }

        inputPhone.afterTextChange = {
            //todo
            updateNextButtonState()
        }

        inputEmail.afterTextChange = {
            viewModel.getRoomsReservationBody().email = it
            updateNextButtonState()
        }

        pickerGuestNo.onValueChange = {
            //// TODO: 6/21/20
        }

        pickerRoomNo.onValueChange = {
            numberOfRooms = it
            updateTotalFee(numberOfDays, numberOfRooms, prize, promo?.percentDiscount ?: 0)
        }

        tvBookNow.onClickDelayAction {
            if (promo == null) {
                addNewRoomsReservation(numberOfRooms)
            } else {
                promo?.let {
                    addNewRoomsReservation(numberOfRooms, listOf(it.promoCode))
                }
            }
        }

    }

    private fun updateNextButtonState() {
        tvBookNow.isEnabled = validateData()
    }

    private fun validateData(): Boolean {
        return inputEmail.isValidateDataNotEmpty()
                && inputFirstName.isValidateDataNotEmpty()
                && inputLastName.isValidateDataNotEmpty()
                && inputPhone.isValidateDataNotEmpty()
                && numberOfRooms > 0
    }

    private fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>? = null
    ) {
        addDisposables(
            viewModel.addNewRoomsReservation(numberOfRooms, listPromoCode)
                .observeOnUiThread()
                .subscribe({
                    RxBus.publish(UpdateMyBooking(true))
                    Log.d("--=", "addNewRoomsReservation: ${it}")
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }

    private fun getUserInfo() {
        addDisposables(
            viewModel.getUserInfo()
                .observeOnUiThread()
                .subscribe({
                    it.body.let { user ->
                        inputFirstName.setText(user.firstName)
                        inputLastName.setText(user.lastName)
                        inputEmail.setText(user.email)
                        inputPhone.setText(user.phone)
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }
}
