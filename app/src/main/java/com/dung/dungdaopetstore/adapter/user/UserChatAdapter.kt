package com.dung.dungdaopetstore.adapter.user

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Chat
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_item_reciever.view.*

class UserChatAdapter(var context: Context, var list: ArrayList<Chat>, var rUsername: String)
    : RecyclerView.Adapter<UserChatAdapter.ChatHolder>() {

    override fun getItemViewType(position: Int): Int {
        if(list.get(position).sender.equals(rUsername)){
            return 1
        }else{
            return 0
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChatHolder {
        var view: View
        if (p1 == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_reciever,p0,false)

        }else{
            view = LayoutInflater.from(context).inflate(R.layout.list_item_sender,p0,false)
        }
        return ChatHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ChatHolder, p1: Int) {
        var chat = list.get(p1)
        p0.txtChat.text = chat.message
        getReciverImage(p0.imgChat, chat.sender)
    }

    private fun getReciverImage(image: CircleImageView , user: String) {
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().userTable).child(user)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    if(user != null){
                        Picasso.get().load(user!!.image).into(image)
                    }
                }

            })
    }

    class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtChat = itemView.txtChat
        var imgChat = itemView.imgChat
    }
}