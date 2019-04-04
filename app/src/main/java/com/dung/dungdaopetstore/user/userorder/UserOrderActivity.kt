package com.dung.dungdaopetstore.user.userorder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.ViewPagerUserOrderAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user_order.*

class UserOrderActivity : BaseActivity() {

    lateinit var adapterViewPager: ViewPagerUserOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order)

        // Create toolbar with new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        // Config animation when switch activity
        activityAnim(this)


        viewPagerControl()

    }

    // set up View Pager for 2 fragment - Order Buy Fragment, Order Sell Fragment
    private fun viewPagerControl() {

        adapterViewPager = ViewPagerUserOrderAdapter(supportFragmentManager, this)
        vpUserOrder.adapter = adapterViewPager
        tlUserOrder.setupWithViewPager(vpUserOrder)

    }

    // Back Button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
