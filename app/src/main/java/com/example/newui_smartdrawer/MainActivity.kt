package com.example.newui_smartdrawer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SC_Const
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : BaseActivity() {
    private var scApp: SCApp? = null
    private var mbackKeyPressed = false
    private var dbManager: DBManager? = null
    private var userAccount: UserAccount? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val it = Intent(this@MainActivity, MyService::class.java)
//        startService(it)
        dbManager = DBManager(this)
        TimeThread().start()
        scApp = application as SCApp
        tv_mainAdmin_user.text = scApp!!.userInfo.userName
        val power = scApp!!.userInfo.userPower
        if (power == SC_Const.NORMAL) {
            btn_mainAdmin_setting.visibility = View.GONE
            btn_mainAdmin_management.visibility = View.GONE
        }
        btn_mainAdmin_setting.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, SettingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        btn_mainAdmin_record.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, RecordActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        btn_mainAdmin_management.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, ManagementActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        btn_mainAdmin_template.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, TemplateActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        btn_mainAdmin_back.setOnClickListener {
            if (!mbackKeyPressed) {
                Toast.makeText(this, "再按一次退出登陆", Toast.LENGTH_SHORT).show()
                mbackKeyPressed = true
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        mbackKeyPressed = false

                    }
                }, 2000)
            } else {
//                val it = Intent(this, MyService::class.java)
//                stopService(it)
                val intent = Intent()
                intent.setClass(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
        btn_mainAdmin_video.setOnClickListener {
            val parentFlie = File(Environment.getExternalStorageDirectory().toString())//+"/SmartCabint/Video/"
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setDataAndType(Uri.fromFile(parentFlie),"video/*")
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent,1)
        }
        ib_mainAdmin_operation.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, OperationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        ib_mainAdmin_search.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        ib_mainAdmin_user.setOnClickListener {
            val dialog = UserDialog(this)
            dialog.setEdit(dbManager!!.getUserAccountByUserName(scApp!!.userInfo.userName), 1)
            dialog.setYesOnclickListener("修改", object : UserDialog.onYesOnclickListener {
                override fun onYesClick() {
                    val etName = dialog.findViewById(R.id.et_Duser_account) as EditText
                    val etNum = dialog.findViewById(R.id.et_Duser_num) as EditText
                    val etAccount = dialog.findViewById(R.id.et_Duser_name) as EditText
                    val etPassword = dialog.findViewById(R.id.et_Duser_password) as EditText
                    val etPassword2 = dialog.findViewById(R.id.et_Duser_password2) as EditText
                    val etPhone = dialog.findViewById(R.id.et_Duser_phone) as EditText
                    val rgDuserLevel = dialog.findViewById(R.id.rg_Duser_level) as RadioGroup
                    val selectId = dialog.findViewById(rgDuserLevel.checkedRadioButtonId) as RadioButton
                    if (etName.length() == 0) {
                        val dialog = TopFalseDialog(this@MainActivity)
                        dialog.window.setDimAmount(0f)
                        dialog.setTitle("账号未填写")
                        dialog.setMessage("请填写账号")
                        dialog.show()
                        dialog.window.setGravity(Gravity.TOP)
                        val t = Timer()
                        t.schedule(timerTask {
                            dialog.dismiss()
                            t.cancel()
                        }, 3000)
                    } else if (etPassword.length() == 0) {
                        val dialog = TopFalseDialog(this@MainActivity)
                        dialog.window.setDimAmount(0f)
                        dialog.setTitle("密码未填写")
                        dialog.setMessage("请填写密码")
                        dialog.show()
                        dialog.window.setGravity(Gravity.TOP)
                        val t = Timer()
                        t.schedule(timerTask {
                            dialog.dismiss()
                            t.cancel()
                        }, 3000)
                    } else if (etPassword.length() != 0 && etPassword.text.toString() != etPassword2.text.toString()) {
                        val dialog = TopFalseDialog(this@MainActivity)
                        dialog.window.setDimAmount(0f)
                        dialog.setTitle("两次密码输入不同")
                        dialog.setMessage("请确认密码")
                        dialog.show()
                        dialog.window.setGravity(Gravity.TOP)
                        val t = Timer()
                        t.schedule(timerTask {
                            dialog.dismiss()
                            t.cancel()
                        }, 3000)
                    } else if (etAccount.length() == 0) {
                        val dialog = TopFalseDialog(this@MainActivity)
                        dialog.window.setDimAmount(0f)
                        dialog.setTitle("姓名未填写")
                        dialog.setMessage("请填写姓名")
                        dialog.show()
                        dialog.window.setGravity(Gravity.TOP)
                        val t = Timer()
                        t.schedule(timerTask {
                            dialog.dismiss()
                            t.cancel()
                        }, 3000)
                    } else if (etName.length() != 0 && etAccount.length() != 0 && etPassword.length() != 0 && etPassword.length() == etPassword2.length()) {
                        if (selectId.text == "管理员") {
                            dbManager?.updateAccountByUserName(etName.text.toString(), etNum.text.toString(), etPassword.text.toString(),
                                    0, etAccount.text.toString(), etPhone.text.toString())

                        } else if (selectId.text == "普通用户") {
                            dbManager?.updateAccountByUserName(etName.text.toString(), etNum.text.toString(), etPassword.text.toString(),
                                    1, etAccount.text.toString(), etPhone.text.toString())
                        }
                        dialog.dismiss()
                    }
                }
            })
            dialog.setNoOnclickListener("取消", object : UserDialog.onNoOnclickListener {
                override fun onNoClick() {
                    dialog.dismiss()
                }
            })
            dialog.show()
            dialog.window.setGravity(Gravity.CENTER)
        }

    }

    override fun onStart() {
        super.onStart()
        val power = scApp!!.userInfo.userPower
        if (power == SC_Const.NORMAL) {
            btn_mainAdmin_setting.visibility = View.GONE
            btn_mainAdmin_management.visibility = View.GONE

        } else {
            btn_mainAdmin_setting.visibility = View.VISIBLE
            btn_mainAdmin_management.visibility = View.VISIBLE
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
                    var line = buffreader.readLine()
                    var lineNumber = 1
                    var lineArray: Array<String>? = null
                    while (line != null) {
                        line = buffreader.readLine()
//                        Log.e("wzj",line)
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
                } else {
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
                            val dialog = TopFalseDialog(this@MainActivity)
                            dialog.window.setDimAmount(0f)
                            dialog.setTitle("试剂模板导入失败")
                            dialog.setMessage(" ")
                            dialog.show()
                            dialog.window.setGravity(Gravity.TOP)
                            val t = Timer()
                            t.schedule(timerTask {
                                dialog.dismiss()
                                t.cancel()
                            }, 3000)
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
                        val dialog = TopFalseDialog(this@MainActivity)
                        dialog.window.setDimAmount(0f)
                        dialog.setTitle("该试剂模板编码不存在")
                        dialog.setMessage("请输入正确的试剂模板编码")
                        dialog.show()
                        dialog.window.setGravity(Gravity.TOP)
                        val t = Timer()
                        t.schedule(timerTask {
                            dialog.dismiss()
                            t.cancel()
                        }, 3000)
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

    internal inner class TimeThread : Thread() {
        override fun run() {
            do {
                try {

                    val msg = Message()
                    msg.what = 3  //消息(一个整型值)
                    mHandler.sendMessage(msg)
                    Thread.sleep(1000)// 每隔1秒发送一个msg给mHandler
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            } while (true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 1)
            {
                val uri:Uri = data!!.data
                Toast.makeText(this,"文件路径："+GetPathFromUri4kitkat.getPath(this,uri).toString(),Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(GetPathFromUri4kitkat.getPath(this,uri)), "video/*")
                startActivity(intent)
            }

        }
    }


    object GetPathFromUri4kitkat {

        @SuppressLint("NewApi")
        fun getPath(context: Context, uri: Uri): String? {


            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT


            // DocumentProvider

            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider

                if (isExternalStorageDocument(uri)) {

                    val docId = DocumentsContract.getDocumentId(uri)

                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    val type = split[0]



                    if ("primary".equals(type, ignoreCase = true)) {

                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]

                    }


                    // TODO handle non-primary volumes

                } else if (isDownloadsDocument(uri)) {


                    val id = DocumentsContract.getDocumentId(uri)

                    val contentUri = ContentUris.withAppendedId(

                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))



                    return getDataColumn(context, contentUri, null, null)

                } else if (isMediaDocument(uri)) {

                    val docId = DocumentsContract.getDocumentId(uri)

                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    val type = split[0]


                    var contentUri: Uri? = null

                    if ("image" == type) {

                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    } else if ("video" == type) {

                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

                    } else if ("audio" == type) {

                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

                    }


                    val selection = "_id=?"

                    val selectionArgs = arrayOf(split[1])



                    return getDataColumn(context, contentUri, selection, selectionArgs)

                }// MediaProvider
                // DownloadsProvider

            } else if ("content".equals(uri.scheme, ignoreCase = true)) {

                return getDataColumn(context, uri, null, null)

            } else if ("file".equals(uri.scheme, ignoreCase = true)) {

                return uri.path

            }// File
            // MediaStore (and general)


            return null

        }


        /**
         *
         * Get the value of the data column for this Uri. This is useful for
         *
         * MediaStore Uris, and other file-based ContentProviders.
         *
         *
         *
         * @param context
         *
         * The context.
         *
         * @param uri
         *
         * The Uri to query.
         *
         * @param selection
         *
         * (Optional) Filter used in the query.
         *
         * @param selectionArgs
         *
         * (Optional) Selection arguments used in the query.
         *
         * @return The value of the _data column, which is typically a file path.
         */

        fun getDataColumn(context: Context, uri: Uri?, selection: String?,

                          selectionArgs: Array<String>?): String? {


            var cursor: Cursor? = null

            val column = "_data"

            val projection = arrayOf(column)



            try {

                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)

                if (cursor != null && cursor!!.moveToFirst()) {

                    val column_index = cursor!!.getColumnIndexOrThrow(column)

                    return cursor!!.getString(column_index)

                }

            } finally {

                if (cursor != null)

                    cursor!!.close()

            }

            return null

        }


        /**
         *
         * @param uri
         *
         * The Uri to check.
         *
         * @return Whether the Uri authority is ExternalStorageProvider.
         */

        fun isExternalStorageDocument(uri: Uri): Boolean {

            return "com.android.externalstorage.documents" == uri.authority

        }


        /**
         *
         * @param uri
         *
         * The Uri to check.
         *
         * @return Whether the Uri authority is DownloadsProvider.
         */

        fun isDownloadsDocument(uri: Uri): Boolean {

            return "com.android.providers.downloads.documents" == uri.authority

        }


        /**
         *
         * @param uri
         *
         * The Uri to check.
         *
         * @return Whether the Uri authority is MediaProvider.
         */

        fun isMediaDocument(uri: Uri): Boolean {

            return "com.android.providers.media.documents" == uri.authority

        }

    }


    //标题栏时间显示
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    //在这里得到数据，并且可以直接更新UI
                    val data = msg.obj as String

                }
                2 -> {
                    val data = msg.obj as String
                }
                3 -> {
                    val formatter = SimpleDateFormat("HH:mm")
                    val curDate = Date(System.currentTimeMillis())
                    val str = formatter.format(curDate)
                    tv_mainAdmin_time.text = str
                }

            }
        }
    }

}

