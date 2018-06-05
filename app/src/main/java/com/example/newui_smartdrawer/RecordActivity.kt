package com.example.newui_smartdrawer

import android.icu.text.AlphabeticIndex
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.OperationRecord
import com.example.newui_smartdrawer.util.ReagentUserRecord
import kotlinx.android.synthetic.main.activity_record.*

class RecordActivity : AppCompatActivity() {
    private var dbManager:DBManager?=null
    private var record:ReagentUserRecord?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        dbManager= DBManager(this)

        val arrayListRecord = dbManager?.reagentUseRecord
        val sum = arrayListRecord!!.size

        if(sum>0)
        {
           for(m in 1..sum)
            {
                record = arrayListRecord[m-1]
                val recordLineFragment = RecordFragment()
                val args = Bundle()
                args.putString("date", record?.operationTime)
                recordLineFragment.arguments = args
                addFragment(recordLineFragment,R.id.ll_record)
            }

        }




        ib_record_back.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{
            add(frameId, fragment)
        }
    }
}
