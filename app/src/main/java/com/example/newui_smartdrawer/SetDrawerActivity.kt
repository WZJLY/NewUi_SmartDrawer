package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_set_drawer.*

class SetDrawerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_drawer)
        val intent = Intent()
        val drawerId = intent.getStringExtra("drawerId")
        tv_setDrawer_num.setText("柜1-抽屉"+drawerId)

        radioGroup2.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
           when(checkedId)
           {
               rb_setDrawer_3.id ->{
                   val tableFragment = TableFragment()
                   val args = Bundle()
                   args.putInt("tableNum",3)
                   tableFragment.arguments = args
                   replaceFragment(tableFragment,R.id.fl_setDrawer_table)
               }
               rb_setDrawer_4.id ->{
                   val tableFragment = TableFragment()
                   val args = Bundle()
                   args.putInt("tableNum",4)
                   tableFragment.arguments = args
                   replaceFragment(tableFragment,R.id.fl_setDrawer_table)
               }
               rb_setDrawer_5.id ->{
                   val tableFragment = TableFragment()
                   val args = Bundle()
                   args.putInt("tableNum",5)
                   tableFragment.arguments = args
                   replaceFragment(tableFragment,R.id.fl_setDrawer_table)
               }
               rb_setDrawer_6.id ->{
                   val tableFragment = TableFragment()
                   val args = Bundle()
                   args.putInt("tableNum",6)
                   tableFragment.arguments = args
                   replaceFragment(tableFragment,R.id.fl_setDrawer_table)
               }
               rb_setDrawer_7.id ->{
                   val tableFragment = TableFragment()
                   val args = Bundle()
                   args.putInt("tableNum",7)
                   tableFragment.arguments = args
                   replaceFragment(tableFragment,R.id.fl_setDrawer_table)
               }




           }



        })
        ib_setDrawer_back.setOnClickListener({
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
