package com.dung.dungdaopetstore.adapter.user

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Owner
import com.dung.dungdaopetstore.user.usersell.UserSellPetActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_user_sell.view.*

class UserSellAdapter(var context: UserSellPetActivity, var list: ArrayList<Owner>)
    : RecyclerView.Adapter<UserSellAdapter.UserHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_user_sell,p0,false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserHolder, p1: Int) {
        var owner = list.get(p1)
        holder.txtUserPetListCategory.text = owner.petCategory
        holder.txtUserPetListName.text = owner.petName
        holder.txtUserPetListGender.text = owner.petGender
        holder.txtUserPetListWeight.text = "${owner.petWeight} kg"
        Picasso.get().load(owner.petImage).into(holder.imgUserPetListShow)
        holder.cvUserSell.setOnClickListener {
            context.sellPet(p1)
        }
        setAnimation(holder.itemView, p1)
    }

    class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cvUserSell = view.cvUserSell
        var txtUserPetListCategory = view.txtUserPetListCategory
        var txtUserPetListName = view.txtUserPetListName
        var txtUserPetListGender = view.txtUserPetListGender
        var txtUserPetListWeight = view.txtUserPetListWeight
        var imgUserPetListShow = view.imgUserPetListShow
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