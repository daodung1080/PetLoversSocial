package com.dung.dungdaopetstore.adapter.user

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Animal
import com.dung.dungdaopetstore.user.userbuy.UserBuyActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_user_market.view.*
import java.text.DecimalFormat

class UserMarketAdapter(var context: UserBuyActivity, var list: ArrayList<Animal>)
    : RecyclerView.Adapter<UserMarketAdapter.UserHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_user_market,p0,false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserHolder, p1: Int) {
        var animal = list.get(p1)
        holder.txtUserMarketPetName.text = animal.name
        holder.txtUserMarketPetGender.text = animal.gender
        var fm = DecimalFormat("###,###,###")
        holder.txtUserMarketPetPrice.text = "${fm.format(animal.price)} VND"
        Picasso.get().load(animal.image).into(holder.imgUserMarketProfile)
        holder.cvUserMarket.setOnClickListener {
            context.showPetInformation(p1)
        }
        setAnimation(holder.itemView,p1)
    }

    class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtUserMarketPetName = view.txtUserMarketPetName
        var imgUserMarketProfile = view.imgUserMarketProfile
        var txtUserMarketPetGender = view.txtUserMarketPetGender
        var cvUserMarket = view.cvUserMarket
        var txtUserMarketPetPrice = view.txtUserMarketPetPrice
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
