package com.example.newui_smartdrawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.widget.*
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SC_Const
import com.example.newui_smartdrawer.util.UploadRecordManager
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask

class LoginActivity :BaseActivity() {
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    private val LOGINNAME = "smart_cabinet)smart_cabinet_login_name"
    private val NAME = "name"
    private var msgList: MutableList<String>? = null
    private var popupWindow: PopupWindow? = null
    private var listView: ListView? = null
    //    private var service:MyService?=null
//    private var bound = false
//    private var sc = MyseriviceConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dbManager = DBManager(this)
        dbManager?.tableUpgrade()
        scApp = application as SCApp
//        val lastname = getLastLoginName()
//        account.setText(lastname)             //保存上一次登陆的用户
        msgList = ArrayList()
        val arrayListLoginUser = dbManager?.loginUser
        Log.d("wzj",""+arrayListLoginUser?.size)
        if(arrayListLoginUser!!.size > 0) {
            for (i in 1..arrayListLoginUser.size) {
                msgList?.add(arrayListLoginUser.get(i-1).loginUser)
            }
        }
        initListView()
        loginButton.setOnClickListener {
            login(account.text.toString(), password.text.toString())
        }
        delet_Button.setOnClickListener {
            password.setText("")
        }
        downUser_button.setOnClickListener {

            popupWindow = PopupWindow(this)
            popupWindow?.width = account.width
            popupWindow?.height = 200
            popupWindow?.contentView=listView
            popupWindow?.isOutsideTouchable = true
            popupWindow?.showAsDropDown(account,0,0)

        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (null != this.currentFocus) {
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0)
        }
        return super.onTouchEvent(event)
    }

    fun saveUserName(strUserName: String) {
        val sharedPreferences = getSharedPreferences(LOGINNAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(NAME, strUserName)
        editor.commit()
    }

    fun login(userName: String, userPWD: String) {
        val intent = Intent()
        if (userName == "admin" && !dbManager!!.isAccountExist(userName)) {
            //first time login with admin - admin/admin
            if (userPWD == "admin") {
                val strUserId = "00001"
                val strAccount = "admin"
                val strPassword = "admin"
                val iPower = SC_Const.ADMIN
                val account = UserAccount(strUserId, strAccount, strPassword, iPower, "admin", "", "0")
                dbManager?.addAccount(account)
                scApp?.setUserInfo(strUserId, strAccount, strPassword, iPower, "admin", "", "0")
                dbManager?.addLoginUser("admin")
                saveUserName(strAccount)
                intent.setClass(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                val dialog = TopFalseDialog(this)
                dialog.window.setDimAmount(0f)
                dialog.show()
                dialog.window.setGravity(Gravity.TOP)
                val t = Timer()
                t.schedule(timerTask {
                    dialog.dismiss()
                    t.cancel()
                }, 3000)
                return
            }
        }
        if (!dbManager!!.isAccountExist(userName, userPWD)) {
            val dialog = TopFalseDialog(this)
            dialog.window.setDimAmount(0f)
            dialog.show()
            dialog.window.setGravity(Gravity.TOP)
            val t = Timer(true)
            t.schedule(timerTask {
                dialog.dismiss()
                t.cancel()
            }, 3000)
        } else {
            val userInfo = dbManager?.getUserAccount(userName, userPWD)
            scApp?.userInfo = userInfo

            //upload user login record to server


            if (userInfo?.statue != "1") {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val curDate = Date(System.currentTimeMillis())
                val str = formatter.format(curDate)
                if (dbManager!!.cabinetNo.size != 0) {
                    val upload: UploadRecordManager = UploadRecordManager(this)
                    upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo, "登陆", scApp!!.userInfo.userName, str,
                "", "", "", "", "", "")

            }
//                val it = Intent(this, MyService::class.java)
//                startService(it)

                //当登入用户操过10位时，对之前的第一个登录用户进行删除
                val arrayListLoginUser1 = dbManager?.loginUser
                if(arrayListLoginUser1!!.size<10) {
                    dbManager?.addLoginUser(userName)
                }
                else
                {
                   dbManager?.deletLoginUser(arrayListLoginUser1.get(0).loginUser)
                    dbManager?.addLoginUser(userName)
                }

                intent.setClass(this, MainActivity::class.java)
                saveUserName(userName)
                startActivity(intent)
                overridePendingTransition(0, 0)

            } else {
                val dialog = TopFalseDialog(this)
                dialog.setTitle("该用户已被禁用")
                dialog.setMessage(" ")
                dialog.window.setDimAmount(0f)
                dialog.show()
                dialog.window.setGravity(Gravity.TOP)
                val t = Timer(true)
                t.schedule(timerTask {
                    dialog.dismiss()
                    t.cancel()
                }, 3000)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        password.text = null
    }

    @JavascriptInterface
    fun getLastLoginName(): String {
        val sharedPreferences = this.getSharedPreferences(LOGINNAME, MODE_PRIVATE)
        return sharedPreferences.getString(NAME, "")!! + ""
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager?.closeDB()
    }

    private fun initListView() {

        listView = ListView(this)

        listView!!.setBackgroundResource(R.drawable.listview_background)

        listView!!.divider = null

        listView!!.isVerticalScrollBarEnabled = false
        listView!!.adapter = MyListAdapter()
    }

    private inner class MyListAdapter : BaseAdapter() {

        override fun getCount(): Int {

            return msgList!!.size
        }

        override fun getItem(arg0: Int): Any? {

            return null
        }

        override fun getItemId(arg0: Int): Long {

            return 0
        }

        override fun getView(arg0: Int, arg1: View?, arg2: ViewGroup): View {
            var arg1 = arg1

            val houder: ViewHolder
            if (arg1 == null) {
                arg1 = View.inflate(applicationContext, R.layout.list_item, null)
                houder = ViewHolder()
                houder.tv_msgTextView = arg1!!.findViewById<View>(R.id.tv_list_item) as TextView
                arg1.tag = houder
            } else {
                houder = arg1.tag as ViewHolder
            }

            houder.tv_msgTextView!!.text = msgList!![arg0]

            arg1.setOnClickListener {
                account.setText(msgList!![arg0])


                popupWindow!!.dismiss()
            }
            return arg1
        }

    }

    private inner class ViewHolder {
        internal var tv_msgTextView: TextView? = null
        internal var deleteImageView: ImageView? = null
    }

}


