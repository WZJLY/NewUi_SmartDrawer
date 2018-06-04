package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import com.example.lib_zxing.activity.CaptureActivity
import com.example.lib_zxing.activity.CodeUtils
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SerialPortInterface
import kotlinx.android.synthetic.main.activity_operation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class OperationActivity : AppCompatActivity(),DrawerFragment2.updateDrawerlisten {
    private var scApp: SCApp? = null
    private var dbManager: DBManager? = null
    private var statue: String? = null
    var spi: SerialPortInterface? = null
    private var REQUEST_CODE = 1
    var stop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)
        if (!EventBus.getDefault().hasSubscriberForEvent(OperationActivity::class.java)) {
            EventBus.getDefault().register(this)
        }
        dbManager = DBManager(applicationContext)
        val cabinetFragment = CabinetFragment()
        val bundle = Bundle()
        bundle.putString("delCabinet","false")
        bundle.putString("choice","true")
        bundle.putString("cabinetNum","柜1")
        cabinetFragment.arguments = bundle
        addFragment(R.id.ll_operation_cabinet, cabinetFragment)
        scApp = application as SCApp
        changeButton("noFocusable")
        updateDrawer()
        //打开串口
//        if(dbManager!!.sysSeting.size > 0){
//            val serialPortNum = dbManager!!.sysSeting[0].serialNum.toInt()
//            var serialPortID:String ?= null
//            when (serialPortNum){
//                0 -> serialPortID = "/dev/ttyS1"
//                1 -> serialPortID = "/dev/ttyS2"
//                2 -> serialPortID = "/dev/ttyS3"
//                3 -> serialPortID = "/dev/ttyS4"
//            }
//            spi = SerialPortInterface(this.applicationContext, serialPortID)
//            scApp?.setSpi(spi)
//        }
//        else
//            Toast.makeText(this,"请进行系统硬件设置", Toast.LENGTH_SHORT).show()

        ib_operation_back.setOnClickListener{
            finish()
            overridePendingTransition(0, 0)
        }
        ib_operation_search.setOnClickListener {
            val intent =Intent()
            intent.setClass(this,SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        btn_operation_into.setOnClickListener {
            //入柜
            statue = "Into"
            Toast.makeText(this, "请将试剂放到电子秤上", Toast.LENGTH_SHORT).show()
//            spi?.sendLED(1, 1)
//            weighThread().start()
            var intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
            overridePendingTransition(0, 0)
        }
        btn_operation_take.setOnClickListener {
            //取用
            val intent =Intent()
            intent.putExtra("subOperation","Take")
            intent.setClass(this,SubOperationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        btn_operation_return.setOnClickListener {
            //归还
            statue = "Return"
            Toast.makeText(this, "请将试剂放到电子秤上", Toast.LENGTH_SHORT).show()
//            spi?.sendLED(1, 1)
//            weighThread().start()
            var intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
            overridePendingTransition(0, 0)
        }
        btn_operation_remove.setOnClickListener {
            //移除
            val intent =Intent()
            intent.putExtra("subOperation","Remove")
            intent.setClass(this,SubOperationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }

    @Subscribe
    fun onEvent(event: BtnEvent){
        changeButton(event.getMsg())
    }

    override fun onStart() {
        super.onStart()
                            //利用全局变量获得试剂位置并更新UI
    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    override fun updateDrawerClick(text: String) {
        if(text == "update")
        {
            finish()
            overridePendingTransition(0, 0)
            val intent = Intent()
            intent.setClass(this,OperationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val intent = Intent()
        intent.setClass(this, SubOperationActivity::class.java)
        intent.putExtra("subOperation", statue)
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                val bundle = data.extras
                if (bundle == null) {
                    Toast.makeText(this, "EMPTY", Toast.LENGTH_LONG).show()
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    intent.putExtra("scan_value", result)
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()

                }
//                if (stop) {
//                    val Double = scApp!!.initialWeight.toDouble()
//                    intent.putExtra("weight", (Double/10).toString())
//                }
//                else {
//                    stop = true
//                }
            }
        }

        spi?.sendLED(1,0)
        weighThread().interrupt()
        startActivity(intent)
    }

    fun updateDrawer()          //通过遍历数据库更新抽屉界面
    {
        val sum = dbManager?.drawers!!.size
        if (sum == 0) {
            Toast.makeText(this, "请添加抽屉", Toast.LENGTH_SHORT).show()
        } else {
            if (sum > 0) {
                for (i in 1..sum) {
                    val drawerFragment2 = DrawerFragment2()
                    val args = Bundle()
                    args.putInt("drawerID", i)
                    drawerFragment2.arguments = args
                    addFragment(R.id.ll_operation_drawer, drawerFragment2)
                }
            }
        }
    }
    fun changeButton(text: String) {
        when(text) {
            "noFocusable" -> {
                btn_operation_into.isEnabled = false
                btn_operation_take.isEnabled = false
                btn_operation_return.isEnabled = true
                btn_operation_remove.isEnabled = false
            }
            "into" -> {
                btn_operation_into.isEnabled = true
                btn_operation_take.isEnabled = false
                btn_operation_return.isEnabled = true
                btn_operation_remove.isEnabled = false
            }
            "take" -> {
                btn_operation_into.isEnabled = false
                btn_operation_take.isEnabled = true
                btn_operation_return.isEnabled = true
                btn_operation_remove.isEnabled = true
            }
            "return" -> {
                btn_operation_into.isEnabled = false
                btn_operation_take.isEnabled = false
                btn_operation_return.isEnabled = true
                btn_operation_remove.isEnabled = false
            }
        }
    }

    inner class weighThread : Thread(){
        override fun run() {
            stop = false
            while (!stop) {
                val weight = arrayOf(0,0,0,0,0)
                for (tim in 0..4) {
                    if (stop)
                        break
                    weight[tim] = spi!!.GetLoad()
                    if (weight[tim] < scApp!!.initialWeight)
                        break
                    else {
                        if (weight[tim] - scApp!!.initialWeight < 20) {
                            Thread.sleep(500)
                            break
                        } else {
                            if (tim < 4) {
                                continue
                            } else {
                                var outIndex: Int = 0
                                var inIndex: Int = 0
                                var temp: Int = 0
                                while (outIndex < 3) {
                                    inIndex = outIndex + 1
                                    while (inIndex < 4) {
                                        if (weight[outIndex] > weight[inIndex]) {
                                            temp = weight[outIndex]
                                            weight[outIndex] = weight[inIndex]
                                            weight[inIndex] = temp
                                        }
                                        inIndex++
                                    }
                                    outIndex++
                                }
                                if (weight[4] - weight[0] < 50) {
                                    var load = 0
                                    for (i in 0..4) {
                                        load += weight[i]
                                    }
                                    scApp?.initialWeight = load / 5 - scApp!!.initialWeight
                                    stop = true
                                }
                                else
                                    break
                            }
                        }
                    }
                }
            }
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }

    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }
}
