package com.example.newui_smartdrawer


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.dialog_import.*


class ImportDialog(context: Context) : Dialog(context){
    private var noOnclickListener: onNoOnclickListener? = null//取消按钮被点击了的监听器
    private var yesOnclickListener: onYesOnclickListener? = null//确定按钮被点击了的监听器
    fun onNoOnclickListener(onNoOnclickListener: onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    fun onYesOnclickListener(onYesOnclickListener: onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_import)
        setCanceledOnTouchOutside(false)
        initEvent()
    }

    private fun initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        btn_Dimport_yes.setOnClickListener {
            if (yesOnclickListener != null) {
                yesOnclickListener!!.onYesClick()
            }
        }

        btn_Dimport_no.setOnClickListener {
            if (noOnclickListener != null) {
                noOnclickListener!!.onNoClick()
            }
        }
    }
    /**
     * 设置确定按钮和取消被点击的接口
     */
    interface onYesOnclickListener {
        fun onYesClick()
    }

    interface onNoOnclickListener {
        fun onNoClick()
    }
}
