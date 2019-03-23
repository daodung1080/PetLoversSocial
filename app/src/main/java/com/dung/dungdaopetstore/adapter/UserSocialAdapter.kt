package com.dung.dungdaopetstore.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Owner
import com.dung.dungdaopetstore.user.usercommunity.UserCommunityActivity

class UserSocialAdapter(var context: UserCommunityActivity,var list: ArrayList<Owner>)
    : RecyclerView.Adapter<UserSocialAdapter.UserHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_user_social,p0,false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: UserHolder, p1: Int) {

    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}