package com.example.newui_smartdrawer


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SC_Const
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.fragment_user_line.*


class UserLineFragment : Fragment() {
    var activityCallback:UserLineFragment.deletbuttonlisten? = null
    var userName:String?=null
    var dbManager:DBManager?=null
    var user : UserAccount?=null
    var statue = String()
    private var scApp: SCApp? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_line, container, false)

    }
    interface deletbuttonlisten {
        fun deletButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as deletbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun deletbuttonClicked(text: String) {
        activityCallback?.deletButtonClick(text)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context.applicationContext)
        scApp = context.applicationContext as SCApp
        if(arguments!=null)
        {
                userName = arguments.getString("userName")
        }
        user = dbManager?.getUserAccountByUserName(userName)
        tv_FuserLine_Name.text=userName
        val account= scApp?.userInfo
        if (user?.statue == "1")
            btn_FuserLine_del.text="启用"
//        if(user?.userPower==0)
//            btn_FuserLine_del.visibility = View.GONE
//        if(userName=="admin") {
//            btn_FuserLine_del.visibility = View.GONE   //对可删除的用户就行了判断
//            // 管理级别为最高，接下去普通管理员，然后是普通用户
//            //同级之间无法删除，只能上级删除下级
//        }
//        else{
//            if(account?.userPower==0&&account.userName!="admin") {
//                btn_FuserLine_del.visibility = View.GONE
//            }
        if(userName=="admin")
            btn_FuserLine_del.visibility = View.GONE   //对可删除的用户就行了判断
        if(account?.userName!="admin"&&user?.userPower==0)
            btn_FuserLine_del.visibility = View.GONE
        if(user?.userPower==SC_Const.ADMIN)
            tv_FuserLine_user.text="管理员"

        ib_FuserLine_user.setOnClickListener({
            if(account?.userName=="admin")
            {
                val dialog = UserDialog(context)
                dialog.setEdit(dbManager!!.getUserAccountByUserName(tv_FuserLine_Name.text.toString()),0)
                dialog.setYesOnclickListener("修改", object : UserDialog.onYesOnclickListener {
                    override fun onYesClick() {
                        val et_name = dialog.findViewById(R.id.et_Duser_account) as EditText
                        val et_num = dialog.findViewById(R.id.et_Duser_num) as EditText
                        val et_account = dialog.findViewById(R.id.et_Duser_name) as EditText
                        val et_password = dialog.findViewById(R.id.et_Duser_password) as EditText
                        val et_password2 = dialog.findViewById(R.id.et_Duser_password2) as EditText
                        val et_phone = dialog.findViewById(R.id.et_Duser_phone) as EditText
                        val rg_Duser_level = dialog.findViewById(R.id.rg_Duser_level) as RadioGroup
                        val selectId = dialog.findViewById(rg_Duser_level.checkedRadioButtonId) as RadioButton
                        if (et_name.length() == 0) {
                            Toast.makeText(context, "账号未填写", Toast.LENGTH_SHORT).show()
                        } else if (et_password.length() == 0) {
                            Toast.makeText(context, "密码未填写", Toast.LENGTH_SHORT).show()
                        } else if (et_password.length() != 0 && et_password.text.toString() != et_password2.text.toString()) {
                            Toast.makeText(context, "两次密码输入不同", Toast.LENGTH_SHORT).show()
                        } else if (et_account.length() == 0) {
                            Toast.makeText(context, "姓名未填写", Toast.LENGTH_SHORT).show()
                        } else if (et_name.length() != 0 && et_account.length() != 0 && et_password.length() != 0 && et_password.length() == et_password2.length()) {
                            if (selectId.text == "管理员") {
                                dbManager?.updateAccountByUserName(et_name.text.toString(),et_num.text.toString(),  et_password.text.toString(),
                                        0, et_account.text.toString(), et_phone.text.toString())

                            } else if (selectId.text == "普通用户") {

                                dbManager?.updateAccountByUserName(et_name.text.toString(),et_num.text.toString(),  et_password.text.toString(),
                                        1, et_account.text.toString(), et_phone.text.toString())
                            }
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
            else {
                if(account?.userPower==0) {
                    if(user?.userPower==1) {
                        val dialog = UserDialog(context)
                        dialog.setEdit(dbManager!!.getUserAccountByUserName(tv_FuserLine_Name.text.toString()),0)
                        dialog.setYesOnclickListener("修改", object : UserDialog.onYesOnclickListener {
                            override fun onYesClick() {
                                val userAccount:UserAccount?=null
                                val etName = dialog.findViewById(R.id.et_Duser_account) as EditText
                                val etNum = dialog.findViewById(R.id.et_Duser_num) as EditText
                                val etAccount = dialog.findViewById(R.id.et_Duser_name) as EditText
                                val etPassword = dialog.findViewById(R.id.et_Duser_password) as EditText
                                val etPassword2 = dialog.findViewById(R.id.et_Duser_password2) as EditText
                                val etPhone = dialog.findViewById(R.id.et_Duser_phone) as EditText
                                val rgDuserLevel = dialog.findViewById(R.id.rg_Duser_level) as RadioGroup
                                val selectId = dialog.findViewById(rgDuserLevel.checkedRadioButtonId) as RadioButton
                                if (etName.length() == 0) {
                                    Toast.makeText(context, "账号未填写", Toast.LENGTH_SHORT).show()
                                } else if (etPassword.length() == 0) {
                                    Toast.makeText(context, "密码未填写", Toast.LENGTH_SHORT).show()
                                } else if (etPassword.length() != 0 && etPassword.text.toString() != etPassword2.text.toString()) {
                                    Toast.makeText(context, "两次密码输入不同", Toast.LENGTH_SHORT).show()
                                } else if (etAccount.length() == 0) {
                                    Toast.makeText(context, "姓名未填写", Toast.LENGTH_SHORT).show()
                                } else if (etName.length() != 0 && etAccount.length() != 0 && etPassword.length() != 0 && etPassword.length() == etPassword2.length()) {
                                    if (selectId.text == "管理员") {
                                        dbManager?.updateAccountByUserName(etName.text.toString(),etNum.text.toString(),  etPassword.text.toString(),
                                                0, etAccount.text.toString(), etPhone.text.toString())
                                    } else if (selectId.text == "普通用户") {
                                        dbManager?.updateAccountByUserName(etName.text.toString(),etNum.text.toString(),  etPassword.text.toString(),
                                                1, etAccount.text.toString(), etPhone.text.toString())
                                    }
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
            }
        })

        btn_FuserLine_del.setOnClickListener({
//            dbManager?.deleteAccountByUserName( userName)

            if(btn_FuserLine_del.text.toString()=="禁用") {
                dbManager?.updateStatueByUserName(userName, "1")
                btn_FuserLine_del.text = "启用"
            }
            else {
                dbManager?.updateStatueByUserName(userName, "0")
                btn_FuserLine_del.text = "禁用"
            }
//            deletbuttonClicked("deletperson")
        })
    }


}
