package com.ctr.hotelreservations.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseActivity
import com.ctr.hotelreservations.extension.addFragment

class AuthActivity : BaseActivity() {

    companion object {
        internal const val KEY_OPEN_LOGIN = "key_open_login"
        internal const val KEY_SHOW_BUTTON_BACK = "key_show_button_back"
        internal const val KEY_EMAIL = "key_email"
        internal fun start(
            from: Fragment,
            isOpenLogin: Boolean = true,
            isShowButtonBack: Boolean = true,
            email: String? = null
        ) {
            AuthActivity()
                .apply {
                    val intent = Intent(from.activity, AuthActivity::class.java)
                    intent.putExtras(Bundle().apply {
                        putBoolean(KEY_OPEN_LOGIN, isOpenLogin)
                        putBoolean(KEY_SHOW_BUTTON_BACK, isShowButtonBack)
                        putString(KEY_EMAIL, email)
                    })
                    from.startActivity(intent)
                }
        }

        internal fun start(
            from: Activity,
            isOpenLogin: Boolean = true,
            isShowButtonBack: Boolean = true,
            email: String? = null
        ) {
            AuthActivity()
                .apply {
                    val intent = Intent(from, AuthActivity::class.java)
                    intent.putExtras(Bundle().apply {
                        putBoolean(KEY_OPEN_LOGIN, isOpenLogin)
                        putBoolean(KEY_SHOW_BUTTON_BACK, isShowButtonBack)
                        putString(KEY_EMAIL, email)
                    })
                    from.startActivity(intent)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if (intent.getBooleanExtra(KEY_OPEN_LOGIN, true)) {
            addFragment(getContainerId(), LoginFragment.newInstance())
        } else {
            addFragment(getContainerId(), RegisterFragment.newInstance())
        }
    }

    override fun getContainerId() = R.id.container

    internal fun openRegisterFragment() {
        addFragment(getContainerId(), RegisterFragment.newInstance(), addToBackStack = true)
    }
}
