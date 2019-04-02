package com.dung.dungdaopetstore.staff.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.staff.StaffOrderAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Order
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_staff_order_list.view.*

class OrderListFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var list: ArrayList<Order>
    lateinit var adapter: StaffOrderAdapter
    lateinit var mData: DatabaseReference
    lateinit var rvOrderList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_staff_order_list, container,false)

        initView()
        getList()

        return rootview
    }

    private fun getList() {
        mData.child(Constants().orderTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var order = it.getValue(Order::class.java)
                    list.add(order!!)
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    private fun initView() {
        list = ArrayList()
        adapter = StaffOrderAdapter(context!!,list)
        mData = FirebaseDatabase.getInstance().reference
        rvOrderList = rootview.rvOrderList
        rvOrderList.layoutManager = GridLayoutManager(context, 2)
        rvOrderList.setHasFixedSize(true)
        rvOrderList.adapter = adapter
    }

}