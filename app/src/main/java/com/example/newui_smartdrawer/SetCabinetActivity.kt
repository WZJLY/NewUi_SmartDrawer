package com.example.newui_smartdrawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_set_cabinet.*

class SetCabinetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_cabinet)
        val setCabinetFragment = SetCabinetFragment()
        val args = Bundle()
//        args.putString("set")
        replaceFragment(setCabinetFragment,R.id.fl_setCabinet)
      ib_setCabinet_back.setOnClickListener({
          finish()
          overridePendingTransition(0, 0)
      })
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
