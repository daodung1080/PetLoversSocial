package com.dung.dungdaopetstore.adapter.staff

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserRecentChatAdapter
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.staff.fragment.QuestionFragment
import com.dung.dungdaopetstore.user.fragment.UserRecentChatFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_recent_chat.view.*

class StaffFeedBackAdapter(var context: Context, var list: ArrayList<User>, var fragment: QuestionFragment)
    : RecyclerView.Adapter<StaffFeedBackAdapter.UserHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_recent_chat, p0, false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserHolder, p1: Int) {
        var user = list.get(p1)
        holder.txtRecentChat.setText(user.fullname)
        Picasso.get().load(user.image).into(holder.imgRecentChat)
        holder.cvRecentChat.setOnClickListener {
            fragment.getChat(p1)
        }
        setAnimation(holder.itemView, p1)
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgRecentChat = itemView.imgRecentChat
        var txtRecentChat = itemView.txtRecentChat
        var cvRecentChat = itemView.cvRecentChat
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        var lastPosition = -1
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            anim.duration = 700
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }
}