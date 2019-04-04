package com.dung.dungdaopetstore.user.userpurchase

import android.content.Intent
import android.os.Bundle
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user_purchase.*

class UserPurchaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_purchase)

        // Create toolbar with new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        // Config animation when switch activity
        activityAnim(this)


        setOnClickIcon()

    }

    // switch activity when user choose value to purchase
    private fun setOnClickIcon() {
        cv10.setOnClickListener { sendValuePurchase(10000) }
        cv50.setOnClickListener { sendValuePurchase(50000) }
        cv100.setOnClickListener { sendValuePurchase(100000) }
        cv200.setOnClickListener { sendValuePurchase(200000) }
        cv500.setOnClickListener { sendValuePurchase(500000) }
        cv1000.setOnClickListener { sendValuePurchase(1000000) }
        cv1500.setOnClickListener { sendValuePurchase(1500000) }
        cv2000.setOnClickListener { sendValuePurchase(2000000) }
        cv3000.setOnClickListener { sendValuePurchase(3000000) }
    }

    // send value into purchase detail screen
    fun sendValuePurchase(value: Int){
        var intent = Intent(this@UserPurchaseActivity,UserPurchaseInformationActivity::class.java)
        intent.putExtra("purchaseValue",value)
        startActivity(intent)
        activityAnim(this)
    }

    // Back button Animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
