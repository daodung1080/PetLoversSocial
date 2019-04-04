package com.dung.dungdaopetstore.adapter.staff

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.model.Animal
import com.dung.dungdaopetstore.staff.fragment.PetListFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_staff_pet.view.*
import java.text.DecimalFormat

class StaffPetAdapter(var context: Context, var list: ArrayList<Animal>,var fragment: PetListFragment)
    : RecyclerView.Adapter<StaffPetAdapter.StaffHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StaffHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_staff_pet,p0,false)
        return StaffHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StaffHolder, p1: Int) {
        var animal = list.get(p1)
        // get all information then put into View
        Picasso.get().load(animal.image).into(holder.imgPet)
        holder.txtCategory.text = animal.category
        holder.txtName.text = animal.name
        holder.txtGender.text = animal.gender
        holder.txtWeight.text = "${animal.weight} kg"
        if(animal.amount <= 0){
            holder.txtAmount.text = "${context.resources.getString(R.string.txtSoldOut)}"
            holder.txtAmount.setTextColor(context.resources.getColor(R.color.colorEndBtn1))
        }else{
            holder.txtAmount.text = "${animal.amount} ${context.resources.getString(R.string.dialogAmount)}"
        }
        var fm = DecimalFormat("###,###,###")
        holder.txtPrice.text = "${fm.format(animal.price)} VND"
        holder.txtSeller.text = animal.seller
        if(animal.confirm == true){
            holder.cvCheck.visibility = View.GONE
        }
        holder.cvBin.setOnClickListener {
            fragment.removePet(p1)
        }
        holder.cvCheck.setOnClickListener {
            fragment.acceptPet(p1)
        }
    }

    class StaffHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // init All view in Staff Pet Layout
        var imgPet = itemView.imgPet
        var txtCategory = itemView.txtCategory
        var txtName = itemView.txtName
        var txtGender = itemView.txtGender
        var txtWeight = itemView.txtWeight
        var txtAmount = itemView.txtAmount
        var txtPrice = itemView.txtPrice
        var txtSeller = itemView.txtSeller
        var cvBin = itemView.cvBin
        var cvCheck = itemView.cvCheck
    }
}