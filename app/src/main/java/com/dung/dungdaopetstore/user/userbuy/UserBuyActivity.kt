package com.dung.dungdaopetstore.user.userbuy

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.UserMarketAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.PetDatabase
import com.dung.dungdaopetstore.model.Animal
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_buy.*
import kotlinx.android.synthetic.main.dialog_user_market.view.*
import kotlinx.android.synthetic.main.dialog_user_market_buy.view.*
import java.text.DecimalFormat
import java.text.NumberFormat

class UserBuyActivity : BaseActivity() {

    lateinit var petDatabase: PetDatabase
    lateinit var adapter: UserMarketAdapter
    lateinit var list: ArrayList<Animal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_buy)

        getAllAnimals()
        onClickAnimals()
        findPetByName()

    }

    fun findPetByName(){

        btnUserMarketFind.setOnClickListener {
            list.clear()
            var petName = edtUserMarketFind.text.toString()
            if(petName.isEmpty()){
                petDatabase.getAllAnimals(adapter,list)
            }else{
                petDatabase.findPet(adapter,list,petName,resources.getString(R.string.errorUserMarketFind))

            }
        }

        edtUserMarketFind.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when(actionId){
                EditorInfo.IME_ACTION_DONE ->{
                    btnUserMarketFind.callOnClick()
                    true
                }
                else -> false
            }
        }

    }

    private fun onClickAnimals() {
        lvUserBuy.setOnItemClickListener { parent, view, position, id ->
            var animal = list.get(position)

            var image = animal.image
            var name = animal.name
            var gender = animal.gender
            var price = "${animal.price.toInt()} ${resources.getString(R.string.dialogPrice)}"
            var amount = "${animal.amount} ${resources.getString(R.string.dialogAmount)}"
            var seller = animal.seller

            showPetInformation(image,name,gender,price,amount,seller)

        }
    }

    private fun getAllAnimals() {
        petDatabase = PetDatabase(this)
        list = ArrayList()
        adapter = UserMarketAdapter(this,list)
        lvUserBuy.adapter = adapter

        petDatabase.getAllAnimals(adapter,list)


    }

    fun showPetInformation(image: String, name: String, gender: String, price: String, amount: String, seller: String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(resources.getString(R.string.titleDialogUserMarket))
        alertDialog.setIcon(R.drawable.img_information)
        var view = layoutInflater.inflate(R.layout.dialog_user_market,null)

        var imgDialogUserMarketProfile = view.imgDialogUserMarketProfile
        var txtDialogUserMarketPetName = view.txtDialogUserMarketPetName
        var txtDialogUserMarketPetGender = view.txtDialogUserMarketPetGender
        var txtDialogUserMarketPetPrice = view.txtDialogUserMarketPetPrice
        var txtDialogUserMarketPetAmount = view.txtDialogUserMarketPetAmount
        var txtDialogUserMarketPetSeller = view.txtDialogUserMarketPetSeller

        Picasso.get().load(image).into(imgDialogUserMarketProfile)
        txtDialogUserMarketPetName.setText(name)
        txtDialogUserMarketPetGender.setText(gender)
        txtDialogUserMarketPetPrice.setText(price)
        txtDialogUserMarketPetAmount.setText(amount)
        txtDialogUserMarketPetSeller.setText(seller)

        alertDialog.setView(view)
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        var dialog = alertDialog.create()
        dialog.show()

    }

    fun buyPet(position: Int){
        var amountList: ArrayList<Int> = ArrayList()
        var animal = list.get(position)
        for(i in 1..animal.amount){
            amountList.add(i)
        }

        var amountAdapter: ArrayAdapter<Int> = ArrayAdapter(this, R.layout.spinner_custom_text, amountList)
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.img_shopping_cart_1)
        alertDialog.setTitle(resources.getString(R.string.alertDialogUserMarket))
        var view = layoutInflater.inflate(R.layout.dialog_user_market_buy,null)
        var spnUserMarketBuy = view.spnUserMarketBuy
        var txtUserMarketBuy = view.txtMarketBuy

        spnUserMarketBuy.adapter = amountAdapter
        spnUserMarketBuy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                txtUserMarketBuy.setText("")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var am = amountList.get(position)
                var price = animal.price
                var cost = (am*price).toInt()
                var format = DecimalFormat("###,###,###")

                txtUserMarketBuy.setText("${format.format(cost)} VND")
            }

        }

        alertDialog.setView(view)
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            showMessage("Your order will be delivered soon!", true)
        })
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        var dialog = alertDialog.create()
        dialog.show()
    }

}
