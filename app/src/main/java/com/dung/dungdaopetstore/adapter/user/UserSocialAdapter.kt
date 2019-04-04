package com.dung.dungdaopetstore.adapter.user

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.animadapter.CardAdapterHelper
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Owner
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.user.usercommunity.UserCommunityActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_user_social.view.*
import java.util.*
import kotlin.collections.ArrayList

class UserSocialAdapter(var context: UserCommunityActivity,var list: ArrayList<User>)
    : RecyclerView.Adapter<UserSocialAdapter.UserHolder>() {

    val cardAdapterHelper = CardAdapterHelper()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_user_social,p0,false)
        cardAdapterHelper.onCreateViewHolder(p0,view)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserHolder, p1: Int) {
        // Change random background each other User Card
        cardAdapterHelper.onBindViewHolder(holder.itemView,p1,itemCount)
        randomBackground(holder.clSocialBackground)
        var user = list.get(p1)
        // get all information then put into View
        Picasso.get().load(user.image).into(holder.imgSocial)
        holder.txtProfileFullname.text = user.fullname
        holder.txtProfileLocation.text = user.address
        holder.txtProfileTradeTime.text = "${user.tradeTime} ${context.resources.getString(R.string.txtTimes)}"
        holder.txtProfilePhone.text = user.phoneNumber
        initPetList(holder.rvSocial,user.username)
        holder.cvSocialBackground.setOnClickListener {
            context.meetPeople(p1)
        }
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // init All view in User Social Layout
        var cvSocialBackground = itemView.cvSocialBackground
        var clSocialBackground = itemView.clSocialBackground
        var imgSocial = itemView.imgSocial
        var txtProfileFullname = itemView.txtProfileFullname
        var txtProfilePhone = itemView.txtProfilePhone
        var txtProfileTradeTime = itemView.txtProfileTradeTime
        var txtProfileLocation = itemView.txtProfileLocation
        var rvSocial = itemView.rvSocial
    }

    fun initPetList(rv: RecyclerView, rUsername: String){
        var petList: ArrayList<Owner> = ArrayList()
        var petAdapter = PetSocialAdapter(context, petList)

        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().ownerTable).orderByChild("username").equalTo(rUsername)
            .addChildEventListener(object: ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var owner = p0.getValue(Owner::class.java)
                    petList.add(owner!!)
                    rv.layoutManager = GridLayoutManager(context,3)
                    rv.adapter = petAdapter
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            })
    }

    fun randomBackground(layout: ConstraintLayout){
        var bgList = listOf<Int>(R.drawable.bg_mix_color
            , R.drawable.bg_mix_color_1, R.drawable.bg_mix_color_2,R.drawable.bg_mix_color_3
            ,R.drawable.bg_mix_color_4,R.drawable.bg_mix_color_5)
        var randomBackground = Random().nextInt(bgList.size)
        layout.setBackgroundResource(bgList.get(randomBackground))
    }

}