package com.dung.dungdaopetstore.user.useraddpet

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Owner
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_pet_update.*
import kotlinx.android.synthetic.main.dialog_user_update_pet_gender.view.*
import kotlinx.android.synthetic.main.dialog_user_update_pet_name.view.*
import java.lang.NumberFormatException

class UserPetUpdateActivity : BaseActivity() {

    lateinit var rUsername: String
    lateinit var mData: DatabaseReference
    lateinit var ownerID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pet_update)

        initView()
        getPetInformation()
        onclickFunction()

    }

    private fun onclickFunction() {
        cvUpdateName.setOnClickListener {
            updatePet(1)
        }
        cvUpdateWeight.setOnClickListener {
            updatePet(2)
        }
        cvUpdateGender.setOnClickListener {
            updatePet(3)
        }
    }

    fun updatePet(switch: Int){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.img_pet_update)
        when(switch){
            1 -> {
                alertDialog.setTitle(resources.getString(R.string.txtUserUpdatePetName))
                var view = layoutInflater.inflate(R.layout.dialog_user_update_pet_name,null)
                var edtUserUpdate = view.edtUserUpdate
                alertDialog.setView(view)
                alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    var pName = edtUserUpdate.text.toString()
                    if(pName.length < 5){
                        showMessage(resources.getString(R.string.errorAnimalNameRestrict),false)
                    }else{
                        mData.child(Constants().ownerTable).child(ownerID).child("petName")
                            .setValue(pName)
                    }
                })
                alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            }
            2 -> {
                alertDialog.setTitle(resources.getString(R.string.txtUserUpdatePetWeight))
                var view = layoutInflater.inflate(R.layout.dialog_user_update_pet_weight,null)
                var edtUserUpdate = view.edtUserUpdate
                alertDialog.setView(view)
                alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    try{
                        var pWeight = (edtUserUpdate.text.toString()).toInt()
                        if(pWeight < 0){
                            showMessage(resources.getString(R.string.errorWeight),false)
                        }else{
                            mData.child(Constants().ownerTable).child(ownerID).child("petWeight")
                                .setValue(pWeight)
                        }
                    }catch (e: NumberFormatException){
                        showMessage(resources.getString(R.string.errorNumberFormat1), false)
                    }
                })
                alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            }
            3 -> {
                alertDialog.setTitle(resources.getString(R.string.txtUserUpdatePetGender))
                var view = layoutInflater.inflate(R.layout.dialog_user_update_pet_gender,null)
                var rdMale = view.rdMale
                alertDialog.setView(view)
                alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    var gender = "Female"
                    if(rdMale.isChecked){
                        gender = "Male"
                    }
                    mData.child(Constants().ownerTable).child(ownerID).child("petGender")
                        .setValue(gender)
                })
                alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            }
        }
        var dialog = alertDialog.create()
        dialog.show()
    }

    private fun initView() {
        rUsername = getRootUsername()
        mData = FirebaseDatabase.getInstance().reference
        var intent = getIntent()
        ownerID = intent.getStringExtra("ownerID")
    }

    private fun getPetInformation() {
        mData.child(Constants().ownerTable).child(ownerID)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var owner = p0.getValue(Owner::class.java)
                    Picasso.get().load(owner!!.petImage).into(imgPetUpdate)
                    txtUpdatePetName.text = owner.petName
                    txtUpdatePetGender.text = owner.petGender
                    txtUpdateWeight.text = "${owner.petWeight} kg"
                }

            })
    }
}
