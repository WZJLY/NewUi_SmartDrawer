package com.example.newui_smartdrawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle

class UserDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_user)
        setCanceledOnTouchOutside(false)
    }
}
