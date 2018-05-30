package com.example.newui_smartdrawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_top_false.*

class TopFalseDialog (context: Context) : Dialog(context){

    private var titleStr: String? = null
    private var messageStr: String? = null
    fun setMessage(message: String) {
        messageStr = message
    }

    fun setTitle(title: String) {
        titleStr = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_top_false)

        if (messageStr != null) {
            tv_DtopFalse_message.text = messageStr
        }

        if (titleStr != null) {
            tv_DtopFalse_title.text = titleStr
        }
    }
}
