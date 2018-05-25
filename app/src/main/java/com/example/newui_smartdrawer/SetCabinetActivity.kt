package com.example.newui_smartdrawer

import android.content.Intent
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
        ib_setCabinet_addDrawer.setOnClickListener({

            val intent = Intent()
            intent.setClass(this,SetDrawerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)

        })
        ib_setCabinet_back.setOnClickListener({

            finish()
            overridePendingTransition(0, 0)
        })

        ib_setCabinet_cabinet.setOnClickListener({
            val cabinetFragment = CabinetFragment()
            addFragment(R.id.drawer_area,cabinetFragment)

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

}
