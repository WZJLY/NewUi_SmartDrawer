package com.example.newui_smartdrawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_binding.*




class BindingDialog(context: Context) : Dialog(context) {

    private var yesOnclickListener: onYesOnclickListener? = null//确定按钮被点击了的监听器

    fun setYesOnclickListener(onYesOnclickListener: onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_binding)
        tv_Dbinding_number.text = ""
        tv_Dbinding_serviceCode.text = ""
        setCanceledOnTouchOutside(false)

        btn_Dbinding_yes.setOnClickListener {
            yesOnclickListener!!.onYesClick()
        }
    }
    interface onYesOnclickListener {
        fun onYesClick()
    }
}
