package com.dung.dungdaopetstore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Animal
import com.dung.dungdaopetstore.user.userbuy.UserBuyActivity
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.list_item_user_market.view.*

class UserMarketAdapter(var context: UserBuyActivity, var list: ArrayList<Animal>)
    : BaseAdapter() {

    class ViewHolder(view: View){
        var txtUserMarketPetName = view.txtUserMarketPetName
        var imgUserMarketBuy = view.imgUserMarketBuy
        var imgUserMarketProfile = view.imgUserMarketProfile
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        var viewHolder: ViewHolder
        if(convertView == null){
            var layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.list_item_user_market,null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        var animal = getItem(position) as Animal
        viewHolder.txtUserMarketPetName.text = animal.name
        viewHolder.imgUserMarketBuy.setOnClickListener {
            context.buyPet(position)
        }
        Picasso.get().load(animal.image).into(viewHolder.imgUserMarketProfile)
        return view as View
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}