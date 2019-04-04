package com.dung.dungdaopetstore.user.usercommunity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserSocialAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_community.*
import com.dung.dungdaopetstore.animadapter.CardScaleHelper
import com.dung.dungdaopetstore.user.userchat.UserChatActivity


class UserCommunityActivity : BaseActivity() {

    lateinit var adapter: UserSocialAdapter
    lateinit var list: ArrayList<User>
    lateinit var rUsername: String
    lateinit var mData: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_community)

        // Create toolbar with new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        // Config animation when switch activity
        activityAnim(this)


        initView()
        addList()

    }

    // get all user then put into recycler View
    private fun addList() {
        mData.child(Constants().userTable).orderByChild("ban").equalTo(false)
            .addChildEventListener(object: ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var user = p0.getValue(User::class.java)
                    list.add(user!!)
                    adapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                }

            })
    }


    // init All View and Class
    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        list = ArrayList()
        adapter = UserSocialAdapter(this, list)
        rUsername = getRootUsername()
        // set adapter for recyclerView
        lvUserCommunity.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        lvUserCommunity.adapter = adapter
        var mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper.setCurrentItemPos(1)
        mCardScaleHelper.attachToRecyclerView(lvUserCommunity)
    }

    // click People Chat icon Function
    fun meetPeople(position: Int){
        var intent = Intent(this@UserCommunityActivity, UserChatActivity::class.java)
        var user = list.get(position)
        if(user.username.equals(rUsername)){
            showMessage(resources.getString(R.string.errorChat),false)
        }else{
            intent.putExtra("people",user.username)
            startActivity(intent)
        }
    }

    // Back button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
