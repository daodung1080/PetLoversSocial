package com.dung.dungdaopetstore.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Animal
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Show message instead Toast
    fun showMessage(message: String, switch: Boolean){
        if(switch == true){
            Toasty.success(applicationContext, message).show()
        }else{
            Toasty.info(applicationContext, message).show()
        }
    }

    // Replace fragment when click item navigation
    fun replaceFragment(idLayout: Int, fragment: Fragment){
        if(fragment != null){
            supportFragmentManager.beginTransaction().replace(idLayout, fragment).commit()
        }
    }

    // get Root username who Login into Application
    fun getRootUsername(): String{
        return getSharedPreferences("USER", Context.MODE_PRIVATE).getString("username","")
    }

    // Set animation for Recycler View when create
    fun setAnimForView(recyclerView: RecyclerView){
        var anim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_fall_down)
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
    fun activityAnim(activity: Activity){
        activity.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }

    // Set animation for Activity when switch
    fun setViewAnimation(viewToAnimate: View){
        val anim = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 600
        viewToAnimate.startAnimation(anim)
    }

    fun notifyUser(context: Activity){
        var mData = FirebaseDatabase.getInstance().reference
        var username = getRootUsername()
        mData.child(Constants().petTable).orderByChild("seller").equalTo(username)
            .addChildEventListener(object: ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    var animal = p0.getValue(Animal::class.java)
                    if(animal!!.amount == 0){
                        AlertDialog.Builder(context)
                            .setTitle("Notification")
                            .setMessage("Your ${animal.name} have been sold")
                            .show()
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            })
    }

}