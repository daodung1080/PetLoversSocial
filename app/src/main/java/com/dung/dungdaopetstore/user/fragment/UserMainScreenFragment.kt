package com.dung.dungdaopetstore.user.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.user.userbuy.UserBuyActivity
import com.dung.dungdaopetstore.user.usercommunity.UserCommunityActivity
import com.dung.dungdaopetstore.user.userorder.UserOrderActivity
import com.dung.dungdaopetstore.user.usersell.UserSellActivity
import kotlinx.android.synthetic.main.fragment_user_main_screen.view.*

class UserMainScreenFragment: BaseFragment() {

    lateinit var rootview: View

    lateinit var imgUserBuy: ImageView
    lateinit var imgUserOrder: ImageView
    lateinit var imgUserTrade: ImageView
    lateinit var imgUserLocation: ImageView
    lateinit var imgUserCommunity: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_user_main_screen, container, false)

        initView()
        onClickView()

        return rootview

    }

    private fun onClickView() {
        imgUserBuy.setOnClickListener {
            startActivity(Intent(context, UserBuyActivity::class.java))
        }
        imgUserOrder.setOnClickListener { startActivity(Intent(context, UserOrderActivity::class.java)) }
        imgUserTrade.setOnClickListener { startActivity(Intent(context, UserSellActivity::class.java)) }
        imgUserLocation.setOnClickListener {  }
        imgUserCommunity.setOnClickListener { startActivity(Intent(context, UserCommunityActivity::class.java)) }
    }

    fun initView(){
        imgUserBuy = rootview.imgUserBuy
        imgUserOrder = rootview.imgUserOrder
        imgUserTrade = rootview.imgUserTrade
        imgUserLocation = rootview.imgUserLocation
        imgUserCommunity = rootview.imgUserCommunity
    }

}