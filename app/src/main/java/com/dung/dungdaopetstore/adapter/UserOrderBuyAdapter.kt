package com.dung.dungdaopetstore.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        holder.txtUserOrderBuyID.setText(order.idOrder)
        holder.txtUserOrderBuyIDPet.setText(order.pet)
        holder.txtUserOrderBuyAmount.setText("${order.amount} ${context.resources.getString(R.string.animals)}")
        var fm = DecimalFormat("###,###,###")
        holder.txtUserOrderBuyMoney.setText("${fm.format(order.totalPrice)} VND")
        Picasso.get().load(order.petImage).into(holder.imgUserOrderBuyImage)

    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtUserOrderBuyID = itemView.txtUserOrderBuyID
        var txtUserOrderBuyIDPet = itemView.txtUserOrderBuyIDPet
        var txtUserOrderBuyAmount = itemView.txtUserOrderBuyAmount
        var txtUserOrderBuyMoney = itemView.txtUserOrderBuyMoney
        var imgUserOrderBuyImage = itemView.imgUserOrderBuyImage
    }
}