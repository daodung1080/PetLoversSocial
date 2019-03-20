package com.dung.dungdaopetstore.user.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.user.useraddpet.UserPetListActivity
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.UserDatabase
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

class UserProfileFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var imgProfileUser: ImageView
    lateinit var txtProfileUsername: TextView
    lateinit var txtProfileMoney: TextView
    lateinit var txtProfileFullname: TextView
    lateinit var txtProfilePhone: TextView
    lateinit var txtProfileTradeTime: TextView
    lateinit var userDatabase: UserDatabase
    lateinit var rUsername: String
    lateinit var cvProfileAddPet: CardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_user_profile, container, false)

        initView()
        getUserInformation()
        selectFunction()

        return rootview

    }

    private fun selectFunction() {
        cvProfileAddPet.setOnClickListener {
            startActivity(Intent(context, UserPetListActivity::class.java))
        }
    }

    private fun getUserInformation() {
        userDatabase.getUserInformation(rUsername,txtProfileUsername,imgProfileUser,txtProfileMoney,
            txtProfileFullname,txtProfilePhone,txtProfileTradeTime,context!!.resources.getString(R.string.txtTimes))
    }

    private fun initView() {
        rUsername = getRootUsername()
        userDatabase = UserDatabase(context!!)
        imgProfileUser = rootview.imgProfleUser
        txtProfileUsername = rootview.txtProfileUsername
        txtProfileMoney = rootview.txtProfileMoney
        txtProfileFullname = rootview.txtProfileFullname
        txtProfilePhone = rootview.txtProfilePhone
        txtProfileTradeTime = rootview.txtProfileTradeTime
        cvProfileAddPet = rootview.cvProfleAddPet
    }

}