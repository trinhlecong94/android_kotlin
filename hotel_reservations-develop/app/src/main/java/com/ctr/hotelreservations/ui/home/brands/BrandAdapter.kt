package com.ctr.hotelreservations.ui.home.brands

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.extension.visible
import kotlinx.android.synthetic.main.layout_item_hotels.view.*
import kotlinx.android.synthetic.main.layout_item_room_of_brand.view.tvName

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class BrandAdapter(private val brands: List<HotelResponse.Hotel.Brand>) :
    RecyclerView.Adapter<BrandAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: HotelResponse.Hotel.Brand) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_hotels, parent, false))
    }

    override fun getItemCount() = brands.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(brands[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(brands[adapterPosition])
            }
        }

        fun onBind(item: HotelResponse.Hotel.Brand) {
            itemView.apply {
                tvAddress.visible()
                tvName.text = item.name
                tvAddress.text = item.address
                tvDescription.text = item.desciption
                Glide.with(itemView.context)
                    .load(item.imgLink)
                    .into(itemView.ivContent)
            }
        }
    }
}
