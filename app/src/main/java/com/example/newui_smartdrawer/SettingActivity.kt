package com.example.newui_smartdrawer


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SerialPortInterface
import com.example.newui_smartdrawer.util.UpdateAppManager
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*
import kotlin.concurrent.timerTask

class SettingActivity : BaseActivity() {

    private var dbManager: DBManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        dbManager = DBManager(this)
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
                                val dialog = TopFalseDialog(this@SettingActivity)
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
                                val dialog = TopFalseDialog(this@SettingActivity)
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

        btn_setting_update.setOnClickListener {
            val manager = UpdateAppManager(this)
            manager.getUpdateMsg()
        }
        btn_setting_weight.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请确认称重平台上上没有任何称重物体")
                    .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                        if(dbManager!!.sysSeting.size > 0){
                            val serialPortNum = dbManager!!.sysSeting[0].serialNum.toInt()
                            var serialPortID:String ?= null
                            when (serialPortNum){
                                0 -> serialPortID = "/dev/ttyS1"
                                1 -> serialPortID = "/dev/ttyS2"
                                2 -> serialPortID = "/dev/ttyS3"
                                3 -> serialPortID = "/dev/ttyS4"
                            }
                            val spi = SerialPortInterface(this.applicationContext, serialPortID,38400)
                            val Weight = spi!!.GetLoad()
                            if (Weight == -1) {
                                val dialog = TopFalseDialog(this@SettingActivity)
                                dialog.window.setDimAmount(0f)
                                dialog.setTitle("称重错误")
                                dialog.setMessage("未连接电子秤")
                                dialog.show()
                                dialog.window.setGravity(Gravity.TOP)
                                val t = Timer()
                                t.schedule(timerTask {
                                    dialog.dismiss()
                                    t.cancel()
                                }, 3000)
                            }else {
                                if (dbManager!!.initialWeight.size > 0) {
                                    dbManager?.deleteAllInitialWeight()
                                }
                                dbManager?.addInitialWeight(Weight.toString())
                            }
                        }
                        else
                            Toast.makeText(this,"请进行系统硬件设置", Toast.LENGTH_SHORT).show()

                    })
                    .setNeutralButton("取消",null)
                    .create()
            dialog.show()
            dialog.window.setGravity(Gravity.CENTER)

        }
    }

}
