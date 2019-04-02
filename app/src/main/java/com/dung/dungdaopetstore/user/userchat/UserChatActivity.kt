package com.dung.dungdaopetstore.user.userchat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserChatAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Chat
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_chat.*
import kotlinx.android.synthetic.main.content_chat.*

class UserChatActivity : BaseActivity() {

    lateinit var list: ArrayList<Chat>
    lateinit var adapter: UserChatAdapter
    lateinit var rUsername: String
    lateinit var mData: DatabaseReference
    lateinit var receiver: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        initView()
        getUserProfile()
        sendMessage()
        getAllMessage()

    }

    private fun getUserProfile() {
        mData.child(Constants().userTable).child(receiver)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    if(user != null){
                        Picasso.get().load(user.image).into(imgUserChat)
                        txtUserChat.text = user.fullname
                    }else{
                        txtUserChat.text = resources.getString(R.string.title_pet_lovers)
                        imgUserChat.setImageResource(R.drawable.img_headquarters)
                    }
                }

            })
    }

    private fun getAllMessage() {
        mData.child(Constants().chatTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var chat = it.getValue(Chat::class.java)
                    if(chat!!.sender.equals(rUsername) && chat.receiver.equals(receiver) ||
                            chat.sender.equals(receiver) && chat.receiver.equals(rUsername)){
                        list.add(chat)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    private fun sendMessage() {
        imgSend.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        var message = edtMessage.text.toString()
        if(message.isEmpty()){

        }else{
            mData.child(Constants().chatTable).push().setValue(Chat(message,receiver,rUsername))
            edtMessage.setText("")
        }
    }

    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserChatAdapter(this, list, rUsername)
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        rvChat.layoutManager = linearLayoutManager
        rvChat.adapter = adapter
        receiver = intent.getStringExtra("people")
    }
}
