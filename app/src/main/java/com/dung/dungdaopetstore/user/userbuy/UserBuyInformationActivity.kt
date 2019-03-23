package com.dung.dungdaopetstore.user.userbuy

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.OwnerDatabase
import com.dung.dungdaopetstore.firebase.PetDatabase
import com.dung.dungdaopetstore.model.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_add_pet.*
import kotlinx.android.synthetic.main.activity_user_buy_information.*
import kotlinx.android.synthetic.main.dialog_user_market_buy.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class UserBuyInformationActivity : BaseActivity() {

    lateinit var rUsername: String
    lateinit var petDatabase: PetDatabase
    lateinit var petID: String
    lateinit var mData: DatabaseReference
    lateinit var ownerDatabase: OwnerDatabase

    // spinner amount pet
    lateinit var spnList: ArrayList<Int>
    lateinit var spnAdapter: ArrayAdapter<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_buy_information)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        ownerDatabase = OwnerDatabase(this)
        rUsername = getRootUsername()
        getPetInformation()
        clickBuyPet()

    }

    private fun clickBuyPet() {
        llMarketBuy.setOnClickListener {
            var status = txtMarketAmount.text.toString()
            if(status.equals(resources.getString(R.string.txtSoldOut))){
                showMessage(resources.getString(R.string.txtUserMarketSoldOut),false)
            }else {
                showOrder()
            }
        }
    }

    private fun getPetInformation() {
        spnList = ArrayList()
        spnAdapter = ArrayAdapter(this@UserBuyInformationActivity, R.layout.spinner_custom_text, spnList)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mData = FirebaseDatabase.getInstance().reference
        petDatabase = PetDatabase(this)
        var intent = getIntent()
        petID = intent.getStringExtra("petID")
        petDatabase.getInformationByName(petID,imgMarketImage,txtMarketName
            ,txtMarketGender,txtMarketSeller,txtMarketAmount,txtMarketPrice,resources.getString(R.string.dialogAmount)
            ,txtMarketWeight,txtMarketCategory,resources.getString(R.string.txtSoldOut))
    }

    private fun showOrder() {
        mData.child(Constants().petTable).orderByChild("id").equalTo(petID)
            .addChildEventListener(object: ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var animal = p0.getValue(Animal::class.java)

                    var totalCost = 0
                    var totalAmount = 0

                    if(rUsername == animal!!.seller){
                        showMessage(resources.getString(R.string.errorUserMarketBuyOwnPet),false)
                    }else{
                        // create dialog
                        var alertDialog = AlertDialog.Builder(this@UserBuyInformationActivity)
                        alertDialog.setTitle(resources.getString(R.string.alertDialogUserMarket))
                        alertDialog.setIcon(R.drawable.img_shoping_cart)
                        var view = layoutInflater.inflate(R.layout.dialog_user_market_buy,null)

                        // init Dialog View
                        var spnUserMarketBuy = view.spnUserMarketBuy
                        var txtMarketBuy = view.txtMarketBuy
                        var cbUserMarketComment = view.cbUserMarketComment
                        var cvUserMarketComment = view.cvUserMarketComment
                        var edtUserMarketComment = view.edtUserMarketComment

                        cvUserMarketComment.visibility = View.GONE

                        // spn list
                        for(i in 1..animal.amount){
                            spnList.add(i)
                        }
                        spnAdapter.notifyDataSetChanged()
                        spnUserMarketBuy.adapter = spnAdapter
                        spnUserMarketBuy.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                var cost = (animal.price * spnList.get(position)).toInt()
                                totalCost = cost
                                totalAmount = spnList.get(position)
                                var fm = DecimalFormat("###,###,###")
                                txtMarketBuy.setText("${fm.format(cost)} VND")
                            }

                        }
                        //

                        // Comment
                        cbUserMarketComment.setOnCheckedChangeListener { buttonView, isChecked ->
                            if(isChecked){
                                cvUserMarketComment.visibility = View.VISIBLE
                            }else{
                                cvUserMarketComment.visibility = View.GONE
                                edtUserMarketComment.setText("")
                            }
                        }
                        //

                        alertDialog.setView(view)
                        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

                            var mComment = edtUserMarketComment.text.toString()
                            var comment = ""
                            if(mComment.isEmpty()){
                                comment = resources.getString(R.string.commentUserMarketBuyNewFeed)
                            }else{
                                comment = mComment
                            }
                            step1Buy(totalCost,animal.seller,totalAmount,animal.amount,animal.image,
                                animal.name, comment, animal.gender, animal.weight, animal.category)
                        })
                        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                        var dialog = alertDialog.create()
                        dialog.show()
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }
            })
        }

    /// Step 1: check User money and Update User Money after buy Pet - Update Amount Pet left
    private fun step1Buy(totalCost: Int,seller: String,buyAmount: Int, totalPetAmount: Int,petImage: String
                         ,petName: String,userComment: String,petGender: String,petWeight: Int, petCategory: String) {
        mData.child(Constants().userTable).orderByChild("username").equalTo(rUsername)
            .addChildEventListener(object: ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var user = p0.getValue(User::class.java)
                    if(user!!.money < totalCost){
                        showMessage(resources.getString(R.string.errorNotEnoughMoney),false)
                    }else{
                        mData.child(Constants().userTable).child(rUsername).child("money")
                            .setValue(user.money - totalCost)
                        var tradeTime = user!!.tradeTime + 1
                        mData.child(Constants().userTable).child(rUsername).child("tradeTime")
                            .setValue(tradeTime)

                        // Update pet Amount
                        mData.child(Constants().petTable).child(petID).child("amount")
                            .setValue(totalPetAmount - buyAmount)
                        step2Buy(seller,totalCost,buyAmount,petImage,user.image,petName,userComment)
                        ownerDatabase.addOwner(rUsername,petName,petGender,petWeight,petCategory,petImage)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            })
    }

    // Step 2: update Seller Trade time and Seller Money
    private fun step2Buy(seller: String, moneyGet: Int,amountGet: Int, imagePet: String,
                         imageUser: String,petName: String,userComment: String) {
        if(seller.equals("Pet Lovers headquarters Store")){
            mData.child(Constants().headquatersTable).orderByChild("fund")
                .addChildEventListener(object: ChildEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    }

                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    }

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        var h = p0.getValue(Headquaters::class.java)
                        var fund = h!!.fund + moneyGet
                        mData.child(Constants().headquatersTable).child("hqt").child("fund")
                            .setValue(fund)
                        step3Buy(amountGet,moneyGet,imagePet,imageUser,petName,userComment)
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                    }

                })
        }else{
            mData.child(Constants().userTable).orderByChild("username").equalTo(seller)
                .addChildEventListener(object: ChildEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    }

                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    }

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        var user = p0.getValue(User::class.java)
                        var tradeTime = user!!.tradeTime + 1
                        mData.child(Constants().userTable).child(seller).child("tradeTime")
                            .setValue(tradeTime)
                        mData.child(Constants().userTable).child(seller).child("money")
                            .setValue(user.money + moneyGet)
                        step3Buy(amountGet,moneyGet,imagePet,imageUser,petName,userComment)
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                    }
                })
        }
    }

    // Step 3: Update Order And New Feed
    private fun step3Buy(totalAmount: Int,totalCost: Int,imagePet: String
                         ,imageUser: String,petName: String,userComment: String) {

        // order
        var orderID = "order${SimpleDateFormat("MMddmmss").format(Calendar.getInstance().time)}"
        mData.child(Constants().orderTable).child(orderID)
            .setValue(Order(rUsername,petID,totalAmount,totalCost,orderID,imagePet))

        // new feed
        var newfeedDate = SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(Calendar.getInstance().time)
        var title  = "${resources.getString(R.string.titleUserMarketBuyNewFeed)} $petName"
        mData.child(Constants().newfeedTable).child(newfeedDate)
            .setValue(NewFeed(imagePet,title,userComment,newfeedDate,rUsername))

        showMessage("$petName ${resources.getString(R.string.UserMarketBuyComplete)}",true)

    }
}
