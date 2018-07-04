package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*
import kotlin.concurrent.timerTask


class SettingActivity : BaseActivity() {
    private var dbManager: DBManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        ib_setting_back.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }

        btn_setting_cabinet.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,SetCabinetActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        btn_setting_hardware.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,SetHardwareActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        btn_setting_binding.setOnClickListener {
            dbManager = DBManager(this)
            val dialog = BindingDialog(this)
            if(dbManager!!.cabinetNo.size > 0)
                dialog.setBinding(dbManager!!.cabinetNo[0].cabinetNo,dbManager!!.cabinetNo[0].cabinetServiceCode)
            dialog.setYesOnclickListener(object :BindingDialog.onYesOnclickListener{
                override fun onYesClick() {
                    if(dbManager!!.cabinetNo.size > 0) {
                        dbManager?.deleteAllCabinetNo()
                        dialog.changeBtn()
                    }
                    else {
                        val cabinetNo = dialog.findViewById<EditText>(R.id.et_Dbinding_number)
                        val serviceCode = dialog.findViewById<EditText>(R.id.et_Dbinding_serviceCode)
                        if (cabinetNo.length() > 0) {
                            if (serviceCode.length() > 0) {
                                dbManager?.addCabinetNo(cabinetNo.text.toString(), serviceCode.text.toString())
                                dialog.dismiss()
                            } else {
                                val dialog = TopFalseDialog(applicationContext)
                                dialog.window.setDimAmount(0f)
                                dialog.setTitle("服务码未填写")
                                dialog.setMessage("请填写服务码")
                                dialog.show()
                                dialog.window.setGravity(Gravity.TOP)
                                val t = Timer()
                                t.schedule(timerTask {
                                    dialog.dismiss()
                                    t.cancel()
                                },3000)
                            }
                        } else {
                            if (serviceCode.length() > 0){
                                val dialog = TopFalseDialog(applicationContext)
                                dialog.window.setDimAmount(0f)
                                dialog.setTitle("智能柜编号未填写")
                                dialog.setMessage("请填写智能柜编号")
                                dialog.show()
                                dialog.window.setGravity(Gravity.TOP)
                                val t = Timer()
                                t.schedule(timerTask {
                                    dialog.dismiss()
                                    t.cancel()
                                },3000)
                            }
                            else {
                                val dialog = TopFalseDialog(applicationContext)
                                dialog.window.setDimAmount(0f)
                                dialog.setTitle("智能柜编号和服务码未填写")
                                dialog.setMessage("请填写智能柜编号和服务码")
                                dialog.show()
                                dialog.window.setGravity(Gravity.TOP)
                                val t = Timer()
                                t.schedule(timerTask {
                                    dialog.dismiss()
                                    t.cancel()
                                },3000)
                            }
                        }
                    }
                }
            })
            dialog.setNoOnclickListener(object :BindingDialog.onNoOnclickListener{
                override fun onNoClick() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.window.setGravity(Gravity.CENTER)
        }
    }
}
