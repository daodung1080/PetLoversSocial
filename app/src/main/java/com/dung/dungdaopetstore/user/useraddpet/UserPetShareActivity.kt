package com.dung.dungdaopetstore.user.useraddpet

import android.os.Bundle
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.NewFeed
import com.dung.dungdaopetstore.model.Owner
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_pet_share.*
import java.text.SimpleDateFormat
import java.util.*

class UserPetShareActivity : BaseActivity() {

    lateinit var rUsername: String
    lateinit var ownerID: String
    lateinit var mData: DatabaseReference

    lateinit var petImage: String
    lateinit var petName: String
    lateinit var petGender: String
    lateinit var petWeight: String
    lateinit var petCategory: String
    lateinit var userFullname: String
    lateinit var userAddress: String
    lateinit var userPhone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pet_share)

        // Create toolbar with new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        // Config animation when switch activity
        activityAnim(this)

        initView()
        getPetInformation()
        getUserInformation()
        tagOnClick()
        shareOnClick()

    }

    // Button share clicked
    private fun shareOnClick() {
        fabPetShare.setOnClickListener {
            validation()
        }
    }

    // check form validate when user clicked Share button
    private fun validation() {
        var title = edtPetShareTitle.text.toString()
        var describe = edtPetShareDescribe.text.toString()
        if(title.length <= 0 || title.length > 40){
            clearAllTextInputLayout()
            tilPetShareTitle.error = resources.getString(R.string.errorShareTitle)
        }else if(describe.length <= 0 || describe.length > 250){
            tilPetShareTitle.error = null
            tilPetShareDescribe.error = resources.getString(R.string.errorShareDescribe)
        }else{
            var newfeedDate = SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(Calendar.getInstance().time)
            mData.child(Constants().newfeedTable).child(newfeedDate)
                .setValue(NewFeed(petImage,title,describe,newfeedDate,rUsername))
                .addOnFailureListener {
                    clearAllTextInputLayout()
                    showMessage(resources.getString(R.string.errorShareFailed),false)
                }
                .addOnSuccessListener {
                    clearAllTextInputLayout()
                    clearAllEditText()
                    showMessage(resources.getString(R.string.errorShareComplete),true)
                }
        }
    }

    // Clear all edit text when finishing share
    fun clearAllEditText(){
        edtPetShareTitle.setText("")
        edtPetShareDescribe.setText("")
    }

    // Clear all text input layout error when finishing share
    fun clearAllTextInputLayout(){
        tilPetShareTitle.error = null
        tilPetShareDescribe.error = null
    }

    // init All Tag
    private fun tagOnClick() {
        cvPetShareName.setOnClickListener {
            var oldContent = edtPetShareDescribe.text.toString()
            var content = "$oldContent $petName"
            edtPetShareDescribe.setText(content)
        }
        cvPetShareCategory.setOnClickListener {
            var oldContent = edtPetShareDescribe.text.toString()
            var content = "$oldContent $petCategory"
            edtPetShareDescribe.setText(content)
        }
        cvPetShareGender.setOnClickListener {
            var oldContent = edtPetShareDescribe.text.toString()
            var content = "$oldContent $petGender"
            edtPetShareDescribe.setText(content)
        }
        cvPetShareWeight.setOnClickListener {
            var oldContent = edtPetShareDescribe.text.toString()
            var content = "$oldContent $petWeight kg"
            edtPetShareDescribe.setText(content)
        }
        cvPetShareUserName.setOnClickListener {
            var oldContent = edtPetShareDescribe.text.toString()
            var content = "$oldContent $userFullname"
            edtPetShareDescribe.setText(content)
        }
        cvPetShareUserAddress.setOnClickListener {
            var oldContent = edtPetShareDescribe.text.toString()
            var content = "$oldContent $userAddress"
            edtPetShareDescribe.setText(content)
        }
        cvPetShareUserPhone.setOnClickListener {
            var oldContent = edtPetShareDescribe.text.toString()
            var content = "$oldContent $userPhone"
            edtPetShareDescribe.setText(content)
        }
    }

    // Get user information
    private fun getUserInformation() {
        mData.child(Constants().userTable).child(rUsername)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    userFullname = user!!.fullname
                    userAddress = user.address
                    userPhone = user.phoneNumber
                }

            })
    }

    // get pet Infomation
    private fun getPetInformation() {
        mData.child(Constants().ownerTable).child(ownerID)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var owner = p0.getValue(Owner::class.java)
                    petImage = owner!!.petImage
                    Picasso.get().load(owner.petImage).into(imgPetShare)
                    petName = owner.petName
                    petWeight = owner.petWeight.toString()
                    petCategory = owner.petCategory
                    petGender = owner.petGender
                }

            })
    }

    // init all View and Class
    private fun initView() {
        rUsername = getRootUsername()
        var intent = intent
        ownerID = intent.getStringExtra("ownerID")
        mData = FirebaseDatabase.getInstance().reference
    }

    // Back Button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
