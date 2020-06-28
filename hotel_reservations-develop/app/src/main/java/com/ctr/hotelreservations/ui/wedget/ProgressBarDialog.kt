package com.ctr.hotelreservations.ui.wedget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.ctr.hotelreservations.R

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class ProgressBarDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        setContentView(R.layout.dialog_progress_bar)
    }
}
