package com.ctr.hotelreservations.ui.onboarding

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.extension.onClickDelayAction
import kotlinx.android.synthetic.main.item_on_boarding_slider.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class SliderAdapter(private val photos: List<Drawable>) :
    RecyclerView.Adapter<SliderAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: Drawable) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.item_on_boarding_slider, parent, false))
    }

    override fun getItemCount() = photos.size * 2 + 1

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(photos[position % photos.size])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(photos[adapterPosition])
            }
        }

        fun onBind(item: Drawable) {
            itemView.apply {
                imageViewPhoto.setImageDrawable(item)
            }
        }
    }
}