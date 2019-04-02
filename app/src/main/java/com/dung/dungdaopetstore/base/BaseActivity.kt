package com.dung.dungdaopetstore.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
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

    fun setAnimForView(recyclerView: RecyclerView){
        var anim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_fall_down)
        recyclerView.animation = anim
    }

    fun imeOption(edt: TextInputEditText, btn: Button){
        edt.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when(actionId){
                EditorInfo.IME_ACTION_DONE -> {
                    btn.callOnClick()
                    true
                }
                else -> false
            }
        }
    }

}