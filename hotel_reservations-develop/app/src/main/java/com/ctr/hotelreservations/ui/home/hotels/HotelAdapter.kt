package com.ctr.hotelreservations.ui.home.hotels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.extension.onClickDelayAction
import kotlinx.android.synthetic.main.layout_item_hotels.view.*
import kotlinx.android.synthetic.main.layout_item_room_of_brand.view.tvName

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class HotelAdapter(private val hotels: List<HotelResponse.Hotel>) :
    RecyclerView.Adapter<HotelAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: HotelResponse.Hotel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_hotels, parent, false))
    }

    override fun getItemCount() = hotels.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(hotels[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(hotels[adapterPosition])
            }
        }

        fun onBind(item: HotelResponse.Hotel) {
            itemView.apply {
                tvName.text = item.name
                tvDescription.text = item.description
                Glide.with(itemView.context)
                    .load(item.images.firstOrNull()?.name)
                    .into(itemView.ivContent)


            }
        }
    }
}
