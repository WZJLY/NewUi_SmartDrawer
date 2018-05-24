package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_set_cabinet.*

class SetCabinetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_cabinet)
        ib_setCabinet_addDrawer.setOnClickListener({

            val intent = Intent()
            intent.setClass(this,SetDrawerActivity::class.java)
            startActivity(intent)


        })
        ib_setCabinet_back.setOnClickListener({

            finish()
        })
    }

}
