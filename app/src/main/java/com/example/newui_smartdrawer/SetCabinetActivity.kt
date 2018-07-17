package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.activity_set_cabinet.*

class SetCabinetActivity : BaseActivity(),SetCabinetFragment.addDrawerbuttonlisten,DrawerFragment.deletDrawerlisten {
    private var dbManager:DBManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_cabinet)
        dbManager = DBManager(this)
        val setCabinetFragment = SetCabinetFragment()
        replaceFragment(setCabinetFragment,R.id.fl_setCabinet)

      ib_setCabinet_back.setOnClickListener{
          finish()
          overridePendingTransition(0, 0)
      }
    }

    override fun addDrawerButtonClick(text: String) {

        if(text == "addDrawer")
        {
            val sum = dbManager!!.drawers.size+1
            Log.d("drawerID",sum.toString())
            finish()
            val intent = Intent()
            intent.setClass(this@SetCabinetActivity,SetDrawerActivity::class.java)
            intent.putExtra("drawerId",sum.toString())
            startActivityForResult(intent,1)
            overridePendingTransition(0, 0)
        }
    }

    override fun deletDrawerClick(text: String) {
        if(text == "deletDrawer")
        {
            val setCabinetFragment = SetCabinetFragment()
            replaceFragment(setCabinetFragment,R.id.fl_setCabinet)
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

}
