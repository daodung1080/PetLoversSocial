package com.dung.dungdaopetstore.user

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.user.fragment.UserMainScreenFragment
import com.dung.dungdaopetstore.user.fragment.UserNewFeedFragment
import com.dung.dungdaopetstore.user.fragment.UserProfileFragment
import com.dung.dungdaopetstore.user.fragment.UserRecentChatFragment
import kotlinx.android.synthetic.main.activity_user.*
import java.lang.NullPointerException

class UserActivity : BaseActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        activityAnim(this)

        this.replaceFragment(R.id.flUser, UserMainScreenFragment())

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

}
