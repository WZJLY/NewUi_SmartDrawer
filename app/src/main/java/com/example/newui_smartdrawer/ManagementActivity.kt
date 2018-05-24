package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_management.*

class ManagementActivity : AppCompatActivity() {
    private var dbManager:DBManager?=null
    private var userAccount: UserAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)
        dbManager= DBManager(this)
        updateUser()
        ib_management_back.setOnClickListener({

            finish()
        })
        ib_management_add.setOnClickListener({

            val userLineFragment:Fragment = UserLineFragment()
            addFragment(R.id.ll_management_admin,userLineFragment)
        })
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
    fun updateUser()
    {
        val arrayList = dbManager?.users
        val sum = arrayList!!.size
        for(i in 1..sum)
        {
            userAccount = arrayList?.get(i-1)
            if(userAccount?.userPower==0) {
                val userLineFragment = UserLineFragment()
                val args = Bundle()
                args.putString("userName", userAccount!!.userName.toString())
                var statue = userAccount!!.getStatue()
                if(statue == null)
                    statue = "0"
                args.putString("statue",statue)
                userLineFragment.arguments = args
                addFragment(R.id.ll_management_admin,userLineFragment)
            }
        }//通过遍历用户的数据表对片断进行添加
        for(i in 1..sum)
        {
            userAccount = arrayList?.get(i-1)
            if(userAccount?.userPower==1) {
                val userLineFragment = UserLineFragment()
                val args = Bundle()
                args.putString("userName", userAccount!!.userName.toString())
                var statue = userAccount!!.getStatue()
                if(statue == null)
                    statue = "0"
                args.putString("statue",statue)
                userLineFragment.arguments = args
                addFragment(R.id.ll_management_user,userLineFragment)
            }
        }//通过遍历用户的数据表对片断进行添加
    }
}
