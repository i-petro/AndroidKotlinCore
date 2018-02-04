package com.androidkotlincore.sample.presentation.screen.login

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by Peter on 04.02.2018.
 */
class LoginFragment : BaseFragment<LoginView, LoginPresenter>(), LoginView {
    override val layoutId: Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnGoogleSignIn.setOnClickListener { presenter.signInByGoogle() }
        btnGoogleLogout.setOnClickListener { presenter.logout() }
    }

    override fun showMessage(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object Factory {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }
}