package com.dung.dungdaopetstore.user.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserRecentChatAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Chat
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.user.userchat.UserChatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_user_recent_chat.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class UserRecentChatFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rvRecentChat: RecyclerView
    lateinit var list: ArrayList<User>
    lateinit var adapter: UserRecentChatAdapter
    lateinit var mData: DatabaseReference
    lateinit var rUsername: String

    lateinit var mList: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_user_recent_chat,container,false)

        initView()
        getList()

        return rootview
    }

    // get All user have chat with rUser before
    private fun getList() {
        mData.child(Constants().chatTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                mList.clear()
                p0.children.forEach {
                    var chat = it.getValue(Chat::class.java)
                    if(chat!!.sender.equals(rUsername)){
                        mList.add(chat.receiver)
                    }
                    if(chat.receiver.equals(rUsername)){
                        mList.add(chat.sender)
                    }
                }

                readUser()

            }

        })
    }

    // get All user have chat with rUser before
    private fun readUser() {
        mData.child(Constants().userTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                    p0.children.forEach {
                        var user = it.getValue(User::class.java)
                        mList.forEach {
                            if(user!!.username.equals(it)){
                                // try catch exception
                                try {
                                    if(list.size != 0){
                                        list.forEach {
                                            if(!user.username.equals(it.username)){
                                                list.add(user)
                                            }
                                        }
                                    }else{
                                        list.add(user)
                                    }
                                }catch (e: ConcurrentModificationException){

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
        rUsername = getRootUsername()
        mData = FirebaseDatabase.getInstance().reference
        rvRecentChat = rootview.rvRecentChat
        list = ArrayList()
        adapter = UserRecentChatAdapter(context!!, list, this)
        // set adapter for Recyler View
        rvRecentChat.layoutManager = LinearLayoutManager(context)
        rvRecentChat.adapter = adapter
        setAnimForView(rvRecentChat)
        mList = ArrayList()
    }

    // switch Activity when click member Icon
    fun getChat(position: Int){
        var user = list.get(position)
        var intent = Intent(context, UserChatActivity::class.java)
        intent.putExtra("people",user.username)
        startActivity(intent)
    }

}