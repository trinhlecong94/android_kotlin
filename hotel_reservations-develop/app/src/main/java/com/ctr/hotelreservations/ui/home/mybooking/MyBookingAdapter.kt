package com.ctr.hotelreservations.ui.home.mybooking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.data.model.BookingStatus
import com.ctr.hotelreservations.data.source.response.MyBookingResponse
import com.ctr.hotelreservations.extension.getPriceFormat
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.util.DateUtil
import com.ctr.hotelreservations.util.parseToCalendar
import com.ctr.hotelreservations.util.parseToString
import kotlinx.android.synthetic.main.layout_item_my_booking.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/24
 */
@SuppressLint("SetTextI18n")
class MyBookingAdapter(private val myBookings: List<MyBookingResponse.MyBooking>) :
    RecyclerView.Adapter<MyBookingAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: MyBookingResponse.MyBooking) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_my_booking, parent, false))
    }

    override fun getItemCount() = myBookings.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(myBookings[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(myBookings[adapterPosition])
            }
        }

        fun onBind(item: MyBookingResponse.MyBooking) {
            itemView.apply {
                Glide.with(itemView.context).load(item.room?.roomType?.thumbnail).into(ivRoomThumb)
                tvBookingId.text = "Booking ID: ${item.id}"
                tvTotalPrize.text = item.reservation.totalBeforeTax.toString().getPriceFormat()
                item.reservation.promos?.firstOrNull()?.let {
                    tvTotalPrize.text =
                        (item.reservation.totalBeforeTax * (100 - it.percentDiscount) / 100.0).toString()
                            .getPriceFormat()
                }
                tvRoomType.text = item.room?.roomType?.name
                tvBrand.text = item.room?.brand?.name
                tvAddress.text = item.room?.brand?.address
                tvCheckInDay.text =
                    "Checkin: " + item.startDate?.parseToCalendar(DateUtil.FORMAT_DATE_TIME_FROM_API_3)
                        ?.parseToString("dd.MM.yyyy")
                tvBookingStatus.text = item.status
                tvBookingStatus.setTextColor(
                    when (item.status) {
                        BookingStatus.PENDING.name -> {
                            when (item.reservation.status) {
                                BookingStatus.PAID.name -> {
                                    tvBookingStatus.text = "CHECK IN"
                                    resources.getColor(R.color.colorAccent)
                                }
                                else -> {
                                    tvBookingStatus.text = "PENDING"
                                    resources.getColor(R.color.colorAccent)
                                }
                            }
                        }
                        BookingStatus.COMPLETED.name -> {
                            resources.getColor(R.color.booking_status_complete)
                        }
                        else -> {
                            resources.getColor(R.color.booking_status_incomplete)
                        }
                    }
                )
            }
        }
    }
}
