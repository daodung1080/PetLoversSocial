package com.dung.dungdaopetstore.adapter.user

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Animal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_user_order_sell.view.*
import java.text.DecimalFormat

class UserOrderSellAdapter(var context: Context, var list: ArrayList<Animal>)
    : RecyclerView.Adapter<UserOrderSellAdapter.UserHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_user_order_sell,p0,false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserHolder, p1: Int) {
        var animal = list.get(p1)
        // get all information then put into View
        Picasso.get().load(animal.image).into(holder.imgUserOrderSellImage)
        holder.txtUserOrderSellPetID.setText(animal.id)
        holder.txtUserOrderSellGender.setText(animal.gender)
        holder.txtUserOrderSellName.setText(animal.name)
        var format = DecimalFormat("###,###,###")
        holder.txtUserOrderSellPrice.setText("${format.format(animal.price)} VND")
        holder.cbUserOrderSell.isChecked = animal.confirm
        if(animal.amount <= 0){
            holder.txtUserOrderSellStatus.setTextColor(context.resources.getColor(R.color.colorEndBtn1))
            holder.txtUserOrderSellStatus.text = context.resources.getString(R.string.txtSoldOut)
        }else{
            holder.txtUserOrderSellStatus.setTextColor(context.resources.getColor(R.color.colorStartBtn))
            holder.txtUserOrderSellStatus.text = context.resources.getString(R.string.txtAvailable)
        }
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // init All view in User Order Sell Layout
        var imgUserOrderSellImage = itemView.imgUserOrderSellImage
        var txtUserOrderSellPetID = itemView.txtUserOrderSellPetID
        var txtUserOrderSellName = itemView.txtUserOrderSellName
        var txtUserOrderSellGender = itemView.txtUserOrderSellGender
        var txtUserOrderSellPrice = itemView.txtUserOrderSellPrice
        var cbUserOrderSell = itemView.cbUserOrderSell
        var txtUserOrderSellStatus = itemView.txtUserOrderSellStatus

    }
}