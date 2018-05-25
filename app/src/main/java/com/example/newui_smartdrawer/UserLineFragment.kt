package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.R.id.tv_FuserLine_Name
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SC_Const
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.fragment_user_line.*


class UserLineFragment : Fragment() {
    var userName:String?=null
    var dbManager:DBManager?=null
    var user : UserAccount?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_line, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context)
        if(arguments!=null)
        {
                userName = arguments.getString("userName")
        }
        user = dbManager?.getUserAccountByUserName(userName)
        tv_FuserLine_Name.text=user?.userAccount
        if(user?.userPower==SC_Const.ADMIN)
        tv_FuserLine_user.text="管理员"


        ib_FuserLine_del.setOnClickListener({




        })
    }


}
