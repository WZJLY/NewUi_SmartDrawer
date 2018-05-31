package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_management.*

class ManagementFragment : Fragment() {
    private var dbManager: DBManager?=null
    private var userAccount: UserAccount? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_management, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context)
        updateUser()
    }

    fun updateUser()
    {
        val arrayList = dbManager?.users
        val sum = arrayList!!.size
        for(i in 1..sum)
        {
            userAccount = arrayList?.get(i-1)
            if(userAccount?.userPower==0) {
                val userLineFragment = UserLineFragment()
                val args = Bundle()
                args.putString("userName", userAccount!!.userName.toString())
                var statue = userAccount!!.getStatue()
                if(statue == null)
                    statue = "0"
                args.putString("statue",statue)
                userLineFragment.arguments = args
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.ll_management_admin,userLineFragment)
                fragmentTransaction.commit()
            }
        }//通过遍历用户的数据表对片断进行添加
        for(i in 1..sum)
        {
            userAccount = arrayList?.get(i-1)
            if(userAccount?.userPower==1) {
                val userLineFragment = UserLineFragment()
                val args = Bundle()
                args.putString("userName", userAccount!!.userName.toString())
                var statue = userAccount!!.getStatue()
                if(statue == null)
                    statue = "0"
                args.putString("statue",statue)
                userLineFragment.arguments = args
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.ll_management_user,userLineFragment)
                fragmentTransaction.commit()
            }
        }//通过遍历用户的数据表对片断进行添加
    }
}
