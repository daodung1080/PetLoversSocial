package com.dung.dungdaopetstore.user.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.NewFeedAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.model.NewFeed
import kotlinx.android.synthetic.main.fragment_user_new_feed.view.*

class UserNewFeedFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rvNewFeed: RecyclerView
    lateinit var list: ArrayList<NewFeed>
    lateinit var adapter: NewFeedAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_user_new_feed, container, false)

        initView()
        createNewFeed()

        return rootview

    }

    private fun createNewFeed() {
        list = ArrayList()
        list.add(NewFeed(2, "Dao Dung", R.drawable.img_pet_trade))
        list.add(NewFeed(2, "Duc Minh", R.drawable.img_buy))
        list.add(NewFeed(2, "Sim Long", R.drawable.img_folder))
        list.add(NewFeed(2, "Tran Dinh", R.drawable.img_camera))
        adapter = NewFeedAdapter(context!!, list)
        rvNewFeed.layoutManager = LinearLayoutManager(context)
        rvNewFeed.adapter = adapter
    }

    private fun initView() {
        rvNewFeed = rootview.rvNewFeed
    }


}