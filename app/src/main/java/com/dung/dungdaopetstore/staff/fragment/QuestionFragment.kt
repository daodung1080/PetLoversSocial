package com.dung.dungdaopetstore.staff.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.staff.StaffFeedBackAdapter
import com.dung.dungdaopetstore.adapter.user.UserRecentChatAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Chat
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.staff.StaffChatActivity
import com.dung.dungdaopetstore.user.userchat.UserChatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_staff_question.view.*
import kotlinx.android.synthetic.main.fragment_user_recent_chat.view.*

class QuestionFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rvRecentChat: RecyclerView
    lateinit var list: ArrayList<User>
    lateinit var adapter: StaffFeedBackAdapter
    lateinit var mData: DatabaseReference
    lateinit var mList: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_staff_question,container,false)
        initView()
        getList()
        return rootview

    }

    // get all user have question with supporter
    private fun getList() {
        mData.child(Constants().chatTable).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                mList.clear()
                p0.children.forEach {
                    var chat = it.getValue(Chat::class.java)
                    if(chat!!.sender.equals("hqt")){
                        mList.add(chat.receiver)
                    }
                    if(chat.receiver.equals("hqt")){
                        mList.add(chat.sender)
                    }
                }

                readUser()

            }

        })
    }

    // get all user have question with supporter
    private fun readUser() {
        mData.child(Constants().userTable).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var user = it.getValue(User::class.java)
                    mList.forEach {
                        if(user!!.username.equals(it)){
                            if(list.size != 0){
                                list.forEach {
                                    if(!user.username.equals(it.username)){
                                        list.add(user)
                                    }
                                }
                            }else{
                                list.add(user)
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }


    // init all View and Class
    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        rvRecentChat = rootview.rvStaffQuestion
        list = ArrayList()
        adapter = StaffFeedBackAdapter(context!!, list, this)
        rvRecentChat.layoutManager = LinearLayoutManager(context)
        rvRecentChat.adapter = adapter
        rvRecentChat.setHasFixedSize(true)
        mList = ArrayList()
    }

    // switch Activity chat
    fun getChat(position: Int){
        var user = list.get(position)
        var intent = Intent(context, StaffChatActivity::class.java)
        intent.putExtra("people",user.username)
        startActivity(intent)
    }

}