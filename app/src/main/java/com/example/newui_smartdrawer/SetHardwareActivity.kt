package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_set_hardware.*
import android.widget.EditText



class SetHardwareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_hardware)
        ib_setHardware_back.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
