package com.example.newui_smartdrawer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import com.example.newui_smartdrawer.util.SerialPortInterface
import com.google.zxing.common.StringUtils
import kotlinx.android.synthetic.main.activity_read_th.*

class ReadTHActivity : AppCompatActivity() {
    var spi: SerialPortInterface?= null
    private var service:MyService?=null
    private var bound = false
    private var sc = MyseriviceConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_th)
        spi = SerialPortInterface(this.application, "/dev/ttyUSB0",9600)
        spi?.readTHData()
        TimeThread().start()
        bg1_image.setOnClickListener{
            val intent = Intent()
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)

        }
        bg2_image.setOnClickListener{

            val intent = Intent()
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
    internal inner class TimeThread : Thread() {
        override fun run() {
            do {
                try {
                    var str = spi?.readTHData().toString()
                    if( str!=null)
                    {
                        if(str.length>5) {
                            val msg = Message()
                            msg.what = 0  //消息(一个整型值)
                            msg.obj = str
                            mHandler.sendMessage(msg)
                        }
                    }
                    Thread.sleep(1000)// 每隔1秒发送一个msg给mHandler
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            } while (true)
        }
    }
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    //在这里得到数据，并且可以直接更新UI

                    val str = msg.obj.toString()
                    val tem = str.substring(0,str.indexOf(".")+2)
                    val ht =  str.substring(str.indexOf(".")+2)
                    t1View.text=tem+"℃"
                    h1View.text=ht+"%"
                }



            }
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
