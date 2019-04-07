package com.dung.dungdaopetstore.user.userchat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserChatAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Chat
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.user.UserActivity
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

        // register toolbar and create new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)
        activityAnim(this)

        initView()
        getUserProfile()
        sendMessage()
        getAllMessage()

    }

    // set physical for back button like the same android original back button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }

    // get all user profile include Full name and Image then put into View
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

    // get All message of user
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

    // button send message on click
    private fun sendMessage() {
        imgSend.setOnClickListener {
            validation()
        }
    }

    // check form validate when clicked Send Button
    private fun validation() {
        var message = edtMessage.text.toString()
        if(message.isEmpty()){

        }else{
            mData.child(Constants().chatTable).push().setValue(Chat(message,receiver,rUsername))
            edtMessage.setText("")
        }
    }

    // init All view and Class
    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserChatAdapter(this, list, rUsername)
        // set adapter for recyclerView
        var linearLayoutManager = LinearLayoutManager(this)
        rvChat.layoutManager = linearLayoutManager
        rvChat.adapter = adapter
        receiver = intent.getStringExtra("people")
    }

    // Back Button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
