package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Gravity
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
        ib_management_back.setOnClickListener{
            finish()
            overridePendingTransition(0,0)
        }
        ib_management_add.setOnClickListener{
            val dialog = UserDialog(this)
            dialog.setYesOnclickListener("保存", object : UserDialog.onYesOnclickListener {
                override fun onYesClick() {
                    val etName = dialog.findViewById(R.id.et_Duser_account) as EditText
                    val etNum = dialog.findViewById(R.id.et_Duser_num) as EditText
                    val etAccount = dialog.findViewById(R.id.et_Duser_name) as EditText
                    val etPassword = dialog.findViewById(R.id.et_Duser_password) as EditText
                    val etPassword2 = dialog.findViewById(R.id.et_Duser_password2) as EditText
                    val etPhone = dialog.findViewById(R.id.et_Duser_phone) as EditText
                    val rgDuserLevel = dialog.findViewById(R.id.rg_Duser_level) as RadioGroup
                    val selectId = dialog.findViewById(rgDuserLevel.checkedRadioButtonId) as RadioButton
                    if (etName.length() == 0) {
                        Toast.makeText(this@ManagementActivity, "账号未填写", Toast.LENGTH_SHORT).show()
                    } else if (etPassword.length() == 0) {
                        Toast.makeText(this@ManagementActivity, "密码未填写", Toast.LENGTH_SHORT).show()
                    } else if (etPassword.length() != 0 && etPassword.text.toString() != etPassword2.text.toString()){
                        Toast.makeText(this@ManagementActivity, "两次密码输入不同", Toast.LENGTH_SHORT).show()
                    } else if (etAccount.length() == 0) {
                        Toast.makeText(this@ManagementActivity, "姓名未填写", Toast.LENGTH_SHORT).show()
                    } else if (etName.length() != 0 && etAccount.length() != 0 && etPassword.length() != 0 && etPassword.length() == etPassword2.length()) {
                        if (selectId.text == "管理员") {
                            userAccount = UserAccount(etNum.text.toString(), etName.text.toString(), etPassword.text.toString(),
                                    0, etAccount.text.toString(), etPhone.text.toString(), "0")
                        } else if (selectId.text == "普通用户") {
                            userAccount = UserAccount(etNum.text.toString(), etName.text.toString(), etPassword.text.toString(),
                                    1, etAccount.text.toString(), etPhone.text.toString(), "0")
                        }
                        dbManager?.addAccount(userAccount)
                        val managementFragment = ManagementFragment()
                        replaceFragment(R.id.fl_management, managementFragment)
                        dialog.dismiss()
                    }
                }

            })
            dialog.setNoOnclickListener("取消", object : UserDialog.onNoOnclickListener {
                override fun onNoClick() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.window.setGravity(Gravity.CENTER)
        }
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
