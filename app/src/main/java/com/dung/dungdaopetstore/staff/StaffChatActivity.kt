package com.dung.dungdaopetstore.staff

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserChatAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Chat
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_chat.*
import kotlinx.android.synthetic.main.app_bar_chat.*
import kotlinx.android.synthetic.main.content_chat.*

class StaffChatActivity : BaseActivity() {

    lateinit var list: ArrayList<Chat>
    lateinit var adapter: UserChatAdapter
    lateinit var rUsername: String
    lateinit var mData: DatabaseReference
    lateinit var receiver: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)

        // create tool bar and set up new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        initView()
        getUserProfile()
        sendMessage()
        getAllMessage()

    }

    // Get username profile include Image and Fullname
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

    // Get all message of 2 member
    private fun getAllMessage() {
        mData.child(Constants().chatTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var chat = it.getValue(Chat::class.java)
                    if(chat!!.sender.equals("hqt") && chat.receiver.equals(receiver) ||
                            chat.sender.equals(receiver) && chat.receiver.equals("hqt")){
                        list.add(chat)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    // Button send click
    private fun sendMessage() {
        imgSend.setOnClickListener {
            validation()
        }
    }

    // check form validate when click button Send
    private fun validation() {
        var message = edtMessage.text.toString()
        if(message.isEmpty()){

        }else{
            mData.child(Constants().chatTable).push().setValue(Chat(message,receiver,"hqt"))
            edtMessage.setText("")
        }
    }

    // set physical for back button like the same android original back button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }

    // init All View and all Class
    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserChatAdapter(this, list, "hqt")
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        rvChat.layoutManager = linearLayoutManager
        rvChat.adapter = adapter
        receiver = intent.getStringExtra("people")
    }

    // Back button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
