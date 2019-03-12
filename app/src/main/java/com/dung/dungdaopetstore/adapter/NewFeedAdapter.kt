package com.dung.dungdaopetstore.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.NewFeed
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_item_user_new_feed.view.*

class NewFeedAdapter(var context: Context, var list: ArrayList<NewFeed>)
    : RecyclerView.Adapter<NewFeedAdapter.NewFeedHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewFeedHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_user_new_feed, p0, false)
        return NewFeedHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewFeedHolder, p1: Int) {
        var newFeed = list.get(p1)

        holder.txtNewFeedName.text = newFeed.name
        holder.imgNewFeedBackground.setImageResource(newFeed.imgBackground)
        holder.imgNewFeedChat.setOnClickListener {
            Toasty.success(context, "You need to chat with anotherone? please wait for the next version").show()
        }

    }

    class NewFeedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgNewFeedProfile = itemView.imgNewFeedProfile
        var txtNewFeedName = itemView.txtNewFeedName
        var imgNewFeedBackground = itemView.imgNewFeedBackground
        var imgNewFeedChat = itemView.imgNewFeedChat
    }
}