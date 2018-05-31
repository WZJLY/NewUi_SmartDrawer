package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_management.*
import kotlinx.android.synthetic.main.dialog_user.*

class ManagementActivity : AppCompatActivity(),UserLineFragment.deletbuttonlisten {
    private var dbManager:DBManager?=null
    private var userAccount: UserAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)
        val managementFragment = ManagementFragment()
        replaceFragment(R.id.fl_management, managementFragment)
        dbManager = DBManager(this)
        ib_management_back.setOnClickListener({
            finish()
        })
        ib_management_add.setOnClickListener({
            val dialog = UserDialog(this)
            dialog.show()
            dialog.setYesOnclickListener("保存", object : UserDialog.onYesOnclickListener {
                override fun onYesClick() {
                    var et_name = dialog.findViewById(R.id.et_Duser_account) as EditText
                    var et_num = dialog.findViewById(R.id.et_Duser_num) as EditText
                    var et_account = dialog.findViewById(R.id.et_Duser_name) as EditText
                    var et_password = dialog.findViewById(R.id.et_Duser_password) as EditText
                    var et_password2 = dialog.findViewById(R.id.et_Duser_password2) as EditText
                    var et_phone = dialog.findViewById(R.id.et_Duser_phone) as EditText
                    var rg_Duser_level = dialog.findViewById(R.id.rg_Duser_level) as RadioGroup
                    var selectId = dialog.findViewById(rg_Duser_level.checkedRadioButtonId) as RadioButton
                    if (et_name.length() == 0) {
                        Toast.makeText(this@ManagementActivity, "账号未填写", Toast.LENGTH_SHORT).show()
                    } else if (et_password.length() == 0) {
                        Toast.makeText(this@ManagementActivity, "密码未填写", Toast.LENGTH_SHORT).show()
                    } else if (et_password.length() != 0 && et_password.text.toString() != et_password2.text.toString()) {
                        Toast.makeText(this@ManagementActivity, "两次密码输入不同", Toast.LENGTH_SHORT).show()
                    } else if (et_account.length() == 0) {
                        Toast.makeText(this@ManagementActivity, "姓名未填写", Toast.LENGTH_SHORT).show()
                    } else if (et_name.length() != 0 && et_account.length() != 0 && et_password.length() != 0 && et_password.length() == et_password2.length()) {
                        if (selectId.text == "管理员") {
                            userAccount = UserAccount(et_num.text.toString(), et_name.text.toString(), et_password.text.toString(),
                                    0, et_account.text.toString(), et_phone.text.toString(), "0")

                        } else if (selectId.text == "普通用户") {

                            userAccount = UserAccount(et_num.text.toString(), et_name.text.toString(), et_password.text.toString(),
                                    1, et_account.text.toString(), et_phone.text.toString(), "0")

                        }
                        dbManager?.addAccount(userAccount)
                        dialog.dismiss()
                    }
                }

            })
            dialog.setNoOnclickListener("取消", object : UserDialog.onNoOnclickListener {
                override fun onNoClick() {

                    dialog.dismiss()

                }

            })
        })
    }
    override fun deletButtonClick(text: String) {
        if(text == "deletperson") {
            val managementFragment = ManagementFragment()
            replaceFragment(R.id.fl_management, managementFragment)
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }


}
