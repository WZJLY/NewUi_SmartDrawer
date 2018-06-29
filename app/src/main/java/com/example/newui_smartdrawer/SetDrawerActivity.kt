package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Gravity
import android.widget.RadioGroup
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.activity_set_drawer.*
import java.util.*
import kotlin.concurrent.timerTask

class SetDrawerActivity : AppCompatActivity() {
    private var num = -1
    private var status = -1 //0-正常使用，1-暂时停用
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
            tv_setDrawer_num.setText("柜1-抽屉"+set_drawerId)
            val drawer =  dbManager?.getDrawerByDrawerId(set_drawerId.toInt(),1)
            val tableFragment = TableFragment()
            val args = Bundle()
            args.putString("table","setDrawer")
            when(drawer?.drawerSize)
            {
                3-> {
                    radioGroup2.check(R.id.rb_setDrawer_3)
                    num=3
                    args.putInt("tableNum",3)
                }
                4-> {
                    radioGroup2.check(R.id.rb_setDrawer_4)
                    num=4
                    args.putInt("tableNum",4)
                }
                5-> {
                    radioGroup2.check(R.id.rb_setDrawer_5)
                    num=5
                    args.putInt("tableNum",5)
                }
                6-> {
                    radioGroup2.check(R.id.rb_setDrawer_6)
                    num=6
                    args.putInt("tableNum",6)
                }
                7-> {
                    radioGroup2.check(R.id.rb_setDrawer_7)
                    num = 7
                    args.putInt("tableNum",7)
                }
            }
            tableFragment.arguments = args
            replaceFragment(tableFragment,R.id.fl_setDrawer_table)
            when(drawer?.statue) {
                "0"-> {
                    status=0
                    drawerStatusGrroup.check(R.id.rb_setDrawer_use)
                }
                "1"-> {
                    status=1
                    drawerStatusGrroup.check(R.id.rb_setDrawer_nouse)
                }

            }

        }
        radioGroup2.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val tableFragment = TableFragment()
            val args = Bundle()
            args.putString("table","setDrawer")
            when(checkedId)
            {
                rb_setDrawer_3.id ->{
                    num = 3
                    args.putInt("tableNum",3)
                }
                rb_setDrawer_4.id ->{
                    num =4
                    args.putInt("tableNum",4)
                }
                rb_setDrawer_5.id ->{
                    num=5
                    args.putInt("tableNum",5)
                }
                rb_setDrawer_6.id ->{
                    num=6
                    args.putInt("tableNum",6)
                }
                rb_setDrawer_7.id ->{
                    num=7
                    args.putInt("tableNum",7)
                }
            }
            tableFragment.arguments = args
            replaceFragment(tableFragment,R.id.fl_setDrawer_table)
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
            if(num == -1&&status== -1) {
                val dialog = TopFalseDialog(this)
                dialog.window.setDimAmount(0f)
                dialog.setTitle("行列数和抽屉状态未选择")
                dialog.setMessage("请选择行列数和抽屉状态")
                dialog.show()
                dialog.window.setGravity(Gravity.TOP)
                val t = Timer()
                t.schedule(timerTask {
                    dialog.dismiss()
                    t.cancel()
                },3000)
            } else if (num == -1) {
                val dialog = TopFalseDialog(this)
                dialog.window.setDimAmount(0f)
                dialog.setTitle("行列数未选择")
                dialog.setMessage("请选择行列数")
                dialog.show()
                dialog.window.setGravity(Gravity.TOP)
                val t = Timer()
                t.schedule(timerTask {
                    dialog.dismiss()
                    t.cancel()
                },3000)
            }else if(status== -1){
                val dialog = TopFalseDialog(this)
                dialog.window.setDimAmount(0f)
                dialog.setTitle("抽屉状态未选择")
                dialog.setMessage("请选择抽屉状态")
                dialog.show()
                dialog.window.setGravity(Gravity.TOP)
                val t = Timer()
                t.schedule(timerTask {
                    dialog.dismiss()
                    t.cancel()
                },3000)
            } else {
                if(set_drawerId!=null) dbManager?.updateDrawer(set_drawerId.toInt(),1,num,status.toString())
                else dbManager?.addDrawer(drawerId.toInt(),1,num,status.toString())
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
