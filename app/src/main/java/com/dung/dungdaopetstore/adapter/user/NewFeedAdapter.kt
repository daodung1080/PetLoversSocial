package com.dung.dungdaopetstore.adapter.user

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.NewFeed
import com.dung.dungdaopetstore.user.fragment.UserNewFeedFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
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
        // get all information then put into View

        holder.txtNewFeedName.text = newFeed.username
        holder.txtNewFeedTitle.text = newFeed.title
        holder.txtNewFeedComment.text = newFeed.userComment
        Picasso.get().load(newFeed.petImage).into(holder.imgNewFeedBackground)
        holder.cvNewFeedImageClick.setOnClickListener {
            fragment.getPetImage(p1)
        }

        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().userTable).child(newFeed.username).child("image")
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var image = p0.value as String
                    Picasso.get().load(image).into(holder.imgNewFeedProfile)
                }

            })

        holder.cvNewFeed.setOnClickListener {
            fragment.meetPeople(p1)
        }

        setAnimation(holder.itemView,p1)

    }

    class NewFeedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // init All view in User New Feed Layout
        var imgNewFeedProfile = itemView.imgNewFeedProfile
        var txtNewFeedName = itemView.txtNewFeedName
        var txtNewFeedTitle = itemView.txtNewFeedTitle
        var txtNewFeedComment = itemView.txtNewFeedComment
        var imgNewFeedBackground = itemView.imgNewFeedBackground
        var cvNewFeed = itemView.cvNewFeed
        var cvNewFeedImageClick = itemView.cvNewFeedImageClick
    }
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        var lastPosition = -1
        if (position > lastPosition) {
            val anim = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            anim.duration = 700
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }
}