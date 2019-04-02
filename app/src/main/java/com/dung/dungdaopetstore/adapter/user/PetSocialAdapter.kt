package com.dung.dungdaopetstore.adapter.user

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Owner
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_pet_social.view.*

class PetSocialAdapter(var context: Context, var list: ArrayList<Owner>)
    : RecyclerView.Adapter<PetSocialAdapter.PetHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PetHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_pet_social,p0,false)
        return PetHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: PetHolder, p1: Int) {
        var owner = list.get(p1)
        Picasso.get().load(owner.petImage).into(p0.imgSocialPet)
    }

    class PetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgSocialPet = itemView.imgSocialPet
    }
}