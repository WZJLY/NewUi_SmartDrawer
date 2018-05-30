package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_management.*

class ManagementActivity : AppCompatActivity(),UserLineFragment.deletbuttonlisten {
    private var dbManager:DBManager?=null
    private var userAccount: UserAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)
        val managementFragment = ManagementFragment()
        replaceFragment(R.id.fl_management,managementFragment)
        dbManager= DBManager(this)
        ib_management_back.setOnClickListener({
            finish()
        })
    }

    override fun deletButtonClick(text: String) {
        if(text == "deletperson") {
            val managementFragment = ManagementFragment()
            replaceFragment(R.id.fl_management, managementFragment)
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


}
