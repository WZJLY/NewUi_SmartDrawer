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

class SetCabinetActivity : BaseActivity(),DrawerFragment.deletDrawerlisten,CabinetFragment.deletCabinetlisten {
    private var dbManager:DBManager?=null
    private var scApp:SCApp?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_cabinet)
        dbManager = DBManager(this)
        scApp = application as SCApp
        val cabinetFragment = HorizontalFragment()
        val arg=Bundle()
        arg.putString("cabinet","set")
        cabinetFragment.arguments=arg
        replaceFragment(cabinetFragment,R.id.fl_setCabinet_cabinet)
        val drawerFragment = VerticalFragment()
        val args = Bundle()
        args.putString("drawer", "addDrawer")
        drawerFragment.arguments=args
        replaceFragment(drawerFragment,R.id.fl_setCabinet_drawer)
          ib_setCabinet_back.setOnClickListener{
              finish()
              overridePendingTransition(0, 0)
          }
        ib_setCabinet_cabinet.setOnClickListener{
            val sum = dbManager!!.boxes.size+1
            dbManager?.addBox(sum)
            val cabinetFragment1 = HorizontalFragment()
            val arg3=Bundle()
            arg3.putString("cabinet","set")
            cabinetFragment1.arguments=arg3
            replaceFragment(cabinetFragment1,R.id.fl_setCabinet_cabinet)
        }

        ib_setCabinet_addDrawer.setOnClickListener {
            val sum = dbManager!!.getDrawersByboxID(scApp?.boxId.toString()).size+1
            Log.d("drawerID",sum.toString())
            finish()
            val intent = Intent()
            intent.setClass(this@SetCabinetActivity,SetDrawerActivity::class.java)
            intent.putExtra("drawerId",sum.toString())
            startActivityForResult(intent,1)
            overridePendingTransition(0, 0)
        }
    }

    override fun deletCabinetClick(text: String) {
       when(text)
       {
           "deletCabinet" ->
           {
               val cabinetFragment = HorizontalFragment()
               val arg = Bundle()
               arg.putString("cabinet","set")
               cabinetFragment.arguments=arg
               replaceFragment(cabinetFragment,R.id.fl_setCabinet_cabinet)
               val drawerFragment = VerticalFragment()
               replaceFragment(drawerFragment,R.id.fl_setCabinet_drawer)
           }
           "updatedrawer" ->
           {
               val drawerFragment = VerticalFragment()
               val args = Bundle()
               args.putString("drawer", "addDrawer")
               drawerFragment.arguments=args
               replaceFragment(drawerFragment,R.id.fl_setCabinet_drawer)
               val cabinetFragment = HorizontalFragment()
               val arg = Bundle()
               arg.putString("cabinet","set")
               cabinetFragment.arguments=arg
               replaceFragment(cabinetFragment,R.id.fl_setCabinet_cabinet)
           }
       }
    }


//    override fun addDrawerButtonClick(text: String) {
//        if(text == "addDrawer")
//        {
//            val sum = dbManager!!.drawers.size+1
//            Log.d("drawerID",sum.toString())
//            finish()
//            val intent = Intent()
//            intent.setClass(this@SetCabinetActivity,SetDrawerActivity::class.java)
//            intent.putExtra("drawerId",sum.toString())
//            startActivityForResult(intent,1)
//            overridePendingTransition(0, 0)
//        }
//    }


    override fun deletDrawerClick(text: String) {
        if(text == "deletDrawer")
        {
            val drawerFragment = VerticalFragment()
            val args = Bundle()
            args.putString("drawer","addDrawer")
            drawerFragment.arguments=args
            replaceFragment(drawerFragment,R.id.fl_setCabinet_drawer)
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
    fun AppCompatActivity.removeFragment(fragment: Fragment){
        supportFragmentManager.inTransaction {
            remove(fragment)
        }
    }

}
