package com.dung.dungdaopetstore.staff.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.StaffUserAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.UserDatabase
import com.dung.dungdaopetstore.model.User
import kotlinx.android.synthetic.main.fragment_staff_user_list.view.*

class UserListFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rvUserList: RecyclerView
    lateinit var list: ArrayList<User>
    lateinit var adapter: StaffUserAdapter
    lateinit var userDatabase: UserDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_staff_user_list, container,false)
        initView()
        return rootview
    }

    private fun initView() {
        userDatabase = UserDatabase(context!!)
        rvUserList = rootview.rvUserList
        list = ArrayList()
        adapter = StaffUserAdapter(context!!, list)
        rvUserList.layoutManager = LinearLayoutManager(context)
        rvUserList.adapter = adapter

        userDatabase.getAllUser(list,adapter)

    }

}