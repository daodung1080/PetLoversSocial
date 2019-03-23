package com.dung.dungdaopetstore.user.userpurchase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dung.dungdaopetstore.R

class UserPurchaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_purchase)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)
    }
}
