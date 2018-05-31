package com.example.newui_smartdrawer

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SC_Const
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_login.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL

class LoginActivity : AppCompatActivity() {
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    private val LOGINNAME = "smart_cabinet)smart_cabinet_login_name"
    private val NAME = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dbManager= DBManager(this)
        dbManager?.tableUpgrade()
        scApp = application as SCApp
        val lastname = getLastLoginName()
        account.setText(lastname)
        loginButton.setOnClickListener {

            login(account.text.toString(),password.text.toString())
        }
        delet_Button.setOnClickListener({
            password.setText("")
        })

    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(null != this.currentFocus) {
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
                val account = UserAccount(strUserId, strAccount, strPassword, iPower,"admin","","0")
                dbManager?.addAccount(account)
                scApp?.setUserInfo(strUserId, strAccount, strPassword, iPower,"admin","","0")
                saveUserName(strAccount)

                intent.setClass(this,MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)

            } else {
                Toast.makeText(this.applicationContext,"登陆失败", Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (!dbManager!!.isAccountExist(userName, userPWD)) {
            Toast.makeText(this.applicationContext, "登陆失败", Toast.LENGTH_SHORT).show()
        } else {
            val userInfo = dbManager?.getUserAccount(userName, userPWD)
            scApp?.userInfo = userInfo

            //upload user login record to server


            if(userInfo?.statue!="1") {
                intent.setClass(this,MainActivity::class.java)
                startActivity(intent)
                saveUserName(userName)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            else
                Toast.makeText(this,"该用户已被禁用", Toast.LENGTH_SHORT).show()
//            finish()

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




}
