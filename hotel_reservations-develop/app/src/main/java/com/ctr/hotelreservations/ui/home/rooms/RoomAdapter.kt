package com.ctr.hotelreservations.ui.home.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import com.ctr.hotelreservations.extension.getPriceFormat
import com.ctr.hotelreservations.extension.onClickDelayAction
import kotlinx.android.synthetic.main.layout_item_room_of_brand.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class RoomAdapter(
    private val rooms: List<RoomTypeResponse.RoomTypeStatus>,
    private val brand: HotelResponse.Hotel.Brand
) :
    RecyclerView.Adapter<RoomAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: RoomTypeResponse.RoomTypeStatus) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_room_of_brand, parent, false))
    }

    override fun getItemCount() = rooms.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(rooms[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(rooms[adapterPosition])
            }
        }

        fun onBind(item: RoomTypeResponse.RoomTypeStatus) {
            itemView.apply {
                tvName.text = item.roomType.name
                tvAddress.text = brand.address
                tvRoomInfo.text =
                    item.roomType.getRoomTypeInfo()
                Glide.with(itemView.context)
                    .load(item.roomType.thumbnail)
                    .into(ivContent)
                tvPrice.text = item.roomType.price.toString().getPriceFormat()
                tvAvailable.text = "Available: ${item.totalRoomAvailable}"
            }
        }
    }
}
