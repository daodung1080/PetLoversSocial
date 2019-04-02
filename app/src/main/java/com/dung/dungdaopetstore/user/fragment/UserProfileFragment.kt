package com.dung.dungdaopetstore.user.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.user.useraddpet.UserPetListActivity
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.UserDatabase
import com.dung.dungdaopetstore.user.userpurchase.UserPurchaseActivity
import com.dung.dungdaopetstore.user.userupdate.UserUpdateActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class UserProfileFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var imgProfileUser: ImageView
    lateinit var txtProfileUsername: TextView
    lateinit var txtProfileMoney: TextView
    lateinit var txtProfileFullname: TextView
    lateinit var txtProfilePhone: TextView
    lateinit var txtProfileTradeTime: TextView
    lateinit var txtProfileLocation: TextView
    lateinit var userDatabase: UserDatabase
    lateinit var rUsername: String
    lateinit var cvProfileAddPet: CardView
    lateinit var cvProfileUpdate: CardView
    lateinit var cvProfilePurchase: CardView
    lateinit var imgProfileHide: ImageView
    var hideshow = true

    lateinit var mData: DatabaseReference

    var REQUEST_CODE_CAMERA = 123
    var MY_PERMISSIONS_REQUEST_CAMERA = 11

    var REQUEST_CODE_FOLDER = 456
    var MY_PERMISSIONS_REQUEST_READ_STORAGE = 22

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_user_profile, container, false)

        initView()
        registerContextMenu()
        getUserInformation()
        selectFunction()

        return rootview

    }

    private fun onclickHideShow() {
        imgProfileHide.setOnClickListener {
            if(hideshow == true){
                hideshow = false
            }else{
                hideshow = true
            }
        }
    }

    private fun registerContextMenu() {
        registerForContextMenu(imgProfileUser)
        imgProfileUser.setOnClickListener {
            activity!!.openContextMenu(imgProfileUser)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity!!.menuInflater.inflate(R.menu.menu_user_trade, menu)
        menu!!.setHeaderTitle(resources.getString(R.string.menuTitle))
        menu!!.setHeaderIcon(R.drawable.img_camera)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == R.id.menuCamera){
            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                        Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.CAMERA),
                        MY_PERMISSIONS_REQUEST_CAMERA)
                }
            }
            else{
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CODE_CAMERA)
            }
        }else if(item.itemId == R.id.menuFolder){
            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(activity!!,
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
            imgProfileUser.setImageBitmap(bitmap)
            this.uploadImageAndSaveIntoDataBase(imgProfleUser)
        }else if(requestCode == REQUEST_CODE_FOLDER && resultCode == Activity.RESULT_OK && data != null){
            var uri: Uri = data.data
            try {
                var inputStream: InputStream = activity!!.contentResolver.openInputStream(uri)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                imgProfileUser.setImageBitmap(bitmap)
                this.uploadImageAndSaveIntoDataBase(imgProfleUser)
            }catch(e: FileNotFoundException){

            }
        }
    }

    fun uploadImageAndSaveIntoDataBase(img: CircleImageView){
        var mStorage = FirebaseStorage.getInstance("gs://dungdaopetstore.appspot.com")

        var sRef = mStorage.reference
        var milis = Calendar.getInstance().timeInMillis
        var imageRef = sRef.child("user$milis.png")

        img.isDrawingCacheEnabled
        img.buildDrawingCache()
        var bitmap = (img.drawable as BitmapDrawable).bitmap
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        var data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.e("errorUploadImage","ErrorUpLoadImage")
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                var downloadURL = it.toString()
                mData.child(Constants().userTable).child(rUsername).child("image")
                    .setValue(downloadURL)
                    .addOnFailureListener { showMessage(resources.getString(R.string.imgProfileFailed),false) }
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

    private fun selectFunction() {
        cvProfileAddPet.setOnClickListener {
            startActivity(Intent(context, UserPetListActivity::class.java))
        }
        cvProfileUpdate.setOnClickListener {
            startActivity(Intent(context, UserUpdateActivity::class.java))
        }
        cvProfilePurchase.setOnClickListener {
            startActivity(Intent(context, UserPurchaseActivity::class.java))
        }
    }

    private fun getUserInformation() {
        userDatabase.getUserInformation(rUsername,txtProfileUsername,imgProfileUser,txtProfileMoney,
            txtProfileFullname,txtProfilePhone,txtProfileTradeTime,txtProfileLocation,context!!.resources.getString(R.string.txtTimes),imgProfileHide,
            R.drawable.img_show, R.drawable.img_hide)
    }

    private fun initView() {
        rUsername = getRootUsername()
        userDatabase = UserDatabase(context!!)
        imgProfileUser = rootview.imgProfleUser
        txtProfileUsername = rootview.txtProfileUsername
        txtProfileMoney = rootview.txtProfileMoney
        txtProfileFullname = rootview.txtProfileFullname
        txtProfilePhone = rootview.txtProfilePhone
        txtProfileTradeTime = rootview.txtProfileTradeTime
        txtProfileLocation = rootview.txtProfileLocation
        cvProfileAddPet = rootview.cvProfleAddPet
        cvProfileUpdate = rootview.cvProfileUpdate
        cvProfilePurchase = rootview.cvProfilePurchase
        imgProfileHide = rootview.imgProfileHide
        mData = FirebaseDatabase.getInstance().reference
    }

}