package com.ctr.hotelreservations.ui.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.extension.invisible
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.extension.visible
import kotlinx.android.synthetic.main.layout_item_booking_step.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
class BookingStepAdapter(private val stepBookings: List<StepBookingUI>) :
    RecyclerView.Adapter<BookingStepAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: StepBookingUI) -> Unit)? = null
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_booking_step, parent, false))
    }

    override fun getItemCount() = stepBookings.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(stepBookings[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(stepBookings[adapterPosition])
            }
        }

        fun onBind(item: StepBookingUI) {
            itemView.apply {
                tvStepNumber.text = (adapterPosition + 1).toString()
                tvTitle.text = item.title
                if (adapterPosition + 1 == stepBookings.size) {
                    viewDivider.invisible()
                }

                updateUI(adapterPosition)
                when {
                    adapterPosition > selectedPosition -> {
                        ivStatusStepDone.invisible()
                        tvStepNumber.visible()
                        tvStepNumber.setBackgroundResource(R.drawable.bg_circle_gray_light)
                        tvTitle.setTextColor(resources.getColor(R.color.gray_light))
                    }
                    adapterPosition == selectedPosition -> {
                        ivStatusStepDone.invisible()
                        tvStepNumber.visible()
                        tvStepNumber.setBackgroundResource(R.drawable.bg_circle_accent)
                        tvTitle.setTextColor(resources.getColor(R.color.colorAccent))
                    }
                    else -> {
                        ivStatusStepDone.visible()
                        tvStepNumber.invisible()
                        tvStepNumber.setBackgroundResource(R.drawable.bg_circle_accent)
                        tvTitle.setTextColor(resources.getColor(R.color.colorAccent))
                    }
                }
            }
        }
    }

    private fun updateUI(adapterPosition: Int) {

    }

    internal fun setSelectedPosition(value: Int) {
        selectedPosition = value

    }
}
