package com.dung.dungdaopetstore.user.usercommunity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.UserSocialAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.model.Owner
import kotlinx.android.synthetic.main.activity_user_community.*

class UserCommunityActivity : BaseActivity() {

    lateinit var adapter: UserSocialAdapter
    lateinit var list: ArrayList<Owner>
    lateinit var rUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_community)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        initView()
        exampleList()

    }

    private fun exampleList() {
        list.add(Owner("dung123","abc","male",3,"dog","none","owner123"))
        list.add(Owner("dung123","abc","male",3,"dog","none","owner123"))
        list.add(Owner("dung123","abc","male",3,"dog","none","owner123"))
        list.add(Owner("dung123","abc","male",3,"dog","none","owner123"))
        list.add(Owner("dung123","abc","male",3,"dog","none","owner123"))
        adapter.notifyDataSetChanged()
    }

    private fun initView() {
        list = ArrayList()
        adapter = UserSocialAdapter(this,list)
        rUsername = getRootUsername()
        lvUserCommunity.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        lvUserCommunity.adapter = adapter
    }
}
