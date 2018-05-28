package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.dialog_binding.*


class SettingActivity : AppCompatActivity() {
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
            val dialog = BindingDialog(this)
            dialog.show()
            dialog.setYesOnclickListener(object :BindingDialog.onYesOnclickListener{
                override fun onYesClick() {
                    dialog.dismiss()
                }
            })
        }
    }
}
