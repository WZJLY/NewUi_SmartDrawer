package com.example.newui_smartdrawer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import kotlinx.android.synthetic.main.activity_advertisement.*

class AdvertisementActivity : AppCompatActivity() {
    private var service:MyService?=null
    private var bound = false
    private var sc = MyseriviceConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertisement)
        val intent1 = Intent(this,MyService::class.java)
        bindService(intent1,sc, Context.BIND_AUTO_CREATE)
        iB_advertisement.setOnClickListener {
            service!!.createWindowView()
            service!!.startVideo()
            val intent = Intent()
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    inner class  MyseriviceConnection: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val mybinder = p1 as MyService.MyBinder
            service=mybinder.getService()
            bound=true
        }

    }
}
