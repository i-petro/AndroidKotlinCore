package com.androidkotlincore.sampleconductor.demo

import android.content.Context
import android.os.Bundle
import android.view.View
import com.androidkotlincore.mvp.impl.BaseMVPController
import com.androidkotlincore.sampleconductor.App
import com.androidkotlincore.sampleconductor.R
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import kotlinx.android.synthetic.main.controller_navigation_demo.view.*

/**
 * Created by Peter on 19.02.2018.
 */
class DemoController(args: Bundle) : BaseMVPController<DemoView, DemoPresenter>(args), DemoView {
    override val layoutId: Int = R.layout.controller_navigation_demo
    override val contextNotNull: Context get() = App.INSTANCE //TODO: move to BaseController

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        val number = args.getInt(EXTRA_NUMBER, -1)

        view.tv_title.text = number.toString()
        view.btn_next.setOnClickListener {
            router.pushController(RouterTransaction.with(DemoController.newInstance(number + 1))
                    .pushChangeHandler(HorizontalChangeHandler())
                    .popChangeHandler(HorizontalChangeHandler()))
        }
        view.btn_up.setOnClickListener {
            router.activity?.onBackPressed()
        }
        view.btn_pop_to_root.setOnClickListener {
            router.popToRoot()
        }
    }

    companion object {
        private const val EXTRA_NUMBER = "EXTRA_NUMBER"

        fun newInstance() = DemoController(Bundle())
        fun newInstance(number: Int): DemoController {
            val args = Bundle()
            args.putInt(EXTRA_NUMBER, number)
            return DemoController(args)
        }
    }
}