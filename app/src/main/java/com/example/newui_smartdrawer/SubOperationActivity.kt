package com.example.newui_smartdrawer

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.lib_zxing.activity.CaptureActivity
import com.example.lib_zxing.activity.CodeUtils
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.ReagentTemplate
import com.example.newui_smartdrawer.util.SerialPortInterface
import com.example.newui_smartdrawer.util.UploadRecordManager
import kotlinx.android.synthetic.main.activity_sub_operation.*
import kotlinx.android.synthetic.main.fragment_into.*
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.android.synthetic.main.fragment_return.*
import java.text.SimpleDateFormat
import java.util.*

class SubOperationActivity : AppCompatActivity(),IntoFragment.intobuttonlisten,ReturnFragment.returnbuttonlisten {
    private var scApp: SCApp? = null
    private var dbManager: DBManager?=null
    var spi: SerialPortInterface?= null
    private var REQUEST_CODE = 1
    private var statue: String ?= null
    private var reagentTemplate: ReagentTemplate ?= null

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
                tv_subOperation_num.text="柜子1-抽屉"+scApp?.touchdrawer
                val informationFragment = IntoFragment()
                val args = Bundle()
                args.putString("scan_value", scanValue)
                args.putString("weight",weight)
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)
            }
            "Take" -> {
                tv_subOperation_num.text="柜子1-抽屉"+scApp?.touchdrawer
                tv_subOperation_title.text = "试剂取用"
                val informationFragment = InformationFragment()
                val args = Bundle()
                args.putString("operation", "operation")
                args.putString("state","in")
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)

            }
            "Return" -> {
                tv_subOperation_num.text="柜子1-抽屉"+scApp?.touchdrawer
                tv_subOperation_title.text = "试剂归还"
                val informationFragment = ReturnFragment()
                val args = Bundle()
                args.putString("scan_value", scanValue)
                args.putString("weight",weight)
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)
            }
            "Remove" -> {
                tv_subOperation_num.text="柜子1-抽屉"+scApp?.touchdrawer
                tv_subOperation_title.text = "试剂移除"
                val informationFragment = InformationFragment()
                val args = Bundle()
                args.putString("operation", "operation")
                args.putString("state","in")
                informationFragment.arguments = args
                replaceFragment(R.id.fl_subOperation_inf,informationFragment)
            }
        }

        btn_subOperation_OK.setOnClickListener {
            var drawerID = scApp!!.touchdrawer
            var table = scApp!!.touchtable
            when (subOperation) {
                "Into" -> {
                    val code = et_Finto_code.text.toString()
                    val load = et_Finto_load.text.toString()
                    val residue = et_Finto_residue.text.toString()
                    if(code.isNotEmpty() && load.isNotEmpty() && residue.isNotEmpty()){
                        if (!dbManager!!.isReagentExist(code) && !dbManager!!.isScrapReagentExist(code)) {
//                            var into_drawer = checkLock(1, 2)
//                            if (into_drawer == null)
//                                Toast.makeText(this, "ERROR：串口通讯！", Toast.LENGTH_SHORT).show()
//                            else if (into_drawer != drawerID && into_drawer > 0)
//                                Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
//                            else {
//                                if (into_drawer != drawerID) {
//                                    spi?.sendOpenLock(1, drawerID)
//                                    Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
//                                    into_drawer = checkLock(1, 190)
//                                }
//                                if (into_drawer == drawerID) {
                                    //弹窗
                                    val dialog = BottomDialog(this)
                                    dialog.setYesOnclickListener(null ,object :BottomDialog.onYesOnclickListener {
                                        override fun onYesClick() {
                                            reagentTemplate = dbManager!!.reagentTemplate.get(scApp!!.templateNum)
                                            var Unit = 1
                                            if (reagentTemplate?.reagentUnit == "ml") {
                                                Unit = 2
                                            }
                                            dbManager?.addReagent(code, reagentTemplate?.reagentName, "", ""
                                                    , "", 1, reagentTemplate?.reagentPurity, residue, load
                                                    , reagentTemplate?.reagentCreater, reagentTemplate?.reagentGoodsID, Unit, reagentTemplate?.reagentDensity, tv_Finto_data.text.toString()
                                                    , "1", drawerID.toString(), table.toString(), 1, scApp!!.userInfo.getUserName())
                                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                            val now = sdf.format(Date())
                                            val curDate = Date(System.currentTimeMillis())
                                            val str = sdf.format(curDate)
                                            val upload  = UploadRecordManager(this@SubOperationActivity)
                                            upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo,"添加试剂",scApp!!.userInfo.userName,str,reagentTemplate?.reagentName)
                                            dbManager?.addReagentUserRecord(code,1,now,scApp!!.userInfo.getUserName(),load+"g",residue+reagentTemplate?.reagentUnit,"")
                                            scApp?.touchtable = 0 //新加的
                                            finish()
                                            overridePendingTransition(0, 0)
                                            dialog.dismiss()
                                        }
                                    })
                                    dialog.setNoOnclickListener(null, object : BottomDialog.onNoOnclickListener {
                                        override fun onNoClick() {
                                            dialog.dismiss()
                                        }
                                    })
                                    dialog.show()
                                    dialog.window.setGravity(Gravity.BOTTOM)
//                                }
//                            }
                        } else
                            Toast.makeText(this, "该试剂编码已使用", Toast.LENGTH_SHORT).show()
                    }
                    else if (code.isNotEmpty()){
                        if(load.isNotEmpty()) {
                            Toast.makeText(this, "试剂余量未填写", Toast.LENGTH_SHORT).show()
                        }else if (residue.isNotEmpty()) {
                            Toast.makeText(this, "称重重量未填写", Toast.LENGTH_SHORT).show()
                        }else {
                            Toast.makeText(this, "称重重量和试剂余量未填写", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        if (load.isNotEmpty()&& residue.isNotEmpty()){
                            Toast.makeText(this, "试剂条码未填写", Toast.LENGTH_SHORT).show()
                        }
                        if(load.isNotEmpty()) {
                            Toast.makeText(this, "试剂条码和试剂余量未填写", Toast.LENGTH_SHORT).show()
                        }else if (residue.isNotEmpty()) {
                            Toast.makeText(this, "试剂条码和称重重量未填写", Toast.LENGTH_SHORT).show()
                        }else {
                            Toast.makeText(this, "试剂条码、称重重量和试剂余量未填写", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Take" -> {
//                    var into_drawer = checkLock(1, 2)
//                    if (into_drawer == null)
//                        Toast.makeText(this, "ERROR：串口通讯！", Toast.LENGTH_SHORT).show()
//                    else if (into_drawer != drawerID && into_drawer > 0)
//                        Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
//                    else {
//                        if (into_drawer != drawerID) {
//                            spi?.sendOpenLock(1, drawerID)
//                            Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
////                            Thread.sleep(50)
//                            into_drawer = checkLock(1, 190)
//                        }
//                        if (into_drawer == drawerID) {
                            //弹窗
                            val dialog = BottomDialog(this)
                            dialog.setMessage("请将试剂从抽屉中取出")
                            dialog.setYesOnclickListener("已取出",object :BottomDialog.onYesOnclickListener {
                                override fun onYesClick() {
                                    dbManager?.updateReagentStatusByPos("" + drawerID, "" + scApp?.touchtable, scApp!!.userInfo.getUserName(), 2)
                                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                    val now = sdf.format(Date())
                                    val reagentId =  dbManager!!.getReagentByPos("" + drawerID,"" + scApp?.touchtable).reagentId
                                    val reagent = dbManager?.getReagentById(reagentId)
                                    var unit = "g"
                                    if(reagent?.reagentUnit==2)
                                        unit = "ml"
                                    dbManager?.addReagentUserRecord(reagentId,2,now,scApp!!.userInfo.getUserName(),reagent?.reagentTotalSize+"g",reagent?.reagentSize+unit,"")
                                    val curDate = Date(System.currentTimeMillis())
                                    val str = sdf.format(curDate)
                                    val upload : UploadRecordManager = UploadRecordManager(this@SubOperationActivity)
                                    upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo,"取用试剂",scApp!!.userInfo.userName,str,reagent?.reagentName)
                                    finish()
                                    overridePendingTransition(0, 0)
                                    dialog.dismiss()
                                }
                            })
                            dialog.setNoOnclickListener(null, object : BottomDialog.onNoOnclickListener {
                                override fun onNoClick() {
                                    dialog.dismiss()
                                }
                            })
                            dialog.show()
                            dialog.window.setGravity(Gravity.BOTTOM)
                        }
//                    }
//                }

                "Return" -> {
                    val code = et_Freturn_code.text.toString()
                    val load = et_Freturn_load.text.toString()
                    if(code.isNotEmpty()) {
                        if (dbManager!!.isReagentExist(code) && !dbManager!!.isScrapReagentExist(code)) {
                            if (dbManager!!.getReagentById(code).status == 2) {
                                if (load.isNotEmpty()) {
//                                    var into_drawer = checkLock(1, 2)
//                                    if (into_drawer == null)
//                                        Toast.makeText(this, "ERROR：串口通讯！", Toast.LENGTH_SHORT).show()
//                                    else if (into_drawer != drawerID && into_drawer > 0)
//                                        Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
//                                    else {
//                                        if (into_drawer != drawerID) {
//                                            spi?.sendOpenLock(1, drawerID)
//                                            Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
////                                        Thread.sleep(50)
//                                            into_drawer = checkLock(1, 190)
//                                        }
//                                        if (into_drawer == drawerID) {
                                            table = dbManager?.getReagentById(code)!!.reagentPosition.toInt()
                                            var reagent = dbManager!!.getReagentById(code)
                                            //弹窗
                                            val dialog = BottomDialog(this)
                                            dialog.setYesOnclickListener(null, object : BottomDialog.onYesOnclickListener {
                                                override fun onYesClick() {
                                                    dbManager?.updateReagentStatus(code, 1, scApp!!.userInfo.getUserName())
                                                    var weight: Int = Integer.valueOf(load)
                                                    var density = "1"
                                                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                                    val now = sdf.format(Date())
                                                    Log.d("suboperationReturn", density)
                                                    if (reagent.reagentDensity.length > 0) {
                                                        density = reagent.reagentDensity
                                                    }
                                                    Log.d("suboperationReturn2", density)
                                                    if (weight > reagent.reagentTotalSize.toInt()) {
                                                        weight -= reagent.reagentTotalSize.toInt()
                                                        var size = reagent.reagentSize.toDouble() - (weight / density.toDouble())
                                                        dbManager?.updateReagentSize(code, size.toString(),load)

                                                        var unit = "g"
                                                        if (reagent?.reagentUnit == 2)
                                                            unit = "ml"
                                                        dbManager?.addReagentUserRecord(code, 3, now, scApp!!.userInfo.getUserName(), load + "g", size.toString() + unit, (weight / density.toDouble()).toString())
                                                    } else {
                                                        weight = reagent.reagentTotalSize.toInt() - weight
                                                        var size1 = reagent.reagentSize.toDouble() - (weight / density.toDouble())
                                                        dbManager?.updateReagentSize(code, size1.toString(),load)
                                                        var unit = "g"
                                                        if (reagent?.reagentUnit == 2)
                                                            unit = "ml"
                                                        dbManager?.addReagentUserRecord(code, 3, now, scApp!!.userInfo.getUserName(), load + "g ", size1.toString() + unit, (weight / density.toDouble()).toString())
                                                    }
                                                    val curDate = Date(System.currentTimeMillis())
                                                    val str = sdf.format(curDate)
                                                    val upload: UploadRecordManager = UploadRecordManager(this@SubOperationActivity)
                                                    upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo, "归还试剂", scApp!!.userInfo.userName, str, reagent?.reagentName)
                                                    finish()
                                                    overridePendingTransition(0, 0)
                                                    dialog.dismiss()
                                                }
                                            })
                                            dialog.setNoOnclickListener(null, object : BottomDialog.onNoOnclickListener {
                                                override fun onNoClick() {
                                                    dialog.dismiss()
                                                }
                                            })
                                            dialog.show()
                                            dialog.window.setGravity(Gravity.BOTTOM)
//                                        }
//                                    }
                                } else
                                    Toast.makeText(this, "称重重量未填写", Toast.LENGTH_SHORT).show()
                            } else
                                Toast.makeText(this, "该试剂在位", Toast.LENGTH_SHORT).show()
                        } else
                            Toast.makeText(this, "无该试剂", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        if (load.isNotEmpty()){
                            Toast.makeText(this, "试剂条码未填写", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this, "试剂条码和称重重量未填写", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Remove" -> {
//                    var into_drawer = checkLock(1, 2)
//                    if (into_drawer == null)
//                        Toast.makeText(this, "ERROR：串口通讯！", Toast.LENGTH_SHORT).show()
//                    else if (into_drawer != drawerID && into_drawer > 0)
//                        Toast.makeText(this, " 请关闭" + (into_drawer) + "号抽屉", Toast.LENGTH_SHORT).show()
//                    else {
//                        if (into_drawer != drawerID) {
//                            spi?.sendOpenLock(1, drawerID)
//                            Toast.makeText(this, " 请拉开" + (drawerID) + "号抽屉", Toast.LENGTH_SHORT).show()
////                            Thread.sleep(50)
//                            into_drawer = checkLock(1, 190)
//                        }
//                        if (into_drawer == drawerID) {
                            //弹窗
                            val dialog = BottomDialog(this)
                            dialog.setMessage("请将试剂从抽屉中取出")
                            dialog.setYesOnclickListener("已取出",object :BottomDialog.onYesOnclickListener {
                                override fun onYesClick() {
                                    val reagentId=dbManager!!.getReagentByPos("" + drawerID,"" + scApp?.touchtable).reagentId
                                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                    val now = sdf.format(Date())
                                    val reagent = dbManager?.getReagentById(reagentId)
                                    dbManager?.addSrapReagent(reagent?.reagentId,reagent?.reagentName,"","","",reagent!!.reagentType.toInt(),reagent?.reagentPurity,reagent?.reagentSize,reagent?.reagentTotalSize,reagent?.reagentCreater,"",reagent?.reagentUnit,reagent?.reagentDensity,reagent?.reagentInvalidDate,reagent?.reagentCabinetId,drawerID.toString(),reagent?.reagentPosition,4,scApp!!.userInfo.getUserName())
                                    dbManager?.deleteReagentById(reagentId)
                                    dbManager?.addReagentUserRecord(reagentId,4,now,scApp!!.userInfo.getUserName(),reagent?.reagentTotalSize+"g","","")
                                    val curDate = Date(System.currentTimeMillis())
                                    val str = sdf.format(curDate)
                                    val upload : UploadRecordManager = UploadRecordManager(this@SubOperationActivity)
                                    upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo,"移除试剂",scApp!!.userInfo.userName,str,reagent?.reagentName)
                                    val intent = Intent()
                                    intent.setClass(applicationContext, OperationActivity::class.java)
                                    startActivity(intent)
                                    dialog.dismiss()
                                }
                            })
                            dialog.setNoOnclickListener(null, object : BottomDialog.onNoOnclickListener {
                                override fun onNoClick() {
                                    dialog.dismiss()
                                }
                            })
                            dialog.show()
                            dialog.window.setGravity(Gravity.BOTTOM)
                        }
//                    }
//                }
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
        var time = 0
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
                if (time > 0){
                    return null
                }
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
