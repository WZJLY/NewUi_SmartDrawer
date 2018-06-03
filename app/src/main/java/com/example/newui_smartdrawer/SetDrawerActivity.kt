package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.activity_set_drawer.*

class SetDrawerActivity : AppCompatActivity() {
    private var num = -1
    private var status = -1 //0-未点击，1-正常使用，2-暂时停用
    private var dbManager:DBManager?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_drawer)
        dbManager = DBManager(this)
        val intent = intent
        val drawerId  = intent.getStringExtra("drawerId")
        val set_drawerId = intent.getStringExtra("set_drawerId")
        tv_setDrawer_num.setText("柜1-抽屉"+drawerId)
        if(set_drawerId!=null)
        {
           val drawer =  dbManager?.getDrawerByDrawerId(set_drawerId.toInt(),1)
            when(drawer?.drawerSize)
            {
                3->
                {
                    radioGroup2.check(R.id.rb_setDrawer_3)
                    num=3
                    val tableFragment = TableFragment()
                    val args = Bundle()
                    args.putInt("tableNum",3)
                    args.putString("status","set")
                    tableFragment.arguments = args
                    replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                }
                4->
                {
                    radioGroup2.check(R.id.rb_setDrawer_4)
                    num=4
                    val tableFragment = TableFragment()
                    val args = Bundle()
                    args.putInt("tableNum",4)
                    args.putString("status","set")
                    tableFragment.arguments = args
                    replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                }
                5->
                {
                    radioGroup2.check(R.id.rb_setDrawer_5)
                    num=5
                    val tableFragment = TableFragment()
                    val args = Bundle()
                    args.putInt("tableNum",5)
                    args.putString("status","set")
                    tableFragment.arguments = args
                    replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                }
                6->
                {
                    radioGroup2.check(R.id.rb_setDrawer_6)
                    num=6
                    val tableFragment = TableFragment()
                    val args = Bundle()
                    args.putInt("tableNum",6)
                    args.putString("status","set")
                    tableFragment.arguments = args
                    replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                }
                7->
                {
                    radioGroup2.check(R.id.rb_setDrawer_7)
                    num = 7
                    val tableFragment = TableFragment()
                    val args = Bundle()
                    args.putInt("tableNum",7)
                    args.putString("status","set")
                    tableFragment.arguments = args
                    replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                }

            }
            when(drawer?.statue)
            {
                "0"->
                {
                    drawerStatusGrroup.check(R.id.rb_setDrawer_use)
                }
                "1"->
                {
                    drawerStatusGrroup.check(R.id.rb_setDrawer_nouse)
                }

            }

        }
        radioGroup2.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
               when(checkedId)
               {
                   rb_setDrawer_3.id ->{
                       num = 3
                       val tableFragment = TableFragment()
                       val args = Bundle()
                       args.putInt("tableNum",3)
                       args.putString("status","set")
                       tableFragment.arguments = args
                       replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                   }
                   rb_setDrawer_4.id ->{
                       num =4
                       val tableFragment = TableFragment()
                       val args = Bundle()
                       args.putInt("tableNum",4)
                       args.putString("status","set")
                       tableFragment.arguments = args
                       replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                   }
                   rb_setDrawer_5.id ->{
                       num=5
                       val tableFragment = TableFragment()
                       val args = Bundle()
                       args.putInt("tableNum",5)
                       args.putString("status","set")
                       tableFragment.arguments = args
                       replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                   }
                   rb_setDrawer_6.id ->{
                       num=6
                       val tableFragment = TableFragment()
                       val args = Bundle()
                       args.putInt("tableNum",6)
                       args.putString("status","set")
                       tableFragment.arguments = args
                       replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                   }
                   rb_setDrawer_7.id ->{
                       num=7
                       val tableFragment = TableFragment()
                       val args = Bundle()
                       args.putInt("tableNum",7)
                       args.putString("status","set")
                       tableFragment.arguments = args
                       replaceFragment(tableFragment,R.id.fl_setDrawer_table)
                   }




           }



        })
        drawerStatusGrroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when(checkedId)
            {
                rb_setDrawer_use.id ->{
                    status=0
                }
                rb_setDrawer_nouse.id ->{
                    status=1
                }


            }



        })
        ib_setDrawer_back.setOnClickListener({
            finish()
            overridePendingTransition(0, 0)
        })

        btn_setDrawer_OK.setOnClickListener({
            if(num == -1 )
                Toast.makeText(this,"行列数未选择",Toast.LENGTH_SHORT).show()
            if(status== -1)
                Toast.makeText(this,"抽屉状态未选择",Toast.LENGTH_SHORT).show()
            if(num!=-1&&status!=-1)
            {
                dbManager?.addDrawer(drawerId.toInt(),1,num,status.toString())
                finish()
                val intent = Intent()
                intent.setClass(this,SetCabinetActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
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
