package com.dung.dungdaopetstore.user.usersell

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserSellAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.OwnerDatabase
import com.dung.dungdaopetstore.model.Animal
import com.dung.dungdaopetstore.model.Owner
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_sell_pet.*
import kotlinx.android.synthetic.main.dialog_user_sell_valuation.view.*
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.util.*

class UserSellPetActivity : BaseActivity() {

    lateinit var rUsername: String
    lateinit var list: ArrayList<Owner>
    lateinit var adapter: UserSellAdapter
    lateinit var ownerDatabase: OwnerDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sell_pet)

        initView()
        addList()

    }

    private fun addList() {
        ownerDatabase.getAllMyOwner1(list,adapter,rUsername,txtUserSellInform,resources.getString(R.string.warningSellPet1))
    }

    private fun initView() {
        rUsername = getRootUsername()
        ownerDatabase = OwnerDatabase(this)
        list = ArrayList()
        adapter = UserSellAdapter(this, list)
        rvUserSell.layoutManager = LinearLayoutManager(this)
        rvUserSell.adapter = adapter
        setAnimForView(rvUserSell)
    }

    fun sellPet(position: Int){
        var owner = list.get(position)
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.img_price)
        alertDialog.setTitle(resources.getString(R.string.titleUserSellValuation))
        var view = layoutInflater.inflate(R.layout.dialog_user_sell_valuation, null)
        var edtUserSellValuation = view.edtUserSellValuation
        alertDialog.setView(view)
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            try {
                var valuation = (edtUserSellValuation.text.toString()).toInt()
                if(valuation < 10000){
                    showMessage(resources.getString(R.string.errorPrice),false)
                }else{
                    var alertDialog1 = AlertDialog.Builder(this)
                    alertDialog1.setIcon(R.drawable.img_price)
                    alertDialog1.setTitle(resources.getString(R.string.titleUserSellPet))
                    var fm = DecimalFormat("###,###,###")
                    alertDialog1.setMessage("${resources.getString(R.string.messageUserSellPet)} ${owner.petName}\n" +
                            "${resources.getString(R.string.messageUserSellPet1)} ${fm.format(valuation)} VND?\n" +
                            "${resources.getString(R.string.messageUserSellPet2)}")
                    alertDialog1.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        completesellPet(owner.petCategory, owner.petName,
                            owner.petGender,owner.petWeight,valuation,owner.petImage,owner.ownerID)
                    })
                    alertDialog1.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    var dialog1 = alertDialog1.create()
                    dialog1.show()
                }
            }catch (e: NumberFormatException){
                showMessage(resources.getString(R.string.errorNumberFormat2),false)
            }
        })
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        var dialog = alertDialog.create()
        dialog.show()
    }


    fun completesellPet(category: String, petName: String, petGender: String,
                petWeight: Int, price: Int,petImage: String, idOwner: String){
        var mData = FirebaseDatabase.getInstance().reference
        var timetoMiliis = Calendar.getInstance().timeInMillis
        var id = "animal$timetoMiliis"
        mData.child(Constants().petTable).child(id).setValue(
            Animal(id,petName, petGender, price.toDouble(), 1, petImage,false,rUsername,petWeight,category))
            .addOnFailureListener {
                showMessage(resources.getString(R.string.errorUserSellPetFailed),false)
                Log.e("UserMarketError",it.toString())
            }.addOnSuccessListener {
                showMessage(resources.getString(R.string.errorUserSellPetComplete),true)
                mData.child(Constants().ownerTable).child(idOwner).removeValue()
            }
    }

}
