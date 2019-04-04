package com.dung.dungdaopetstore.introduce

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.loginsignup.LoginActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : BaseActivity() {

    var isRememberIntroduce = "INTRODUCE"
    var seenMemberIntroduce = "SEEN"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        this.initView()
        this.memberIntroduce()

    }

    // init Share Preferences
    private fun initView() {
        sharedPreferences = getSharedPreferences(isRememberIntroduce,Context.MODE_PRIVATE)
    }

    // remember user have seen introduce screen
    private fun rememberIntroduce() {
        var editor = sharedPreferences.edit()
        editor.putBoolean(seenMemberIntroduce, true)
        editor.commit()
    }

    // check remember
    private fun memberIntroduce() {
        var memberIntroduce = sharedPreferences.getBoolean(seenMemberIntroduce,false)
        if(memberIntroduce == false){
            rememberIntroduce()
            startActivity(Intent(this@SplashScreen, IntroduceActivity::class.java))
            this@SplashScreen.finish()
        }else{
            startAnim()
        }
    }

    // Start to switch into Login Screen
    fun startAnim(){

        var timer = Timer()
        timer.schedule(100){
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            this@SplashScreen.finish()
        }

    }

}

