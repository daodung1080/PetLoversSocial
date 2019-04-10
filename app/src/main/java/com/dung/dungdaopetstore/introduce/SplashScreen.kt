package com.dung.dungdaopetstore.introduce

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.UserDatabase
import com.dung.dungdaopetstore.loginsignup.LoginActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import kotlin.concurrent.schedule
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils


class SplashScreen : BaseActivity() {

    var isRememberIntroduce = "INTRODUCE"
    var seenMemberIntroduce = "SEEN"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        this.initView()
        this.memberIntroduce()
        this.initAnimation()

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

        var handler = Handler()
        var runnable = Runnable {
            var checkInternet = checkInternet(this)
            if(checkInternet == true){
                imgLoading.clearAnimation()
                imgLoading.visibility = View.GONE
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                this@SplashScreen.finish()
            }else{
                imgLoading.clearAnimation()
                imgLoading.visibility = View.GONE
                txtCheckInternet.visibility = View.VISIBLE
            }
        }
        handler.postDelayed(runnable,3000)
    }

    // Check internet
    fun checkInternet(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }

    // set animation for loading View
    fun initAnimation(){
        var anim_loading: Animation = AnimationUtils.loadAnimation(this,R.anim.anim_loading)
        imgLoading.startAnimation(anim_loading)
    }


}

