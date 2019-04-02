package com.dung.dungdaopetstore.staff.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.staff.StaffPetAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Animal
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_staff_pet_list.view.*
import java.util.*

class PetListFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var list: ArrayList<Animal>
    lateinit var adapter: StaffPetAdapter
    lateinit var rvPetList: RecyclerView
    lateinit var mData: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_staff_pet_list, container,false)

        initView()
        getList()

        return rootview
    }

    private fun getList() {
        mData.child(Constants().petTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var animal = it.getValue(Animal::class.java)
                    list.add(animal!!)
                }
                Collections.reverse(list)
                adapter.notifyDataSetChanged()
            }

        })
    }

    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        rvPetList = rootview.rvPetList
        list = ArrayList()
        adapter = StaffPetAdapter(context!!,list,this)
        rvPetList.layoutManager = LinearLayoutManager(context)
        rvPetList.setHasFixedSize(true)
        rvPetList.adapter = adapter
    }

    fun removePet(position: Int){
        var animal = list.get(position)
        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setIcon(R.drawable.img_bin)
        alertDialog.setTitle(resources.getString(R.string.titleRemovePet))
        alertDialog.setMessage("${resources.getString(R.string.removePet)} ${animal.name}?")
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var mData = FirebaseDatabase.getInstance().reference
            mData.child(Constants().petTable).child(animal.id).removeValue()
                .addOnFailureListener {
                    showMessage(resources.getString(R.string.petRemoveFailed),false)
                }
        })
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        var dialog = alertDialog.create()
        dialog.show()
    }

    fun acceptPet(position: Int){
        var animal = list.get(position)
        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setIcon(R.drawable.img_done)
        alertDialog.setTitle(resources.getString(R.string.titleCheckPet))
        alertDialog.setMessage("${resources.getString(R.string.removePet)} ${animal.name}\n${resources.getString(R.string.checkPet1)}")
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var mData = FirebaseDatabase.getInstance().reference
            mData.child(Constants().petTable).child(animal.id).child("confirm").setValue(true)
                .addOnFailureListener {
                    showMessage(resources.getString(R.string.checkInternet),false)
                }
        })
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        var dialog = alertDialog.create()
        dialog.show()
    }

}