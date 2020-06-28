package com.ctr.hotelreservations.ui.home.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.UserRepository
import com.ctr.hotelreservations.extension.observeOnUiThread
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.extension.showDialog
import com.ctr.hotelreservations.extension.showErrorDialog
import com.ctr.hotelreservations.ui.App
import com.ctr.hotelreservations.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_account.*


/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class AccountFragment : BaseFragment() {
    private lateinit var viewModel: AccountVMContract

    companion object {
        fun newInstance() =
            AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = AccountViewModel(
            App.instance.localRepository,
            UserRepository()
        )
        getUserInfo()

        tvViewProfile.onClickDelayAction {
            Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show()
        }

        llSupport.onClickDelayAction {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:1800 6969")
            startActivity(intent)
        }

        tvLogout.onClickDelayAction {
            activity?.showDialog("Warning", "Do you want to logout?", "OK", {
                App.instance.localRepository.removeToken()
                App.instance.localRepository.removeUserId()
                activity?.let {
                    AuthActivity.start(this, isOpenLogin = true, isShowButtonBack = false)
                    finishAffinity(it)
                }
            })
        }
    }

    private fun getUserInfo() {
        addDisposables(
            viewModel.getUserInfo()
                .observeOnUiThread()
                .subscribe({
                    tvName.text = it.body.firstName + " " + it.body.lastName
                    tvEmail.text = it.body.email
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }
}
