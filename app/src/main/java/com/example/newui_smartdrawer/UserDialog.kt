package com.example.newui_smartdrawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.dialog_user.*

class UserDialog(context: Context) : Dialog(context) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }
    private var yesStr: String? = null
    private var noStr: String? = null
    private var accountStr: String? = null
    private var passwordStr: String? = null
    private var password2Str: String? = null
    private var nameStr: String? = null
    private var levelStr: String? = null

    private var phoneStr: String? = null
    private var numStr: String? = null
    private var stateInt: Int = 0
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

    fun setEdit(userAccount: UserAccount,state :Int) {
        accountStr = userAccount.userName
        passwordStr = userAccount.userPassword
        password2Str = userAccount.userPassword
        nameStr = userAccount.userAccount
        phoneStr = userAccount.phoneNumber
        numStr = userAccount.userId
        if(userAccount.userPower==0)
            levelStr = "ADMIN"
        else
            levelStr = "NORMAL"
        stateInt = state
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_user)

        setCanceledOnTouchOutside(false)
        initData()

        btn_Duser_yes.setOnClickListener {
            if (yesOnclickListener != null) {
                yesOnclickListener!!.onYesClick()
            }
        }

        btn_Duser_no.setOnClickListener {
            if (noOnclickListener != null) {
                noOnclickListener!!.onNoClick()
            }
        }
    }

    private fun initData() {
        if (accountStr != null) {
            tv_Duser_title.text = "个人信息修改"
            et_Duser_account.setText(accountStr)
            et_Duser_account.isEnabled = false
            et_Duser_password.setText(passwordStr)
            et_Duser_password2.setText(password2Str)
            if (levelStr == "ADMIN"){
                rg_Duser_level.check(R.id.rb_Duser_admin)
            }
            else if (levelStr == "NORMAL"){
                rg_Duser_level.check(R.id.rb_Duser_normal)
            }
            if (stateInt > 0) {
                rb_Duser_admin.isEnabled = false
                rb_Duser_normal.isEnabled = false
                rg_Duser_level.setBackgroundResource(R.drawable.bg_edit_false)
            }
        }
        if (nameStr != null) {
            et_Duser_name.setText(nameStr)
        }
        if (phoneStr != null) {
            et_Duser_phone.setText(phoneStr)
        }
        if (numStr != null) {
            et_Duser_num.setText(numStr)
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            btn_Duser_yes.text = yesStr
        }
        if (noStr != null) {
            btn_Duser_no.text = noStr
        }
    }

    interface onYesOnclickListener {
        fun onYesClick()
    }

    interface onNoOnclickListener {
        fun onNoClick()
    }
}
