package com.dung.dungdaopetstore.adapter.staff

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.staff.fragment.UserListFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_staff_user.view.*
import java.text.DecimalFormat

class StaffUserAdapter(var context: Context, var list: ArrayList<User>,var fragment: UserListFragment)
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
        var ban = true
        if(user.ban == true){
            ban = false
            holder.imgBan.setImageResource(R.drawable.img_release)
        }else{
            ban = true
            holder.imgBan.setImageResource(R.drawable.img_ban)
        }
        Picasso.get().load(user.image).into(holder.imgStaffUserImage)
        holder.txtStaffUsername.text = user.username
        holder.txtStaffPassword.text = user.password
        holder.txtStaffFullname.text = user.fullname
        holder.txtStaffPhone.text = user.phoneNumber.toString()
        holder.txtStaffBannedTime.text = "${user.bannedTime.toString()} ${context.resources.getString(R.string.txtTimes)}"
        holder.txtStaffTradeTime.text = "${user.tradeTime.toString()} ${context.resources.getString(R.string.txtTimes)}"
        holder.txtStaffMoney.text = "${format.format(user.money)} VND"
        holder.cvBan.setOnClickListener {
            fragment.banMember(p1,ban)
        }
        holder.cvChat.setOnClickListener {
            fragment.meetPeople(p1)
        }
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
        var cvBan = itemView.cvBan
        var imgBan = itemView.imgBan
        var cvChat = itemView.cvChat
    }
}