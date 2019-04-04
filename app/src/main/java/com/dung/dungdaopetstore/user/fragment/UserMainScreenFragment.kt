package com.dung.dungdaopetstore.user.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.user.userbuy.UserBuyActivity
import com.dung.dungdaopetstore.user.usercommunity.UserCommunityActivity
import com.dung.dungdaopetstore.user.userorder.UserOrderActivity
import com.dung.dungdaopetstore.user.usersell.UserSellPetActivity
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
        startViewAnim()
        onClickView()

        return rootview

    }

    // Start animation for Icon
    private fun startViewAnim() {
        setViewAnimation(cvMCMarket)
        setViewAnimation(cvMCSell)
        setViewAnimation(cvMCOrder)
        setViewAnimation(cvMCSocial)
        setViewAnimation(cvMCLocation)
    }


    // Function on click for all Icon
    private fun onClickView() {

        cvMCMarket.setOnClickListener { startActivity(Intent(context, UserBuyActivity::class.java)) }
        cvMCSell.setOnClickListener { startActivity(Intent(context, UserSellPetActivity::class.java)) }
        cvMCOrder.setOnClickListener { startActivity(Intent(context, UserOrderActivity::class.java)) }
        cvMCSocial.setOnClickListener { startActivity(Intent(context, UserCommunityActivity::class.java)) }
        cvMCLocation.setOnClickListener {}

    }

    // init all View and Class
    fun initView(){
        var anim = AnimationUtils.loadAnimation(context,R.anim.anim_main_screen)
        cvMCMarket = rootview.cvMCMarket
        cvMCOrder = rootview.cvMCOrder
        cvMCSell = rootview.cvMCSell
        cvMCLocation = rootview.cvMCLocation
        cvMCSocial = rootview.cvMCSocial

//        cvMCMarket.startAnimation(anim)
//        cvMCOrder.startAnimation(anim)
//        cvMCSell.startAnimation(anim)
//        cvMCLocation.startAnimation(anim)
//        cvMCSocial.startAnimation(anim)

    }

}