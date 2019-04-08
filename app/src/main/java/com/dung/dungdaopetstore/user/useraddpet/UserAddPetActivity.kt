package com.dung.dungdaopetstore.user.useraddpet

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.OwnerDatabase
import kotlinx.android.synthetic.main.activity_user_add_pet.*
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.NumberFormatException

class UserAddPetActivity : BaseActivity() {

    var REQUEST_CODE_CAMERA = 123
    var MY_PERMISSIONS_REQUEST_CAMERA = 11

    var REQUEST_CODE_FOLDER = 456
    var MY_PERMISSIONS_REQUEST_READ_STORAGE = 22

    lateinit var ownerDatabase: OwnerDatabase
    lateinit var sharedPreferences: SharedPreferences
    lateinit var username: String

    lateinit var spnList: List<String>
    lateinit var spnAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_add_pet)

        // Create toolbar with new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        // Config animation when switch activity
        activityAnim(this)

        ownerDatabase = OwnerDatabase(this)
        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username","")

        registerContextMenu()
        onClickConfirm()
        initSpinner()
        imeOption(edtUserSellWeight,btnUserSellConfirm)

    }

    // create spinner of Pet Category
    private fun initSpinner() {
        spnList = listOf("Dog","Cat","Fish","Turtle","Mouse","Bird","Difference")
        spnAdapter = ArrayAdapter(this, R.layout.spinner_custom_text, spnList)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnUserSellCategory.setAdapter(spnAdapter)
    }

    // Button confirm call on click
    private fun onClickConfirm() {
        btnUserSellConfirm.setOnClickListener {
            validationAnimal()
        }
    }

    // register context menu for Image
    fun registerContextMenu(){
        registerForContextMenu(imgUserSellAddition)
        imgUserSellAddition.setOnClickListener {
            openContextMenu(imgUserSellAddition)
        }
    }

    // check form validate when clicked button Add Pet
    fun validationAnimal(){
        try {
            var aName = edtUserSellName.text.toString()
            var aWeight = (edtUserSellWeight.text.toString()).toInt()
            var aCategory = spnList.get(spnUserSellCategory.selectedItemPosition)
            var aGender = "Female"
            if(rdMale.isChecked){
                aGender = "Male"
            }

            if(aName.length < 5){
                clearAllTextInputLayout()
                tilUserSellName.error = resources.getString(R.string.errorAnimalNameRestrict)
            }else if(aWeight < 0){
                tilUserSellName.error = null
                tilUserSellWeight.error = resources.getString(R.string.errorWeight)
            }else if(ownerDatabase.insertOwner(username,aName,aGender,aWeight,aCategory,imgUserSellAddition) == true){
                clearAllEditText()
                clearAllTextInputLayout()
                imgUserSellAddition.setImageResource(R.drawable.img_addition)
                showMessage(resources.getString(R.string.completeAnimalAdded),true)
                onBackPressed()
            }else if(ownerDatabase.insertOwner(username,aName,aGender,aWeight,aCategory,imgUserSellAddition) == false){
                clearAllTextInputLayout()
                showMessage(resources.getString(R.string.failedAnimalAdded),false)
            }
        }catch (e: NumberFormatException){
            showMessage(resources.getString(R.string.errorNumberFormat1), false)
        }
    }

    // clear all edit text when finishing Addition
    fun clearAllEditText(){
        edtUserSellName.setText("")
        edtUserSellWeight.setText("")
    }

    // clear all text input layout error when finishing Addition
    fun clearAllTextInputLayout(){
        tilUserSellName.error = null
        tilUserSellWeight.error = null
    }

    // create context menu when clicked Image
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_user_trade, menu)
        menu!!.setHeaderTitle(resources.getString(R.string.menuTitle))
        menu!!.setHeaderIcon(R.drawable.img_camera)
    }

    // context menu item Function
    override fun onContextItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == R.id.menuCamera){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA),
                        MY_PERMISSIONS_REQUEST_CAMERA)
                }
            }
            else{
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CODE_CAMERA)
            }
        }else if(item.itemId == R.id.menuFolder){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_CAMERA)
                }
            }
            else{
                var intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_FOLDER)
            }
        }else{
            return false
        }
        return true
    }


    // get image from Camera or Folder
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            var bitmap: Bitmap = data.extras.get("data") as Bitmap
            imgUserSellAddition.setImageBitmap(bitmap)
        }else if(requestCode == REQUEST_CODE_FOLDER && resultCode == Activity.RESULT_OK && data != null){
            var uri: Uri = data.data
            try {
                var inputStream: InputStream = this.contentResolver.openInputStream(uri)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                imgUserSellAddition.setImageBitmap(bitmap)
            }catch(e: FileNotFoundException){

            }
        }
    }

    // Get Permission for camera or folder
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                }
                return
            }
            MY_PERMISSIONS_REQUEST_READ_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                }
                return
            }

            else -> {
            }
        }
    }

    // Button Back Animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
