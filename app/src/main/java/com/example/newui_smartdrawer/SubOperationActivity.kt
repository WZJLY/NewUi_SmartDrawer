package com.example.newui_smartdrawer

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.lib_zxing.activity.CaptureActivity
import com.example.lib_zxing.activity.CodeUtils
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SerialPortInterface
import kotlinx.android.synthetic.main.activity_sub_operation.*
import kotlinx.android.synthetic.main.fragment_into.*
import kotlinx.android.synthetic.main.fragment_return.*

class SubOperationActivity : AppCompatActivity(),IntoFragment.intobuttonlisten,ReturnFragment.returnbuttonlisten {
    private var scApp: SCApp? = null
    private var dbManager: DBManager?=null
    var spi: SerialPortInterface?= null
    private var REQUEST_CODE = 1
    private var statue: String ?= null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_operation)

        ib_subOperation_back.setOnClickListener({
            finish()
            overridePendingTransition(0, 0)
        })

        scApp = application as SCApp
        dbManager = DBManager(applicationContext)
        val subOperation: String = intent.getStringExtra("subOperation")
        val scanValue = intent.getStringExtra("scan_value")
        val weight = intent.getStringExtra("weight")
        spi =  scApp?.getSpi()

        when(subOperation) {
            "Into" -> {
                val informationFragment = IntoFragment()
                val args = Bundle()
                args.putString("scan_value", scanValue)
                args.putString("weight",weight)
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)
            }
            "Take" -> {
                tv_subOperation_title.text = "试剂取用"
                val informationFragment = InformationFragment()
                val args = Bundle()
                args.putString("operation", "operation")
//                args.putString("state","in")
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)

            }
            "Return" -> {
                tv_subOperation_title.text = "试剂归还"
                val informationFragment = ReturnFragment()
                val args = Bundle()
                args.putString("scan_value", scanValue)
                args.putString("weight",weight)
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)
            }
            "Remove" -> {
                tv_subOperation_title.text = "试剂移除"
                val informationFragment = InformationFragment()
                val args = Bundle()
                args.putString("operation", "operation")
//                args.putString("state","in")
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)
            }
        }
    }

    override fun intobuttononClick(text: String) {
        if (text == "scan") {
            spi?.sendLED(1,1)
            var intent = Intent(this,CaptureActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }

    override fun returnbuttonClick(text: String) {
        if (text == "scan") {
            spi?.sendLED(1,1)
            var intent = Intent(this,CaptureActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        spi?.sendLED(1,0)
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                val bundle = data.extras
                if (bundle == null) {
                    Toast.makeText(this, "EMPTY", Toast.LENGTH_LONG).show()
                    return
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    when(statue) {
                        "Into" -> {
                            et_Finto_code.setText(result)
                            et_Finto_load.isFocusable = true
                            et_Finto_load.isFocusableInTouchMode = true
                            et_Finto_load.requestFocus()
                        }
                        "Return" -> {
                            et_Freturn_code.setText(result)
                            et_Freturn_load.isFocusable = true
                            et_Freturn_load.isFocusableInTouchMode = true
                            et_Freturn_load.requestFocus()
                        }
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    fun checkLock(DID: Int,num: Int): Int? {
        var time:Int = 0
        for(i in 1..num) {
            val lockData = spi?.sendGetStat(DID)
            if (lockData != null) {
                val drawerId = lockData.indexOf("1")+1
                Log.d("SubOperation",""+drawerId)
                if (drawerId > 0)
                    return drawerId
                else
                    continue
            }
            else {
                if (i > 1)
                    return null
                else{
                    time++
                    continue
                }
            }
        }
        return 0
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
