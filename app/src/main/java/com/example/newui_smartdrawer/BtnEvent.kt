package com.example.newui_smartdrawer

import android.util.Log

class BtnEvent {
    private var message: String ?= null
    fun setMsg(msg: String){
        Log.d("event" ,msg)
        message = msg
    }

    fun getMsg(): String {
        return message!!
    }
}