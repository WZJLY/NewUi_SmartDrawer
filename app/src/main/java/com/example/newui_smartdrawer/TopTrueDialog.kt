package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_top_true.*

class TopTrueDialog : AppCompatActivity() {
    private var messageStr: String? = null
    fun setMessage(message: String) {
        messageStr = message
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_top_true)

        if (messageStr != null) {
            tv_DtopTrue_message.text = messageStr
        }
    }
}
