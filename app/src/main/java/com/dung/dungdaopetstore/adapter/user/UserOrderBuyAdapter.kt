package com.dung.dungdaopetstore.adapter.user

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Order
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_user_order_buy.view.*
import java.text.DecimalFormat

class UserOrderBuyAdapter(var context: Context, var list: ArrayList<Order>)
    : RecyclerView.Adapter<UserOrderBuyAdapter.UserHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_user_order_buy,p0,false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserHolder, p1: Int) {

        var order = list.get(p1)
        // get all information then put into View
        holder.txtUserOrderBuyID.setText(order.idOrder)
        holder.txtUserOrderBuyIDPet.setText(order.pet)
        holder.txtUserOrderBuyAmount.setText("${order.amount} ${context.resources.getString(R.string.animals)}")
        var fm = DecimalFormat("###,###,###")
        holder.txtUserOrderBuyMoney.setText("${fm.format(order.totalPrice)} VND")
        Picasso.get().load(order.petImage).into(holder.imgUserOrderBuyImage)
        setAnimation(holder.itemView, p1)
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // init All view in User Order Buy Layout
        var txtUserOrderBuyID = itemView.txtUserOrderBuyID
        var txtUserOrderBuyIDPet = itemView.txtUserOrderBuyIDPet
        var txtUserOrderBuyAmount = itemView.txtUserOrderBuyAmount
        var txtUserOrderBuyMoney = itemView.txtUserOrderBuyMoney
        var imgUserOrderBuyImage = itemView.imgUserOrderBuyImage
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