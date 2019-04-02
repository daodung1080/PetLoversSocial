package com.dung.dungdaopetstore.adapter.user

import android.content.Context
import android.support.transition.FragmentTransitionSupport
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.user.userorder.UserOrderActivity
import com.dung.dungdaopetstore.user.userorder.fragment.UserOrderBuyFragment
import com.dung.dungdaopetstore.user.userorder.fragment.UserOrderSellFragment

class ViewPagerUserOrderAdapter(fm: FragmentManager?, var context: UserOrderActivity) : FragmentStatePagerAdapter(fm) {

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
            0 -> title = context.resources.getString(R.string.vpPetBuy)
            1 -> title = context.resources.getString(R.string.vpPetSell)
        }

        return title
    }
}