package com.dung.dungdaopetstore.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.dmoral.toasty.Toasty

open class BaseFragment: Fragment() {

    fun showMessage(message: String, switch: Boolean){
        if(switch == true){
            Toasty.success(context!!, message).show()
        }else{
            Toasty.error(context!!, message).show()
        }
    }

    fun getRootUsername(): String{
        return context!!.getSharedPreferences("USER",Context.MODE_PRIVATE).getString("username","")
    }

}