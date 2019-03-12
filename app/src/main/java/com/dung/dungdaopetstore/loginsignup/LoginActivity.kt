package com.dung.dungdaopetstore.loginsignup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.staff.StaffActivity
import com.dung.dungdaopetstore.user.UserActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            if(rdStaff.isChecked){
                startActivity(Intent(this@LoginActivity, StaffActivity::class.java))
            }else{
                startActivity(Intent(this@LoginActivity, UserActivity::class.java))
            }
        }

    }
}
