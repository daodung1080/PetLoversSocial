package com.dung.dungdaopetstore.staff.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseFragment
import android.widget.ImageView
import android.widget.RadioButton
import com.dung.dungdaopetstore.firebase.PetDatabase
import kotlinx.android.synthetic.main.fragment_staff_pet_addition.view.*
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.NumberFormatException


class PetAdditionFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var imgAddition: ImageView
    lateinit var imgCamera: ImageView
    lateinit var imgFolder: ImageView

    lateinit var tilAnimalName: TextInputLayout
    lateinit var tilAnimalPrice: TextInputLayout
    lateinit var tilAnimalAmount: TextInputLayout

    lateinit var edtAnimalName: EditText
    lateinit var edtAnimalPrice: EditText
    lateinit var edtAnimalAmount: EditText

    lateinit var btnAnimalConfirm: Button
    lateinit var rdMale: RadioButton

    var REQUEST_CODE_CAMERA = 123
    var REQUEST_CODE_FOLDER = 456
    var MY_PERMISSIONS_REQUEST_CAMERA = 11
    var MY_PERMISSIONS_REQUEST_READ_STORAGE = 22

    lateinit var petDatabase: PetDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_staff_pet_addition, container,false)

        initView()

        return rootview
    }

    fun validationAnimal(){
        try {
            var aName = edtAnimalName.text.toString()
            var aPrice = (edtAnimalPrice.text.toString()).toDouble()
            var aAmount = (edtAnimalAmount.text.toString()).toInt()
            var aGender = "Female"
            if(rdMale.isChecked){
                aGender = "Male"
            }

            if(aName.length < 5){
                clearAllTextInputLayout()
                tilAnimalName.error = resources.getString(R.string.errorAnimalNameRestrict)
            }else if(aPrice < 10000){
                tilAnimalName.error = null
                tilAnimalPrice.error = resources.getString(R.string.errorPrice)
            }else if(aAmount < 0){
                tilAnimalPrice.error = null
                tilAnimalAmount.error = resources.getString(R.string.errorAmount)
            }else if(petDatabase.insertAnimal(aName, aGender, aPrice, aAmount, imgAddition, true, "Pet Lovers headquarters Store") == true){
                clearAllEditText()
                clearAllTextInputLayout()
                imgAddition.setImageResource(R.drawable.img_addition)
                showMessage(resources.getString(R.string.completeAnimalAdded),true)
            }else if(petDatabase.insertAnimal(aName, aGender, aPrice, aAmount, imgAddition,true,"abc") == false){
                clearAllTextInputLayout()
                showMessage(resources.getString(R.string.failedAnimalAdded),false)
            }
        }catch (e: NumberFormatException){
            showMessage(resources.getString(R.string.errorNumberFormat), false)
        }
    }

    fun clearAllTextInputLayout(){
        tilAnimalName.error = null
        tilAnimalPrice.error = null
        tilAnimalAmount.error = null
    }

    fun clearAllEditText(){
        edtAnimalName.setText("")
        edtAnimalPrice.setText("")
        edtAnimalAmount.setText("")
    }

    fun initView(){
        petDatabase = PetDatabase(context!!)
        imgAddition = rootview.imgAddition
        imgCamera = rootview.imgCamera
        imgFolder = rootview.imgFolder

        tilAnimalName = rootview.tilAnimalName
        tilAnimalPrice = rootview.tilAnimalPrice
        tilAnimalAmount = rootview.tilAnimalAmount

        edtAnimalName = rootview.edtAnimalName
        edtAnimalPrice = rootview.edtAnimalPrice
        edtAnimalAmount = rootview.edtAnimalAmount

        rdMale = rootview.rdMale
        btnAnimalConfirm = rootview.btnAnimalConfirm

        btnAnimalConfirm.setOnClickListener {
            validationAnimal()
        }

        imgCamera.setOnClickListener {
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
        }

        imgFolder.setOnClickListener {
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
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            var bitmap: Bitmap = data.extras.get("data") as Bitmap
            imgAddition.setImageBitmap(bitmap)
        }else if(requestCode == REQUEST_CODE_FOLDER && resultCode == Activity.RESULT_OK && data != null){
            var uri: Uri = data.data
            try {
                var inputStream: InputStream = context!!.contentResolver.openInputStream(uri)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                imgAddition.setImageBitmap(bitmap)
            }catch(e: FileNotFoundException){

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
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