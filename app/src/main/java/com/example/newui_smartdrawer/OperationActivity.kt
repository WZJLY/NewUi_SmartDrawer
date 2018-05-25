package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_operation.*

class OperationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)

        btn_operation_into.isEnabled = false

        ib_operation_back.setOnClickListener({

            finish()
            overridePendingTransition(0, 0)

        })

        ib_operation_search.setOnClickListener(
                {
                    val intent =Intent()
                    intent.setClass(this,SearchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
        )


    }

     override fun onStart() {
        super.onStart()
                            //利用全局变量获得试剂位置并更新UI
    }
}
