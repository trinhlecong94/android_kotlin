package com.ctr.hotelreservations.ui.roomdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.data.source.response.PromoResponse
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.util.DateUtil
import kotlinx.android.synthetic.main.layout_item_sale.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/24
 */
class PromoAdapter(private val promos: List<PromoResponse.Promo>) :
    RecyclerView.Adapter<PromoAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: PromoResponse.Promo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_sale, parent, false))
    }

    override fun getItemCount() = promos.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(promos[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(promos[adapterPosition])
            }
        }

        fun onBind(item: PromoResponse.Promo) {
            itemView.apply {
                val startDate = DateUtil.format(
                    DateUtil.parse(
                        item.startDate,
                        DateUtil.FORMAT_DATE_PROMO_SERVER
                    ), DateUtil.FORMAT_DATE_DAY_MONTH
                )
                val endDate = DateUtil.format(
                    DateUtil.parse(item.endDate, DateUtil.FORMAT_DATE_PROMO_SERVER),
                    DateUtil.FORMAT_DATE_DAY_MONTH
                )
                tvContent.text =
                    "Sale off ${item.percentDiscount}% for reservation with check-in date from $startDate to $endDate"
            }
        }
    }
}
