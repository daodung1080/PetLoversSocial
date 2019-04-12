package com.dung.dungdaopetstore.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.dung.dungdaopetstore.R
import es.dmoral.toasty.Toasty

open class BaseFragment: Fragment() {

    // share preferences User
    var sUser = "USER"
    var sUsername = "username"

    // Show message instead Toast
    fun showMessage(message: String, switch: Boolean){
        if(switch == true){
            Toasty.success(context!!, message,1500).show()
        }else{
            Toasty.info(context!!, message,1500).show()
        }
    }

    // get Root username who Login into Application
    fun getRootUsername(): String{
        return context!!.getSharedPreferences(sUser,Context.MODE_PRIVATE).getString(sUsername,"")
    }

    // Set animation for Recycler View when create
    fun setAnimForView(recyclerView: RecyclerView){
        var anim = AnimationUtils.loadAnimation(context, R.anim.anim_fall_down)
        recyclerView.animation = anim
    }

    // Create ImeOption for Keybroad
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

    // Set animation for Activity when switch
    fun setViewAnimation(viewToAnimate: View){
        val anim = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 600
        viewToAnimate.startAnimation(anim)
    }

    // Set animation for Layout
    fun setLayoutAnimation(viewToAnimate: View){
        val anim = ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 500
        viewToAnimate.startAnimation(anim)
    }

}