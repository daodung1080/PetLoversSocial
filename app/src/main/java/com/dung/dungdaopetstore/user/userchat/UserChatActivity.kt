package com.dung.dungdaopetstore.user.userchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserChatAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.model.Chat
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_chat.*
import kotlinx.android.synthetic.main.content_chat.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class UserChatActivity : BaseActivity() {

    lateinit var list: ArrayList<Chat>
    lateinit var adapter: UserChatAdapter
    lateinit var rUsername: String
    lateinit var mData: DatabaseReference
    lateinit var receiver: String

    // request code int
    var REQUEST_CODE_CAMERA = 123
    var MY_PERMISSIONS_REQUEST_CAMERA = 11

    var REQUEST_CODE_FOLDER = 456
    var MY_PERMISSIONS_REQUEST_READ_STORAGE = 22

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)

        // register toolbar and create new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)
        activityAnim(this)

        setFunctionForSendChatButton()
        initView()
        getUserProfile()
        sendMessage()
        getAllMessage()

    }

    // Set function for send chat button
    private fun setFunctionForSendChatButton() {
        // Register context menu for image button
        registerForContextMenu(imgChat)
        imgChat.setOnClickListener {
            // open context menu when click button
            openContextMenu(imgChat)
        }
    }

    // set physical for back button like the same android original back button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }

    // get all user profile include Full name and Image then put into View
    private fun getUserProfile() {
        mData.child(Constants().userTable).child(receiver)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    if(user != null){
                        Picasso.get().load(user.image).into(imgUserChat)
                        txtUserChat.text = user.fullname
                    }else{
                        txtUserChat.text = resources.getString(R.string.title_pet_lovers)
                        imgUserChat.setImageResource(R.drawable.img_headquarters)
                    }
                }

            })
    }

    // get All message of user
    private fun getAllMessage() {
        mData.child(Constants().chatTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var chat = it.getValue(Chat::class.java)
                    if(chat!!.sender.equals(rUsername) && chat.receiver.equals(receiver) ||
                            chat.sender.equals(receiver) && chat.receiver.equals(rUsername)){
                        list.add(chat)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    // button send message on click
    private fun sendMessage() {
        imgSend.setOnClickListener {
            validation()
        }
    }

    // check form validate when clicked Send Button
    private fun validation() {
        var message = edtMessage.text.toString()
        if(message.isEmpty()){

        }else{
            mData.child(Constants().chatTable).push().setValue(Chat(message,receiver,rUsername))
            edtMessage.setText("")
        }
    }

    // init All view and Class
    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserChatAdapter(this, list, rUsername)
        // set adapter for recyclerView
        var linearLayoutManager = LinearLayoutManager(this)
        rvChat.layoutManager = linearLayoutManager
        rvChat.adapter = adapter
        // get receiver from sender activity
        receiver = intent.getStringExtra("people")
    }

    // Back Button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
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
            uploadPictureToDatabase(bitmap)
        }else if(requestCode == REQUEST_CODE_FOLDER && resultCode == Activity.RESULT_OK && data != null){
            var uri: Uri = data.data
            try {
                var inputStream: InputStream = this.contentResolver.openInputStream(uri)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                uploadPictureToDatabase(bitmap)
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

    // send picture message to Database
    fun uploadPictureToDatabase(image: Bitmap){
        var mStorage = FirebaseStorage.getInstance("gs://dungdaopetstore.appspot.com")

        var sRef = mStorage.reference
        var milis = Calendar.getInstance().timeInMillis
        var imageRef = sRef.child("user$milis.png")

        var baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        var data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.e("errorUploadImage","ErrorUpLoadImage")
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                var downloadURL = it.toString()
                mData.child(Constants().chatTable)
                    .push()
                    .setValue(Chat(downloadURL,receiver,rUsername,true))
                    .addOnFailureListener { showMessage(resources.getString(R.string.checkInternet),false) }
            }
        }
    }

}
