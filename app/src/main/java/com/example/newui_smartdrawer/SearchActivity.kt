package com.example.newui_smartdrawer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.text.Editable
import android.text.TextWatcher
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.Reagent
import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SearchActivity : BaseActivity(){
    private var dbManager:DBManager?=null
    private var scApp:SCApp?=null
    private var reagent: Reagent?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (!EventBus.getDefault().hasSubscriberForEvent(SearchActivity::class.java)) {
            EventBus.getDefault().register(this)
        }

        scApp = application as SCApp
        dbManager = DBManager(this)

        val verticalFragment = VerticalFragment()
        val args = Bundle()
        args.putString("addreagent","addreagent")
        verticalFragment.arguments=args
        replaceFragment(verticalFragment,R.id.reagent_area)

        ib_search_back.setOnClickListener{
            finish()
           overridePendingTransition(0, 0)
        }
        et_search_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {



            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) { //屏蔽回车 中英文空格
                if(s.length!=0)
                {
                    val verticalFragment = VerticalFragment()
                    val args = Bundle()
                    args.putString("addreagent","addreagent")
                    args.putString("serach",s.toString())
                    verticalFragment.arguments=args
                    replaceFragment(verticalFragment,R.id.reagent_area)
                }

            }
        })

    }
    @Subscribe
    fun onEvent(event: SerachEvent){
        updateButton(event.getMsg())
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction {
            replace(frameId, fragment)
        }

    }
    fun updateButton(text:String)
    {
        if(text=="update")
        {
            finish()
            overridePendingTransition(0,0)
            val intent = Intent()
            intent.setClass(this,OperationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }


    }
}
