package com.example.newui_smartdrawer

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_bottom.*

class BottomDialog(context: Context) : Dialog(context){
    private var yesStr: String? = null
    private var noStr: String? = null
    private var messageStr: String? = null//从外界设置的消息文本

    private var noOnclickListener: onNoOnclickListener? = null//取消按钮被点击了的监听器
    private var yesOnclickListener: onYesOnclickListener? = null//确定按钮被点击了的监听器

    fun setNoOnclickListener(str: String?, onNoOnclickListener: onNoOnclickListener) {
        if (str != null) {
            noStr = str
        }
        this.noOnclickListener = onNoOnclickListener
    }

    fun setYesOnclickListener(str: String?, onYesOnclickListener: onYesOnclickListener) {
        if (str != null) {
            yesStr = str
        }
        this.yesOnclickListener = onYesOnclickListener
    }

    fun setMessage(message: String) {
        messageStr = message
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_bottom)
        initData()

        btn_Dbottom_yes.setOnClickListener {
            if (yesOnclickListener != null) {
                yesOnclickListener!!.onYesClick()
            }
        }

        btn_Dbottom_no.setOnClickListener {
            if (noOnclickListener != null) {
                noOnclickListener!!.onNoClick()
            }
        }
    }

    private fun initData() {
        if (messageStr != null) {
            tv_Dbottom_message.text = messageStr
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            btn_Dbottom_yes.text = yesStr
        }
        if (noStr != null) {
            btn_Dbottom_no.text = noStr
        }
    }

    interface onYesOnclickListener {
        fun onYesClick()
    }

    interface onNoOnclickListener {
        fun onNoClick()
    }
}
