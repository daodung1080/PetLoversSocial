package com.dung.dungdaopetstore.user.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.user.userbuy.UserBuyActivity
import com.dung.dungdaopetstore.user.usercommunity.UserCommunityActivity
import com.dung.dungdaopetstore.user.userorder.UserOrderActivity
import com.dung.dungdaopetstore.user.useraddpet.UserAddPetActivity
import kotlinx.android.synthetic.main.fragment_user_main_screen.view.*

class UserMainScreenFragment: BaseFragment() {

    lateinit var rootview: View

    lateinit var cvMCMarket: CardView
    lateinit var cvMCOrder: CardView
    lateinit var cvMCSell: CardView
    lateinit var cvMCLocation: CardView
    lateinit var cvMCSocial: CardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_user_main_screen, container, false)

        initView()
        onClickView()

        return rootview

    }

    private fun onClickView() {
        cvMCMarket.setOnClickListener { startActivity(Intent(context, UserBuyActivity::class.java)) }
        cvMCOrder.setOnClickListener { startActivity(Intent(context, UserOrderActivity::class.java)) }
        cvMCSocial.setOnClickListener { startActivity(Intent(context, UserCommunityActivity::class.java)) }
    }

    fun initView(){
        cvMCMarket = rootview.cvMCMarket
        cvMCOrder = rootview.cvMCOrder
        cvMCSell = rootview.cvMCSell
        cvMCLocation = rootview.cvMCLocation
        cvMCSocial = rootview.cvMCSocial
    }

}