package com.dung.dungdaopetstore.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.dung.dungdaopetstore.R
import es.dmoral.toasty.Toasty

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showMessage(message: String, switch: Boolean){
        if(switch == true){
            Toasty.success(applicationContext, message).show()
        }else{
            Toasty.info(applicationContext, message).show()
        }
    }

    fun replaceFragment(idLayout: Int, fragment: Fragment){
        if(fragment != null){
            supportFragmentManager.beginTransaction().replace(idLayout, fragment).commit()
        }
    }

    fun getRootUsername(): String{
        return getSharedPreferences("USER", Context.MODE_PRIVATE).getString("username","")
    }

}