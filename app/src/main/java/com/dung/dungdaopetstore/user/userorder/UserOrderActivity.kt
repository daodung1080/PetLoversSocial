package com.dung.dungdaopetstore.user.userorder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.ViewPagerUserOrderAdapter
import kotlinx.android.synthetic.main.activity_user_order.*

class UserOrderActivity : AppCompatActivity() {

    lateinit var adapterViewPager: ViewPagerUserOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        viewPagerControl()

    }

    private fun viewPagerControl() {

        adapterViewPager = ViewPagerUserOrderAdapter(supportFragmentManager,this)
        vpUserOrder.adapter = adapterViewPager
        tlUserOrder.setupWithViewPager(vpUserOrder)

    }
}
