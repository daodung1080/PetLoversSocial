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

        // Button login clicked by user
        btnLogin.setOnClickListener {
                userValidation()
                activityAnim(this)
        }

        // Button sign up clicked by user
        txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            activityAnim(this)
        }

        fillAllUserInformation()

        edtDone()

    }

    // get all data and fill out view
    fun fillAllUserInformation(){
        var sharedPreferences = getSharedPreferences("USER_REMEMBER_LAST_LOGIN", Context.MODE_PRIVATE)
        var username = sharedPreferences.getString("username",null)
        var password = sharedPreferences.getString("password",null)
        var remember = sharedPreferences.getBoolean("remember",false)

        edtUserName.setText(username)
        edtPassword.setText(password)
        cbLoginRemember.isChecked = remember
    }

    // Call login button when finishing type password
    private fun edtDone() {
        imeOption(edtPassword,btnLogin)
    }

    // Check validate form when user click button Login
    private fun userValidation() {
        var username = edtUserName.text.toString()
        var password = edtPassword.text.toString()
        if(username.length < 5 || username.length > 30){
            tilPassword.error = null
            tilUsername.error = resources.getString(R.string.errorSignUpUsername)
        }else if(password.length < 5 || password.length > 30){
            tilUsername.error = null
            tilPassword.error = resources.getString(R.string.errorSignUpPassword)
        }else if(username.equals("admin") && password.equals("admin")){
            rememberUserForLastTimeLogin("","",false)
            startActivity(Intent(this@LoginActivity, StaffActivity::class.java))
            activityAnim(this)
            this.finish()
        }else{
            checkLogin(username,password)
        }
    }

    // Clear all characters when finish Login
    fun clearAllEDT(){
        edtUserName.setText("")
        edtPassword.setText("")
    }

    // Clear all error when finish Login
    fun clearAllTIL(){
        tilUsername.error = null
        tilPassword.error = null
    }

    // Check user profile - username and password have signed - this username have not been banned
    fun checkLogin(username: String, password: String){
        var up = "username_password"
        mData.child(Constants().userTable).orderByChild(up).equalTo("$username-$password")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.childrenCount > 0){
                        p0.children.forEach {
                            var user = it.getValue(User::class.java)
                            if(user!!.ban == true){
                                clearAllTIL()
                                showMessage(resources.getString(R.string.errorLoginBanned),false)
                            }else{
                                if(cbLoginRemember.isChecked == true){

                                    // remember password and username
                                    rememberUserForLastTimeLogin(username,password,true)
                                    switchActivityWhenComplete(username)

                                }else{
                                    // clear data remember profile
                                    rememberUserForLastTimeLogin(username,password,false)
                                    switchActivityWhenComplete(username)
                                }
                            }
                        }
                    }else{
                        clearAllTIL()
                        showMessage(resources.getString(R.string.errorLoginFailed),false)
                    }
                }

            })
        }

    fun switchActivityWhenComplete(username: String){
        clearAllEDT()
        clearAllTIL()
        var intent = Intent(this@LoginActivity, UserActivity::class.java)
        startActivity(intent)
        rememberUser(username)
        this.finish()
    }


    // Remember the username when Login
    fun rememberUser(username: String){
        var editor = getSharedPreferences("USER", Context.MODE_PRIVATE).edit()
        editor.putString("username", username)
        editor.commit()
    }


    // When user click button Back
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

    // Remember user name and password when user clicked Check Box
    fun rememberUserForLastTimeLogin(username: String, password: String,remember: Boolean){
        var sharedPreferences = getSharedPreferences("USER_REMEMBER_LAST_LOGIN", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        if(remember == false){
            editor.clear()
        }else {
            editor.putString("username", username)
            editor.putString("password", password)
            editor.putBoolean("remember",remember)
        }
        editor.apply()
    }

}
