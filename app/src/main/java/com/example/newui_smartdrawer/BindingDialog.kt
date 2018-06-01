package com.example.newui_smartdrawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.dialog_binding.*

class BindingDialog(context: Context) : Dialog(context) {
    private var numberStr: String? = null//从外界设置的title文本
    private var serviceCodeStr: String? = null//从外界设置的消息文本
    private var yesOnclickListener: onYesOnclickListener? = null//确定按钮被点击了的监听器
    private var noOnclickListener: onNoOnclickListener? = null//确定按钮被点击了的监听器

    fun setYesOnclickListener(onYesOnclickListener: onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener
    }

    fun setNoOnclickListener(onNoOnclickListener: onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_binding)
        tv_Dbinding_number.text = ""
        tv_Dbinding_serviceCode.text = ""
        setCanceledOnTouchOutside(false)
        initData()
        btn_Dbinding_yes.setOnClickListener {
            yesOnclickListener!!.onYesClick()
        }

        btn_Dbinding_no.setOnClickListener {
            noOnclickListener!!.onNoClick()
        }
    }

    private fun initData() {
        if (numberStr != null) {
            et_Dbinding_number.setText(numberStr)
            btn_Dbinding_yes.text = "解除绑定"
            et_Dbinding_number.isEnabled = false
            et_Dbinding_serviceCode.isEnabled = false
            if (serviceCodeStr != null)
                et_Dbinding_serviceCode.setText(serviceCodeStr)
        }
    }
    fun setBinding(number: String,serviceCode: String) {
        numberStr = number
        serviceCodeStr = serviceCode
    }

    fun changeBtn() {
        et_Dbinding_number.setText("")
        et_Dbinding_serviceCode.setText("")
        btn_Dbinding_yes.text = "绑定"
        et_Dbinding_number.isEnabled = true
        et_Dbinding_serviceCode.isEnabled = true
    }
    interface onYesOnclickListener {
        fun onYesClick()
    }
    interface onNoOnclickListener {
        fun onNoClick()
    }
}
