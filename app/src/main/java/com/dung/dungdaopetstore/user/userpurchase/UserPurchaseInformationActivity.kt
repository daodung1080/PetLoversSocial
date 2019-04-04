package com.dung.dungdaopetstore.user.userpurchase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_purchase_information.*
import java.text.DecimalFormat

class UserPurchaseInformationActivity : BaseActivity() {

    lateinit var rUsername: String
    lateinit var mData: DatabaseReference
    var purchaseMoney = 0
    var userMoney = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_purchase_information)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        initView()
        showEditTextWhenChooseBank()
        setImeOptionForEditText()
        setOnClickButton()
        getUserMoney()

    }

    // get user money from profile
    private fun getUserMoney() {
        mData.child(Constants().userTable).child(rUsername).child("money")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    showMessage(resources.getString(R.string.checkInternet),false)
                }
                override fun onDataChange(p0: DataSnapshot) {
                    userMoney = p0.getValue(Int::class.java)!!
                }

            })
    }

    // Button purchase on click
    private fun setOnClickButton() {
        btnUserPurchase.setOnClickListener {
            validateForm()
        }
    }

    // Check form validate for Edittext
    private fun validateForm() {
            var aNumberToString = edtUserPurchase.text.toString()
            if(aNumberToString.isEmpty()){
                tilUserPurchase.error = resources.getString(R.string.errorUserPurchase)
            }else{
                // put value in database
                mData.child(Constants().userTable).child(rUsername).child("money")
                    .setValue(userMoney + purchaseMoney)
                    .addOnFailureListener {
                        showMessage(resources.getString(R.string.checkInternet),false)
                    }
                    .addOnSuccessListener {
                        showMessage(resources.getString(R.string.completeUserPurchase),true)
                        startActivity(Intent(this@UserPurchaseInformationActivity,UserPurchaseActivity::class.java))
                        this.finish()
                    }
                clearAllView()
            }
    }

    // set imeOption Done for edittext
    private fun setImeOptionForEditText() {
        edtUserPurchase.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when(actionId){
                EditorInfo.IME_ACTION_DONE ->{
                    btnUserPurchase.callOnClick()
                    true
                }
                else -> false
            }
        }
    }

    // show plain text when user choosing bank
    private fun showEditTextWhenChooseBank() {
        rdBIDV.setOnClickListener {
            appearView()
        }
        rdAgribank.setOnClickListener {
            appearView()
        }
        rdVietcombank.setOnClickListener {
            appearView()
        }
    }

    // clear all error and edit text
    private fun clearAllView(){
        tilUserPurchase.error = null
        edtUserPurchase.setText("")
    }

    // button and plain text appear
    @SuppressLint("RestrictedApi")
    private fun appearView(){
        tilUserPurchase.visibility = View.VISIBLE
        btnUserPurchase.visibility = View.VISIBLE
        setViewAnimation(tilUserPurchase)
        setViewAnimation(btnUserPurchase)
    }

    // init all step
    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        rUsername = getRootUsername()
        var intent = intent
        purchaseMoney = intent.getIntExtra("purchaseValue",0)
        txtUserPurchase.text = "${DecimalFormat("###,###,###").format(purchaseMoney)} VND"
    }

    // Set animation for back button
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
