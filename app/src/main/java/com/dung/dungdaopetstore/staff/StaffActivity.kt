package com.dung.dungdaopetstore.staff

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.loginsignup.LoginActivity
import com.dung.dungdaopetstore.staff.fragment.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_staff.*
import kotlinx.android.synthetic.main.app_bar_staff.*
import kotlinx.android.synthetic.main.nav_header_staff.view.*
import java.text.DecimalFormat

class StaffActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var mData: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)
        setSupportActionBar(toolbar)

        mData = FirebaseDatabase.getInstance().reference
        replaceFragment(R.id.flStaff, PetAdditionFragment())

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        var headerview = nav_view.getHeaderView(0)
        var txtHqtMoney = headerview.txtHqtMoney
        getHqtMoney(txtHqtMoney)
    }

    fun getHqtMoney(txt: TextView){
        mData.child(Constants().headquatersTable).child("hqt").child("fund")
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    showMessage(resources.getString(R.string.checkInternet),false)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var money = p0.getValue(Int::class.java)
                    var fm = DecimalFormat("###,###,###")
                    txt.text = "${fm.format(money)} VND"
                }

            })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.navigationPetAddtion -> fragment = PetAdditionFragment()
            R.id.navigationPetList -> fragment = PetListFragment()
            R.id.navigationUserList -> fragment = UserListFragment()
            R.id.navigationOrderList -> fragment = OrderListFragment()
            R.id.nav_share -> fragment = QuestionFragment()
            R.id.nav_send -> {
                startActivity(Intent(this@StaffActivity, LoginActivity::class.java))
                this.finish()
            }
        }

        replaceFragment(R.id.flStaff, fragment!!)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
