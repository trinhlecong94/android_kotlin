package com.ctr.hotelreservations.ui.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.bus.RxBus
import com.ctr.hotelreservations.data.model.BookingStatus
import com.ctr.hotelreservations.data.model.UpdateMyBooking
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.UserRepository
import com.ctr.hotelreservations.data.source.response.MyBookingResponse
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.App
import com.ctr.hotelreservations.util.DateUtil
import com.ctr.hotelreservations.util.compareDay
import com.ctr.hotelreservations.util.parseToCalendar
import com.ctr.hotelreservations.util.parseToString
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.layout_payment.*
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/06/27
 */
@SuppressLint("SetTextI18n")
class PaymentFragment : BaseFragment() {
    private lateinit var viewModel: BookingViewModel
    private lateinit var adapterBookingStep: BookingStepAdapter
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var promoPercent = 0
    private var numberOfRooms = 1
    private var numberOfDays = 1
    private var prize = 0.0
    private var totalFree = 0.0
    private var status: BookingStatus = BookingStatus.PENDING
    private var reservationId = 0
    private var roomReservationId = 0
    private var bookingSteps = mutableListOf(
        StepBookingUI("Booking"),
        StepBookingUI("Payment"),
        StepBookingUI("Check in"),
        StepBookingUI("Review")
    )

    companion object {
        internal const val KEY_MY_BOOKING = "key_my_booking"

        fun newInstance(myBooking: MyBookingResponse.MyBooking) = PaymentFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_MY_BOOKING, myBooking)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = BookingViewModel(
            App.instance.localRepository, HotelRepository(), UserRepository()
        )
        initRecyclerView()
        initView()
        initListener()
    }

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        rcvStepBooking.apply {
            PagerSnapHelper().attachToRecyclerView(this)
            adapterBookingStep = BookingStepAdapter(
                bookingSteps
            ).apply { setSelectedPosition(1) }
            setHasFixedSize(true)
            adapter = adapterBookingStep
            scrollToPosition(1)
        }
    }

    private fun initView() {
        arguments?.getParcelable<MyBookingResponse.MyBooking>(KEY_MY_BOOKING)?.let {
            when (it.status) {
                BookingStatus.PENDING.name -> {
                    when (it.reservation.status) {
                        "UNPAID" -> {
                            status = BookingStatus.UNPAID
                            tvPayNow.text = "Pay Now"
                            tvBookAlertTitle.text = "Still Awaiting Your Payment"
                            tvBookAlert.text = "Please complete you payment."
                        }
                        "PAID" -> {
                            status = BookingStatus.PAID
                            tvPayNow.text = "Cancel Booking"
                            tvBookAlertTitle.text = "Wait for you check in"
                            tvBookAlert.text =
                                "Please check in before 14:00 ${it.startDate?.parseToCalendar(
                                    DateUtil.FORMAT_DATE_TIME_FROM_API_3
                                )?.parseToString(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)}"
                            adapterBookingStep.setSelectedPosition(2)
                            rcvStepBooking.scrollToPosition(2)
                        }
                    }
                }
                BookingStatus.COMPLETED.name -> {
                    status = BookingStatus.COMPLETED
                    tvPayNow.text = "Book Again"
                    tvBookAlertTitle.text = "You booking has been completed"
                    tvBookAlert.text = "Please review this room."
                    adapterBookingStep.setSelectedPosition(3)
                    rcvStepBooking.scrollToPosition(3)
                }
                else -> {
                    status = BookingStatus.CANCELLED
                    tvPayNow.text = "Book Again"
                    tvBookAlertTitle.text = "You booking has been canceled"
                    tvBookAlert.text =
                        "Sorry, the booking has been cancelled. Please book again or book another room."
                    adapterBookingStep.setSelectedPosition(2)
                    bookingSteps.apply {
                        clear()
                        addAll(
                            listOf(
                                StepBookingUI("Booking"),
                                StepBookingUI("Payment"),
                                StepBookingUI("Cancelled")
                            )
                        )
                    }
                    rcvStepBooking.adapter?.notifyDataSetChanged()
                    rcvStepBooking.scrollToPosition(2)
                }
            }

            tvTitle.text = it.room?.brand?.name.toString()
            tvBookingId.text = "Booking ID: ${it.id}"
            tvCustomer.apply {
                visible()
                text = "${it.reservation.user?.firstName} ${it.reservation.user?.lastName} "
            }
            context?.let { context ->
                Glide.with(context).load(it.room?.roomType?.thumbnail).into(ivRoomThumb)
            }

            tvRoomTitle.text = it.room?.roomType?.name
            tvRoomInfo.text = it.room?.roomType?.getRoomTypeInfo()

            tvRoomAddress.text = it.room?.brand?.address
            reservationId = it.reservation.id
            roomReservationId = it.id
            startDate = it.startDate?.parseToCalendar(DateUtil.FORMAT_DATE_TIME_FROM_API_3)
            endDate = it.endDate?.parseToCalendar(DateUtil.FORMAT_DATE_TIME_FROM_API_3)
            numberOfDays = startDate.compareDay(endDate)
            prize = it.room?.roomType?.price?.toDouble() ?: 0.0

            updateUI(startDate, endDate, numberOfDays, numberOfRooms, prize)
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

        updateTotalFee(numberOfDays, numberOfRooms, prize, promoPercent)
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

        totalFree = pricePerNoRoom - pricePromo + priceTax
        tvTotalFee.text = totalFree.toString().getPriceFormat()
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvPayNow.onClickDelayAction {
            when (status) {
                BookingStatus.UNPAID, BookingStatus.PENDING -> {
                    activity?.showDialog(
                        "Payment Method",
                        "Do you want pay ${totalFree.toString().getPriceFormat()} now?",
                        "Yes",
                        {
                            changeReservationStatus()
                        }, "No"
                    )
                }
                BookingStatus.PAID -> {
                    activity?.showDialog(
                        "Warning!",
                        "Do you want cancel booking?",
                        "Yes",
                        {
                            changeRoomReservationStatus()
                        }, "No"
                    )
                }
                else -> {
                }
            }

        }

        tvContactUs.onClickDelayAction {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:1800 6969")
            startActivity(intent)
        }
    }

    private fun changeReservationStatus() {
        addDisposables(
            viewModel.changeReservationStatus(reservationId)
                .observeOnUiThread()
                .subscribe({
                    if (it.body.status == BookingStatus.PAID.name) {
                        status = BookingStatus.PAID
                        tvPayNow.text = "Cancer Booking"
                        tvBookAlertTitle.text = "Wait for you check in"
                        tvBookAlert.text =
                            "Please check in before 14:00 ${startDate?.parseToString(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)}"
                        adapterBookingStep.setSelectedPosition(2)
                        rcvStepBooking.scrollToPosition(2)
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        RxBus.publish(UpdateMyBooking(true))
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }

    private fun changeRoomReservationStatus() {
        addDisposables(
            viewModel.changeRoomReservationStatus(roomReservationId)
                .observeOnUiThread()
                .subscribe({
                    if (it.body.status == BookingStatus.CANCELLED.name) {
                        status = BookingStatus.CANCELLED
                        tvPayNow.text = "Book Again"
                        tvBookAlertTitle.text = "You booking has been canceled"
                        tvBookAlert.text =
                            "Sorry, the booking has been cancelled. Please book again or book another room."
                        adapterBookingStep.setSelectedPosition(2)
                        bookingSteps.apply {
                            clear()
                            addAll(
                                listOf(
                                    StepBookingUI("Booking"),
                                    StepBookingUI("Payment"),
                                    StepBookingUI("Cancelled")
                                )
                            )
                        }
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        rcvStepBooking.scrollToPosition(2)
                        RxBus.publish(UpdateMyBooking(true))
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }

    override fun getProgressBarControlObservable() =
        viewModel.progressBarDialogStateObservable
}


