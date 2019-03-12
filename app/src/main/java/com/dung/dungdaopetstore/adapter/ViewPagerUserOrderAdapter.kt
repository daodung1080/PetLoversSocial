package com.dung.dungdaopetstore.adapter

import android.support.transition.FragmentTransitionSupport
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.dung.dungdaopetstore.user.userorder.fragment.UserOrderBuyFragment
import com.dung.dungdaopetstore.user.userorder.fragment.UserOrderSellFragment

class ViewPagerUserOrderAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        var fragment: Fragment? = null
        when(p0){
            0 -> fragment = UserOrderBuyFragment()
            1 -> fragment = UserOrderSellFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title = ""

        when(position){
            0 -> title = "Your Pet Buy Order"
            1 -> title = "Your Pet Trade Order"
        }

        return title
    }
}