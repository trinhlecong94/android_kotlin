package com.ctr.hotelreservations.ui.roomdetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseActivity
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import com.ctr.hotelreservations.extension.addFragment
import com.ctr.hotelreservations.ui.home.rooms.RoomFragment.Companion.KEY_BRAND

/**
 * Created by at-trinhnguyen2 on 2020/06/13
 */
class RoomDetailActivity : BaseActivity() {

    companion object {
        internal const val KEY_ROOM_TYPE_STATUS = "key_room_type_status"
        internal const val KEY_START_DATE = "key_start_date"
        internal const val KEY_END_DATE = "key_end_date"

        internal fun start(
            from: Fragment,
            brand: HotelResponse.Hotel.Brand,
            roomTypeStatus: RoomTypeResponse.RoomTypeStatus,
            startDate: String,
            endDate: String
        ) {
            RoomDetailActivity()
                .apply {
                    val intent = Intent(from.activity, RoomDetailActivity::class.java)
                    intent.putExtras(Bundle().apply {
                        putParcelable(KEY_BRAND, brand)
                        putParcelable(KEY_ROOM_TYPE_STATUS, roomTypeStatus)
                        putString(KEY_START_DATE, startDate)
                        putString(KEY_END_DATE, endDate)
                    })
                    from.startActivity(intent)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)
        addFragment(getContainerId(), RoomDetailFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): AppearAnim = AppearAnim.SLIDE_FROM_RIGHT
}
