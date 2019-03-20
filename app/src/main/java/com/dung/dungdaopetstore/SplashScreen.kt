package com.dung.dungdaopetstore

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.loginsignup.LoginActivity
import com.dung.dungdaopetstore.model.Headquaters
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        this.startAnim()

    }

    fun startAnim(){

        var timer = Timer()
        timer.schedule(100){
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            this@SplashScreen.finish()
        }

    }

}

