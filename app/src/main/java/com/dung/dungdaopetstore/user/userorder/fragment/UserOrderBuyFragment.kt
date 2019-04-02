package com.dung.dungdaopetstore.user.userorder.fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserOrderBuyAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.OrderDatabase
import com.dung.dungdaopetstore.model.Order
import kotlinx.android.synthetic.main.fragment_user_order_buy.view.*

class UserOrderBuyFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rUsername: String
    lateinit var rvUserOrderBuy: RecyclerView
    lateinit var list: ArrayList<Order>
    lateinit var adapter: UserOrderBuyAdapter
    lateinit var orderDatabase: OrderDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_user_order_buy, container,false)

        initView()
        startRecycleView()
        changeListInformation()

        return rootview
    }

    fun initView(){
        orderDatabase = OrderDatabase(context!!)
        rvUserOrderBuy = rootview.rvUserOrderBuy
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserOrderBuyAdapter(context!!, list)
    }

    fun startRecycleView(){
        rvUserOrderBuy.layoutManager = GridLayoutManager(context!!, 2)
        rvUserOrderBuy.setHasFixedSize(true)
        rvUserOrderBuy.adapter = adapter
    }

    fun changeListInformation(){
        orderDatabase.getBuyOrderByUsername(list,adapter,rUsername)
    }

}