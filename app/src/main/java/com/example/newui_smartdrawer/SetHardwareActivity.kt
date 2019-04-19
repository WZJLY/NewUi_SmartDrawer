package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_set_hardware.*
import com.example.newui_smartdrawer.util.DBManager



class SetHardwareActivity : BaseActivity() {

    private var dbManager: DBManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_hardware)
        var serialNumber = 0
        var cameraNum = 0
        dbManager = DBManager(this)
        if(dbManager!!.sysSeting.size > 0) {
            serialNumber = dbManager!!.sysSeting[0].serialNum.toInt()
            cameraNum = dbManager!!.sysSeting[0].cameraVersion.toInt()
            Log.d("sysSeting",serialNumber.toString() + "," + cameraNum.toString())
        }
        when(serialNumber){
            0 -> rg_setHardware_dev.check(R.id.rb_setHardware_ttyS1)
            1 -> rg_setHardware_dev.check(R.id.rb_setHardware_ttyS2)
            2 -> rg_setHardware_dev.check(R.id.rb_setHardware_ttyS3)
            3 -> rg_setHardware_dev.check(R.id.rb_setHardware_ttyS4)
        }
        when(cameraNum) {
            0 -> rg_setHardware_camer.check(R.id.rb_setHardware_camera1)
            1 -> rg_setHardware_camer.check(R.id.rb_setHardware_camera2)
        }
        ib_setHardware_back.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
        btn_setHardware_save.setOnClickListener {
            when(rg_setHardware_dev.checkedRadioButtonId){
                rb_setHardware_ttyS1.id -> serialNumber = 0
                rb_setHardware_ttyS2.id -> serialNumber = 1
                rb_setHardware_ttyS3.id -> serialNumber = 2
                rb_setHardware_ttyS4.id -> serialNumber = 3
            }
            when(rg_setHardware_camer.checkedRadioButtonId) {
                rb_setHardware_camera1.id -> cameraNum = 0
                rb_setHardware_camera2.id -> cameraNum = 1
            }
            if(dbManager!!.sysSeting.size > 0) {
                dbManager?.deleteAllSysSeting()
            }
            dbManager?.addSysSeting(serialNumber.toString(),cameraNum.toString())
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
