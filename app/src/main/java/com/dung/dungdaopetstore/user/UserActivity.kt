package com.dung.dungdaopetstore.user

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.loginsignup.LoginActivity
import com.dung.dungdaopetstore.user.fragment.UserMainScreenFragment
import com.dung.dungdaopetstore.user.fragment.UserNewFeedFragment
import com.dung.dungdaopetstore.user.fragment.UserProfileFragment
import com.dung.dungdaopetstore.user.fragment.UserRecentChatFragment
import kotlinx.android.synthetic.main.activity_user.*
import java.lang.NullPointerException

class UserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        activityAnim(this)

        getNotification()

        this.replaceFragment(R.id.flUser, UserMainScreenFragment())

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    // On click item when clicked item of navigation bottom
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.navigationUserMain -> fragment = UserMainScreenFragment()
            R.id.navigationUserNewFeed -> fragment = UserNewFeedFragment()
            R.id.navigationUserRecentChat -> fragment = UserRecentChatFragment()
            R.id.navigationProfile -> fragment =  UserProfileFragment()
        }
        this.replaceFragment(R.id.flUser, fragment!!)
        true
    }

    // Create button exit
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_exit,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.navigationExit){
            // Show exit dialog
            createExitDialog()
        }
        return true
    }

    // Create exit dialog
    private fun createExitDialog() {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.img_exit)
        alertDialog.setTitle(resources.getString(R.string.title_app_exit))
        alertDialog.setMessage(resources.getString(R.string.message_app_exit))
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var intent = Intent(this@UserActivity,LoginActivity::class.java)
            disableRememberUser()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            activityAnim(this)
            this.finish()
        })
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        var dialog = alertDialog.create()
        dialog.show()
    }

    // disable remember user
    fun disableRememberUser(){
        var editor = getSharedPreferences("USER", Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.commit()
    }

}
