package com.dung.dungdaopetstore.user.userbuy

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.OwnerDatabase
import com.dung.dungdaopetstore.model.*
import com.dung.dungdaopetstore.user.userchat.UserChatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_buy_information.*
import kotlinx.android.synthetic.main.dialog_user_market_buy.view.*
import java.lang.NullPointerException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class UserBuyInformationActivity : BaseActivity() {

    lateinit var rUsername: String
    lateinit var petID: String
    lateinit var mData: DatabaseReference
    lateinit var ownerDatabase: OwnerDatabase

    // spinner amount pet
    lateinit var spnList: ArrayList<Int>
    lateinit var spnAdapter: ArrayAdapter<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_buy_information)

        // Create toolbar with new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        // Config animation when switch activity
        activityAnim(this)


        ownerDatabase = OwnerDatabase(this)
        rUsername = getRootUsername()
        getPetInformation()
        clickBuyPet()

    }

    // function when user click Buy Pet
    private fun clickBuyPet() {
        // Button buy on click
        llMarketBuy.setOnClickListener {
            var status = txtMarketAmount.text.toString()
            if(status.equals(resources.getString(R.string.txtSoldOut))){
                // Pet have been sold out
                showMessage(resources.getString(R.string.txtUserMarketSoldOut),false)
            }else {
                // Show bill detail when user want to buy
                showOrder()
            }
        }
        // Button contact on click
        llMarketContact.setOnClickListener {
            var rSeller = txtMarketSeller.text.toString()
            var intent = Intent(this@UserBuyInformationActivity, UserChatActivity::class.java)
            if(rSeller.equals("Pet Lovers headquarters Store")){
                intent.putExtra("people","hqt")
                startActivity(intent)
            }else if(rSeller.equals(rUsername)){
                showMessage(resources.getString(R.string.errorChat),false)
            }else{
                intent.putExtra("people",rSeller)
                startActivity(intent)
            }
        }
    }

    // get Pet information then put into View
    private fun getPetInformation() {
        spnList = ArrayList()
        spnAdapter = ArrayAdapter(this@UserBuyInformationActivity, R.layout.spinner_custom_text_buy, spnList)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mData = FirebaseDatabase.getInstance().reference
        var intent = getIntent()
        petID = intent.getStringExtra("petID")
        getInformationByName(petID,imgMarketImage,txtMarketName
            ,txtMarketGender,txtMarketSeller,txtMarketAmount,txtMarketPrice,resources.getString(R.string.dialogAmount)
            ,txtMarketWeight,txtMarketCategory,resources.getString(R.string.txtSoldOut))
    }

    // Show order detail when user want to buy
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
                        // user cannot buy their own pet
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
                    // check user money
                    if(user!!.money < totalCost){
                        // user doesnt have enough money
                        showMessage(resources.getString(R.string.errorNotEnoughMoney),false)
                    }else{
                        // user have enough money to buy
                        mData.child(Constants().userTable).child(rUsername).child("money")
                            .setValue(user.money - totalCost)
                        var tradeTime = user!!.tradeTime + 1
                        mData.child(Constants().userTable).child(rUsername).child("tradeTime")
                            .setValue(tradeTime)

                        // Update pet Amount
                        mData.child(Constants().petTable).child(petID).child("amount")
                            .setValue(totalPetAmount - buyAmount)
                        // switch to step 2
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
        // seller is Pet Lovers Headquarters
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
                        // get fund then update value of fund
                        mData.child(Constants().headquatersTable).child("hqt").child("fund")
                            .setValue(fund)
                        step3Buy(amountGet,moneyGet,imagePet,imageUser,petName,userComment)
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                    }

                })
            // seller is another User
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
                        // get user trade time and money
                        // update value user tradeTime
                        mData.child(Constants().userTable).child(seller).child("tradeTime")
                            .setValue(tradeTime)
                        // update value user Money
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

        // create order
        var orderID = "order${SimpleDateFormat("MMddmmss").format(Calendar.getInstance().time)}"
        mData.child(Constants().orderTable).child(orderID)
            .setValue(Order(rUsername,petID,totalAmount,totalCost,orderID,imagePet))

        // create new feed
        var newfeedDate = SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(Calendar.getInstance().time)
        var title  = "${resources.getString(R.string.titleUserMarketBuyNewFeed)} $petName"
        mData.child(Constants().newfeedTable).child(newfeedDate)
            .setValue(NewFeed(imagePet,title,userComment,newfeedDate,rUsername))

        showMessage("$petName ${resources.getString(R.string.UserMarketBuyComplete)}",true)

    }

    // Back button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

    // get all pet by name
    fun getInformationByName(petID: String, img: ImageView, name: TextView,
                             gender: TextView, seller: TextView, amount: TextView, price: TextView
                             , left: String, weight: TextView, category: TextView, soldOut: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().petTable).child(petID)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        var animal = p0.getValue(Animal::class.java)
                        Picasso.get().load(animal!!.image).into(img)
                        name.setText(animal.name)
                        gender.setText(animal.gender)
                        seller.setText(animal.seller)
                        if(animal.amount > 0){
                            amount.setTextColor(Color.GRAY)
                            amount.setText("${animal.amount} ${left}")
                        }else{
                            amount.setTextColor(Color.RED)
                            amount.setText(soldOut)
                        }
                        var fm = DecimalFormat("###,###,###")
                        price.setText("${fm.format(animal.price)} VND")
                        weight.setText("${animal.weight} kg")
                        category.setText(animal.category)
                    }catch (e: NullPointerException){
                        showMessage(resources.getString(R.string.errorUserBuyRemove),false)
                        llMarketBuy.visibility = View.GONE
                        llMarketContact.visibility = View.GONE
                    }
                }

            })
    }

}
