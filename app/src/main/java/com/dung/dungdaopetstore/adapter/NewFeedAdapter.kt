package com.dung.dungdaopetstore.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.NewFeed
import com.dung.dungdaopetstore.user.fragment.UserNewFeedFragment
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_item_user_new_feed.view.*

class NewFeedAdapter(var context: Context, var list: ArrayList<NewFeed>,var fragment: UserNewFeedFragment)
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

        holder.txtNewFeedName.text = newFeed.username
        holder.txtNewFeedTitle.text = newFeed.title
        holder.txtNewFeedComment.text = newFeed.userComment
        Picasso.get().load(newFeed.petImage).into(holder.imgNewFeedBackground)
        Picasso.get().load(newFeed.userImage).into(holder.imgNewFeedProfile)
        holder.cvNewFeed.setOnClickListener {
            Toasty.success(context, "You need to chat with ${newFeed.username}? please wait for the next version").show()
        }
        holder.cvNewFeedImageClick.setOnClickListener {
            fragment.getPetImage(p1)
        }

    }

    class NewFeedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgNewFeedProfile = itemView.imgNewFeedProfile
        var txtNewFeedName = itemView.txtNewFeedName
        var txtNewFeedTitle = itemView.txtNewFeedTitle
        var txtNewFeedComment = itemView.txtNewFeedComment
        var imgNewFeedBackground = itemView.imgNewFeedBackground
        var cvNewFeed = itemView.cvNewFeed
        var cvNewFeedImageClick = itemView.cvNewFeedImageClick
    }
}