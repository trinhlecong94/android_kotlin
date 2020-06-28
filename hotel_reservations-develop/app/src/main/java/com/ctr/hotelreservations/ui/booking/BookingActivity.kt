package com.ctr.hotelreservations.ui.booking

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseActivity
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.PromoResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import com.ctr.hotelreservations.extension.addFragment
import com.ctr.hotelreservations.ui.home.rooms.RoomFragment
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity

class BookingActivity : BaseActivity() {

    companion object {
        internal const val KEY_PROMO = "key_promos"

        internal fun start(
            from: Fragment,
            brand: HotelResponse.Hotel.Brand,
            roomTypeStatus: RoomTypeResponse.RoomTypeStatus,
            startDate: String,
            endDate: String,
            promo: PromoResponse.Promo?
        ) {
            BookingActivity()
                .apply {
                    val intent = Intent(from.activity, BookingActivity::class.java)
                    intent.putExtras(Bundle().apply {
                        putParcelable(RoomFragment.KEY_BRAND, brand)
                        putParcelable(RoomDetailActivity.KEY_ROOM_TYPE_STATUS, roomTypeStatus)
                        putString(RoomDetailActivity.KEY_START_DATE, startDate)
                        putString(RoomDetailActivity.KEY_END_DATE, endDate)
                        putParcelable(KEY_PROMO, promo)
                    })
                    from.startActivity(intent)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        addFragment(R.id.container, BookingFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): BaseActivity.AppearAnim =
        BaseActivity.AppearAnim.SLIDE_FROM_RIGHT

    internal fun showDatePickerDialog(dialog: DatePickerDialog) {
        dialog.show(fragmentManager, "DatePickerDialogBooking")
    }

    internal fun setOnDateSetListener(dialog: DatePickerDialog.OnDateSetListener) {
        val datePickerDialog =
            fragmentManager?.findFragmentByTag("DatePickerDialogBooking") as? DatePickerDialog
        datePickerDialog?.setOnDateSetListener(dialog)
    }

    internal fun openBookingFragment() {
        addFragment(
            R.id.container,
            BookingFragment.newInstance(),
            addToBackStack = true,
            tag = "BookingFragment.Payment"
        )
    }
}
