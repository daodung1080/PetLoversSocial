package com.dung.dungdaopetstore.user.userorder.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.UserOrderBuyAdapter
import com.dung.dungdaopetstore.adapter.UserOrderSellAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.OrderDatabase
import com.dung.dungdaopetstore.model.Animal
import kotlinx.android.synthetic.main.fragment_user_order_sell.view.*

class UserOrderSellFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rUsername: String
    lateinit var rvUserOrderSell: RecyclerView
    lateinit var list: ArrayList<Animal>
    lateinit var adapter: UserOrderSellAdapter
    lateinit var orderDatabase: OrderDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_user_order_sell, container,false)

        initView()
        startRecycleView()
        changeListInformation()

        return rootview
    }


    fun initView(){
        orderDatabase = OrderDatabase(context!!)
        rvUserOrderSell = rootview.rvUserOrderSell
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserOrderSellAdapter(context!!, list)
    }

    fun startRecycleView(){
        rvUserOrderSell.layoutManager = GridLayoutManager(context!!, 2)
        rvUserOrderSell.adapter = adapter
    }

    fun changeListInformation(){
        orderDatabase.getSellOrderByUsername(list,adapter,rUsername)
    }

}