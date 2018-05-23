package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
        private var mbackKeyPressed=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent =getIntent()
        val account = intent.getStringExtra("acconut")
        tv_mainAdmin_user.text=account

        btn_mainAdmin_setting.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,SetDrawerActivity::class.java)
            startActivity(intent)
        }
        btn_mainAdmin_record.setOnClickListener({


        })
        btn_mainAdmin_management.setOnClickListener({
            val intent = Intent()
            intent.setClass(this,ManagementActivity::class.java)
            startActivity(intent)
        })
        btn_mainAdmin_template.setOnClickListener({


        })
        btn_update.setOnClickListener({



        })
        back_button.setOnClickListener({
            if(!mbackKeyPressed)
                {
                 Toast.makeText(this,"再按一次退出登陆",Toast.LENGTH_SHORT).show()
                    mbackKeyPressed=true
                    Timer().schedule(object :TimerTask(){
                        override fun run() {
                            mbackKeyPressed=false
                        }
                    },2000)
                }
            else{
                val intent = Intent()
                intent.setClass(this,LoginActivity::class.java)
                startActivity(intent)
            }

        })

        ib_mainAdmin_operation.setOnClickListener({

        })
        ib_mainAdmin_search.setOnClickListener({

        })
        ib_mainAdmin_user.setOnClickListener({

        })
    }
}
