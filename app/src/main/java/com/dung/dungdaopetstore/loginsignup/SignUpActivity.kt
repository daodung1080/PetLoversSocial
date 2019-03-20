package com.dung.dungdaopetstore.loginsignup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.UserDatabase
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.FileNotFoundException
import java.io.InputStream

class SignUpActivity : BaseActivity() {

    var REQUEST_CODE_CAMERA = 123
    var MY_PERMISSIONS_REQUEST_CAMERA = 11

    var REQUEST_CODE_FOLDER = 456
    var MY_PERMISSIONS_REQUEST_READ_STORAGE = 22
    lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        registerContextMenu()
        confirmRegistration()

    }

//    fun showBoolean(b: Boolean){
//        showMessage(b.toString(),true)
//    }

    fun confirmRegistration(){
        userDatabase = UserDatabase(this)
        btnUserConfirm.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        var fullname = edtUserFullName.text.toString()
        var username = edtUserUsername.text.toString()
        var password = edtUserPassword.text.toString()
        var phone = edtUserPhone.text.toString()

        if(fullname.length < 5 || fullname.length > 30){
            clearAllTIL()
            tilUserFullName.error = resources.getString(R.string.errorSignUpFullname)
        }else if(username.length < 5 || username.length > 30){
            tilUserFullName.error = null
            tilUserUsername.error = resources.getString(R.string.errorSignUpUsername)
        }else if(password.length < 5 || password.length > 30){
            tilUserUsername.error = null
            tilUserPassword.error = resources.getString(R.string.errorSignUpPassword)
        }else if (phone.length != 10){
            tilUserPassword.error = null
            tilUserPhone.error = resources.getString(R.string.errorSignUpPhone)
        }else{
            checkSignUp(username,fullname,password,phone)
        }
    }

    fun checkSignUp(username: String,fullname: String,password: String, phone: String){
        var mData = FirebaseDatabase.getInstance().reference
        var query = mData.child(Constants().userTable).orderByChild("username").equalTo(username)

        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                clearAllTIL()
                showMessage(resources.getString(R.string.errorSignUpFailed1), false)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.childrenCount > 0){
                    clearAllTIL()
                    showMessage(resources.getString(R.string.errorSignUpFailed), false)
                }else{
                    if(userDatabase.insertUser(imgUserAddition,fullname,username,password
                            , 0, 0 ,0, phone) == true){
                        clearAllEDT()
                        clearAllTIL()
                        showMessage(resources.getString(R.string.errorSignUpComplete), true)
                    }else{
                        showMessage(resources.getString(R.string.errorSignUpFailed1), false)
                    }
                }
            }

        })
    }

    fun clearAllTIL(){
        tilUserFullName.error = null
        tilUserUsername.error = null
        tilUserPassword.error = null
        tilUserPhone.error = null
    }

    fun clearAllEDT(){
        edtUserFullName.setText("")
        edtUserUsername.setText("")
        edtUserPassword.setText("")
        edtUserPhone.setText("")
    }

    fun registerContextMenu(){
        registerForContextMenu(imgUserAddition)
        imgUserAddition.setOnClickListener {
            openContextMenu(imgUserAddition)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_user_trade, menu)
        menu!!.setHeaderTitle(resources.getString(R.string.menuTitle))
        menu!!.setHeaderIcon(R.drawable.img_camera)
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            var bitmap: Bitmap = data.extras.get("data") as Bitmap
            imgUserAddition.setImageBitmap(bitmap)
        }else if(requestCode == REQUEST_CODE_FOLDER && resultCode == Activity.RESULT_OK && data != null){
            var uri: Uri = data.data
            try {
                var inputStream: InputStream = this.contentResolver.openInputStream(uri)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                imgUserAddition.setImageBitmap(bitmap)
            }catch(e: FileNotFoundException){

            }
        }
    }

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



}
