package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.newui_smartdrawer.util.SerialPortInterface

class ReadTHActivity : AppCompatActivity() {
    var spi: SerialPortInterface?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_th)
        spi = SerialPortInterface(this.application, "/dev/ttyUSB0")
        spi?.readData()
        TimeThread().start()
    }
    internal inner class TimeThread : Thread() {
        override fun run() {
            do {
                try {
                    var str = spi?.readData()
                    if( str!=null)
                    {
                        val msg = Message()
                        msg.what = 0  //消息(一个整型值)
                        msg.obj = str
                        mHandler.sendMessage(msg)
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
                    val str = msg.obj
                    Log.d("wzj",str.toString())
                }



            }
        }
    }
}
