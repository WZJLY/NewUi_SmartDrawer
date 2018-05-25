package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val scApp = application as SCApp

        ib_search_back.setOnClickListener({
            finish()
           overridePendingTransition(0, 0)
        })
        test_button.setOnClickListener({   //查找试剂位置时传参
//            finish()
//            val intent = Intent()
//            scApp.reagentID="123"
//            intent.setClass(this,OperationActivity::class.java)
//            startActivity(intent)

        })

    }

}
