package com.dung.dungdaopetstore.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.User
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_item_staff_user.view.*
import java.text.DecimalFormat

class StaffUserAdapter(var context: Context, var list: ArrayList<User>)
    : RecyclerView.Adapter<StaffUserAdapter.StaffHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StaffHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_staff_user, p0, false)
        return StaffHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StaffHolder, p1: Int) {
        var user = list.get(p1)
        var format = DecimalFormat("###,###,###")
        Picasso.get().load(user.image).into(holder.imgStaffUserImage)
        holder.txtStaffUsername.text = user.username
        holder.txtStaffPassword.text = user.password
        holder.txtStaffFullname.text = user.fullname
        holder.txtStaffPhone.text = user.phoneNumber.toString()
        holder.txtStaffBannedTime.text = "${user.bannedTime.toString()} ${context.resources.getString(R.string.txtTimes)}"
        holder.txtStaffTradeTime.text = "${user.tradeTime.toString()} ${context.resources.getString(R.string.txtTimes)}"
        holder.txtStaffMoney.text = "${format.format(user.money)} VND"
    }

    class StaffHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgStaffUserImage = itemView.imgStaffUserImage
        var txtStaffUsername = itemView.txtStaffUsername
        var txtStaffFullname = itemView.txtStaffFullname
        var txtStaffPassword = itemView.txtStaffPassword
        var txtStaffPhone = itemView.txtStaffPhone
        var txtStaffBannedTime = itemView.txtStaffBannedTime
        var txtStaffTradeTime = itemView.txtStaffTradeTime
        var txtStaffMoney = itemView.txtStaffMoney
    }
}