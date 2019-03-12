package com.dung.dungdaopetstore.staff

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.staff.fragment.OrderListFragment
import com.dung.dungdaopetstore.staff.fragment.PetAdditionFragment
import com.dung.dungdaopetstore.staff.fragment.PetListFragment
import com.dung.dungdaopetstore.staff.fragment.UserListFragment
import kotlinx.android.synthetic.main.activity_staff.*
import kotlinx.android.synthetic.main.app_bar_staff.*

class StaffActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)
        setSupportActionBar(toolbar)

        replaceFragment(R.id.flStaff, PetAdditionFragment())

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.navigationPetAddtion -> fragment = PetAdditionFragment()
            R.id.navigationPetList -> fragment = PetListFragment()
            R.id.navigationUserList -> fragment = UserListFragment()
            R.id.navigationOrderList -> fragment = OrderListFragment()
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        replaceFragment(R.id.flStaff, fragment!!)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
