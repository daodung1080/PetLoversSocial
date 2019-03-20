package com.dung.dungdaopetstore.loginsignup

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.staff.StaffActivity
import com.dung.dungdaopetstore.user.UserActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    lateinit var mData: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mData = FirebaseDatabase.getInstance().reference

        btnLogin.setOnClickListener {
            if(rdStaff.isChecked){
                startActivity(Intent(this@LoginActivity, StaffActivity::class.java))
            }else{
                userValidation()
            }
        }

        txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

    }

    private fun userValidation() {
        var username = edtUserName.text.toString()
        var password = edtPassword.text.toString()
        if(username.length < 5 || username.length > 30){
            tilPassword.error = null
            tilUsername.error = resources.getString(R.string.errorSignUpUsername)
        }else if(password.length < 5 || password.length > 30){
            tilUsername.error = null
            tilPassword.error = resources.getString(R.string.errorSignUpPassword)
        }else{
            checkLogin(username,password)
        }
    }

    fun clearAllEDT(){
        edtUserName.setText("")
        edtPassword.setText("")
    }

    fun clearAllTIL(){
        tilUsername.error = null
        tilPassword.error = null
    }

    fun checkLogin(username: String, password: String){
        var up = "username_password"
        mData.child(Constants().userTable).orderByChild(up).equalTo("$username-$password")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.childrenCount > 0){
                        clearAllEDT()
                        clearAllTIL()
                        showMessage(resources.getString(R.string.errorLoginComplete),true)
                        var intent = Intent(this@LoginActivity, UserActivity::class.java)
                        startActivity(intent)
                        rememberUser(username)
                    }else{
                        clearAllTIL()
                        showMessage(resources.getString(R.string.errorLoginFailed),false)
                    }
                }

            })
        }

//    fun checkBanned(username: String){
//        mData.child(Constants().userTable).orderByChild("banned")
//            .equalTo(true).addListenerForSingleValueEvent(object: ValueEventListener{
//                override fun onCancelled(p0: DatabaseError) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    if(p0.childrenCount > 0){
//                        var user = p0.getValue(User::class.java)
//                        if (username.equals(user!!.username)){
//                            showMessage("Your account have been banned",false)
//                        }else{
//                            showMessage("Login Successfully",true)
//                            showMessage(user.username,true)
//                        }
//                    }
//                }
//
//            })
//    }

    fun rememberUser(username: String){
        var editor = getSharedPreferences("USER", Context.MODE_PRIVATE).edit()
        editor.putString("username", username)
        editor.commit()
    }
}
