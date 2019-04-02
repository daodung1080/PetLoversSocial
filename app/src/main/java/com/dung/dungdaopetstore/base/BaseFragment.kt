package com.dung.dungdaopetstore.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.dung.dungdaopetstore.R
import es.dmoral.toasty.Toasty

open class BaseFragment: Fragment() {

    fun showMessage(message: String, switch: Boolean){
        if(switch == true){
            Toasty.success(context!!, message).show()
        }else{
            Toasty.info(context!!, message).show()
        }
    }

    fun getRootUsername(): String{
        return context!!.getSharedPreferences("USER",Context.MODE_PRIVATE).getString("username","")
    }

    fun setAnimForView(recyclerView: RecyclerView){
        var anim = AnimationUtils.loadAnimation(context, R.anim.anim_fall_down)
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