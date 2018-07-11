package com.example.newui_smartdrawer

import android.content.DialogInterface
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.ReagentTemplate
import com.example.newui_smartdrawer.util.SC_Const
import kotlinx.android.synthetic.main.activity_template.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.concurrent.timerTask

class TemplateActivity : BaseActivity(),TemplateFragment.deletTemplatelisten {
    private var dbManager:DBManager?=null
    private var reagentTemplate: ReagentTemplate?=null
    private var scApp: SCApp? = null
    var mHandler: Handler = object : Handler() {
        override  fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    //在这里得到数据，并且可以直接更新UI
                    val data = msg.obj as String
//                    Toast.makeText(this@TemplateActivity,data,Toast.LENGTH_SHORT).show()
                    val templateLine = VerticalFragment()
                    val args = Bundle()
                    args.putString("addtemplate","add")
                    templateLine.arguments=args
                    replaceFragment(templateLine,R.id.fl_template)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)
        dbManager= DBManager(this)
        scApp = application as SCApp
        val templateLine = VerticalFragment()
        val args = Bundle()
        args.putString("addtemplate","add")
        templateLine.arguments=args
        replaceFragment(templateLine,R.id.fl_template)
        ib_Template_back.setOnClickListener({
            finish()
            overridePendingTransition(0, 0)
        })
        btn_template_batchImport.setOnClickListener({
            val edit= EditText(this)
            val dialog = AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setView(edit)
                    .setMessage("请输入试剂模板编号")
                    .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                        scApp?.templateID=edit.text.toString()
                        downLoad() //下载与导入模板的线程开启
                    })
                    .setNeutralButton("取消",null)
                    .create()
            dialog.show()
            dialog.window.setGravity(Gravity.CENTER)
        })
        btn_template_allDel.setOnClickListener({

            dbManager?.deleteAllReagentTemplate()
            val templateLine = VerticalFragment()
            val args = Bundle()
            args.putString("addtemplate","add")
            templateLine.arguments=args
            replaceFragment(templateLine,R.id.fl_template)

        })
        btn_template_import.setOnClickListener{
            val dialog = ImportDialog(this)
            dialog.onYesOnclickListener(object :ImportDialog.onYesOnclickListener{
                override fun onYesClick() {
                    var et_name = dialog.findViewById(R.id.et_Dimport_name) as EditText
                    var et_volume = dialog.findViewById(R.id.et_Dimport_volume) as EditText
                    var et_purity = dialog.findViewById(R.id.et_Dimport_purity) as EditText
                    var et_manfactor = dialog.findViewById(R.id.et_Dimport_manfactor) as EditText
                    var et_density = dialog.findViewById(R.id.et_Dimport_density) as EditText
                    var et_code = dialog.findViewById(R.id.et_Dimport_code) as EditText
                    var et_anotherName = dialog.findViewById(R.id.et_Dimport_anotherName) as EditText
                    var template_density = "1"
                    var rg_Dimport_level=dialog.findViewById(R.id.rg_Dimport_level) as RadioGroup
                    if (et_name.length()>0) {
                        if (et_volume.length()>0) {
                            if(et_density.length()>0)
                                template_density = et_density.text.toString()
                            if (rg_Dimport_level.checkedRadioButtonId  > 0) {
                                var selectId=dialog.findViewById(rg_Dimport_level.checkedRadioButtonId) as RadioButton
                                if (selectId.text.toString() == "固体") {
                                    dbManager?.addReagentTemplate("", et_name.text.toString(), et_anotherName.text.toString(), "", "",
                                            1, et_purity.text.toString(), et_volume.text.toString(), et_manfactor.text.toString(),
                                            et_code.text.toString(), "g", template_density)
                                    val templateLine = VerticalFragment()
                                    val args = Bundle()
                                    args.putString("addtemplate","add")
                                    templateLine.arguments=args
                                    replaceFragment(templateLine,R.id.fl_template)
                                    dialog.dismiss()
                                } else if (selectId.text.toString() == "液体") {
                                    dbManager?.addReagentTemplate("", et_name.text.toString(), et_anotherName.text.toString(), "", "",
                                            2, et_purity.text.toString(), et_volume.text.toString(), et_manfactor.text.toString(),
                                            et_code.text.toString(), "ml", template_density)
                                    val templateLine = VerticalFragment()
                                    val args = Bundle()
                                    args.putString("addtemplate","add")
                                    templateLine.arguments=args
                                    replaceFragment(templateLine,R.id.fl_template)
                                    dialog.dismiss()
                                }
                            }
                            else {
                                val dialog = TopFalseDialog(this@TemplateActivity)
                                dialog.window.setDimAmount(0f)
                                dialog.setTitle("试剂形态未填写")
                                dialog.setMessage("请填写试剂形态")
                                dialog.show()
                                dialog.window.setGravity(Gravity.TOP)
                                val t = Timer()
                                t.schedule(timerTask {
                                    dialog.dismiss()
                                    t.cancel()
                                },3000)
                            }
                        }
                        else{
                            val dialog = TopFalseDialog(this@TemplateActivity)
                            dialog.window.setDimAmount(0f)
                            dialog.setTitle("试剂瓶容量未填写")
                            dialog.setMessage("请填写试剂瓶容量")
                            dialog.show()
                            dialog.window.setGravity(Gravity.TOP)
                            val t = Timer()
                            t.schedule(timerTask {
                                dialog.dismiss()
                                t.cancel()
                            },3000)
                        }
                    }
                    else {
                        if (et_volume.length()>0){
                            val dialog = TopFalseDialog(this@TemplateActivity)
                            dialog.window.setDimAmount(0f)
                            dialog.setTitle("试剂名未填写")
                            dialog.setMessage("请填写试剂名")
                            dialog.show()
                            dialog.window.setGravity(Gravity.TOP)
                            val t = Timer()
                            t.schedule(timerTask {
                                dialog.dismiss()
                                t.cancel()
                            },3000)
                        }
                        else {
                            val dialog = TopFalseDialog(this@TemplateActivity)
                            dialog.window.setDimAmount(0f)
                            dialog.setTitle("试剂名和试剂瓶容量未填写")
                            dialog.setMessage("请填写试剂名和试剂瓶容量")
                            dialog.show()
                            dialog.window.setGravity(Gravity.TOP)
                            val t = Timer()
                            t.schedule(timerTask {
                                dialog.dismiss()
                                t.cancel()
                            },3000)
                        }
                    }
                }
            })
            dialog.onNoOnclickListener(object :ImportDialog.onNoOnclickListener{
                override fun onNoClick() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.window.setGravity(Gravity.CENTER)
        }
    }

    override fun deletTemplateClick(text: String) {
        if(text=="delet_template")
        {
            val templateLine = VerticalFragment()
            val args = Bundle()
            args.putString("addtemplate","add")
            templateLine.arguments=args
            replaceFragment(templateLine,R.id.fl_template)
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{
            replace(frameId, fragment)
        }
    }
    fun templateToDB(filePath: String?): String {
        var ret = ""
        //打开文件
        if (filePath == null)
            return "导入模板失败"
        val file = File(filePath)
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory) {
            Log.e(filePath, "The File doesn't not exist.")
            ret = "导入模板失败"
        } else {
            try {
                val instream = FileInputStream(file)
                if (instream != null) {
                    val inputreader = InputStreamReader(instream)
                    val buffreader = BufferedReader(inputreader)
                    var line =buffreader.readLine()
                    var lineNumber = 1
                    var lineArray: Array<String>? = null
                    while ( line != null) {
                        line=buffreader.readLine()
                        Log.e("wzj",line)
                        if (lineNumber >= 1) {     //insert to DB
                            lineArray = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (lineArray[0] != null && lineArray[0] != "") {
                                if (lineArray[5] == "") lineArray[5] = "1"
                                dbManager?.addReagentTemplate(lineArray[0], lineArray[1], lineArray[2], lineArray[3], lineArray[4], lineArray[5].toInt(),
                                        lineArray[6], lineArray[7], lineArray[8], lineArray[9], lineArray[10], lineArray[11])
                            }
                        }
                        lineNumber++
                    }
                    instream.close()
                }
            } catch (e: Exception) {
                Log.e("TestFile", e.message)
                ret = "导入模板失败"
            }
        }
        return ret
    }

    fun downLoad() {                    //下载线程
        object : Thread() {
            override fun run() {
                val path = "SmartCabinet/ReagentTemplate"
                val fileName = scApp?.templateID + ".csv"
                val urlStr = SC_Const.REAGENTTEMPLATEADDRESS + fileName
                val SDCard = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + ""
                val pathName = "$SDCard/$path/$fileName"//文件存储路径
                if (File(pathName).exists()) {
                    templateToDB(pathName)
                    val msg = Message()
                    msg.what = 0
                    msg.obj = "更新成功"
                    mHandler.sendMessage(msg)
                }
                else
                {
                    try {
                        /*
                         * 通过URL取得HttpURLConnection
                         * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                         * <uses-permission android:name="android.permission.INTERNET" />
                         */
                        //取得inputStream，并将流中的信息写入SDCard

                        /*
                         * 写前准备
                         * 1.在AndroidMainfest.xml中进行权限配置
                         * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                         * 取得写入SDCard的权限
                         * 2.取得SDCard的路径： Environment.getExternalStorageDirectory()
                         * 3.检查要保存的文件上是否已经存在
                         * 4.不存在，新建文件夹，新建文件
                         * 5.将input流中的信息写入SDCard
                         * 6.关闭流
                         */
                        val status = Environment.getExternalStorageState()
                        if (status.equals(Environment.MEDIA_MOUNTED)) {
                            val main = Environment.getExternalStorageDirectory().getPath() + File.separator + "SmartCabinet/ReagentTemplate"
                            val destDir = File(main)
                            if (!destDir.exists()) {
                                destDir.mkdirs()
                            }
                            destDir.mkdirs()
                        }
                        Looper.prepare()

                        Toast.makeText(SCApp.getContext(), "开始下载试剂模板", Toast.LENGTH_LONG).show()
                        val dir = SDCard + "/" + path
                        File(dir).mkdir()//新建文件夹
                        val output = File(pathName)
                        val requestUrl = URL(urlStr)
                        output.writeBytes(requestUrl.readBytes())
                        Toast.makeText(SCApp.getContext(), "模板下载成功", Toast.LENGTH_SHORT).show()
                        if (templateToDB(pathName) == "") {
                            val dialog = TopFalseDialog(SCApp.getContext())
                            dialog.window.setDimAmount(0f)
                            dialog.setTitle("试剂模板导入失败")
                            dialog.setMessage(" ")
                            dialog.show()
                            dialog.window.setGravity(Gravity.TOP)
                            val t = Timer()
                            t.schedule(timerTask {
                                dialog.dismiss()
                                t.cancel()
                            },3000)
                        } else {
                            Toast.makeText(SCApp.getContext(), "试剂模板导入成功", Toast.LENGTH_SHORT).show()
                            val msg = Message()
                            msg.what = 0
                            msg.obj = "更新成功"
                            mHandler.sendMessage(msg)
                        }
                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        val dialog = TopFalseDialog(SCApp.getContext())
                        dialog.window.setDimAmount(0f)
                        dialog.setTitle("该试剂模板编码不存在")
                        dialog.setMessage("请输入正确的试剂模板编码")
                        dialog.show()
                        dialog.window.setGravity(Gravity.TOP)
                        val t = Timer()
                        t.schedule(timerTask {
                            dialog.dismiss()
                            t.cancel()
                        },3000)
                        e.printStackTrace()
                    } finally {
                        try {

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        Looper.loop()
                    }
                }
            }
        }.start()  //开启一个线程
    }

//    override fun onRestart() {
//        super.onRestart()
//        val templateLine = VerticalFragment()
//        val args = Bundle()
//        args.putString("addtemplate","add")
//        templateLine.arguments=args
//        replaceFragment(templateLine,R.id.fl_template)
//    }
}
