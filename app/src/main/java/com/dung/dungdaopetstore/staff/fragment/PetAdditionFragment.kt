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
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.*
import co.ceryle.radiorealbutton.RadioRealButton
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.PetDatabase
import kotlinx.android.synthetic.main.fragment_staff_pet_addition.view.*
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.NumberFormatException


class PetAdditionFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var imgAddition: ImageView

    lateinit var tilAnimalName: TextInputLayout
    lateinit var tilAnimalPrice: TextInputLayout
    lateinit var tilAnimalAmount: TextInputLayout
    lateinit var tilAnimalWeight: TextInputLayout

    lateinit var edtAnimalName: TextInputEditText
    lateinit var edtAnimalPrice: TextInputEditText
    lateinit var edtAnimalAmount: TextInputEditText
    lateinit var edtAnimalWeight: TextInputEditText

    lateinit var spnAnimalCategory: Spinner
    lateinit var spnList: List<String>
    lateinit var spnAdapter: ArrayAdapter<String>

    lateinit var btnAnimalConfirm: Button
    lateinit var rdMale: RadioRealButton

    var REQUEST_CODE_CAMERA = 123
    var REQUEST_CODE_FOLDER = 456
    var MY_PERMISSIONS_REQUEST_CAMERA = 11
    var MY_PERMISSIONS_REQUEST_READ_STORAGE = 22

    lateinit var petDatabase: PetDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_staff_pet_addition, container, false)

        initView()
        registerContextMenu()
        initSpinner()

        imeOption(edtAnimalWeight,btnAnimalConfirm)

        return rootview
    }

    private fun initSpinner() {
        spnAnimalCategory = rootview.spnAnimalCategory
        spnList = listOf(
            resources.getString(R.string.dog),
            resources.getString(R.string.cat),
            resources.getString(R.string.fish),
            resources.getString(R.string.turtle),
            resources.getString(R.string.mouse),
            resources.getString(R.string.bird),
            resources.getString(R.string.another)
        )
        spnAdapter = ArrayAdapter(context, R.layout.spinner_custom_text, spnList)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnAnimalCategory.adapter = spnAdapter
    }

    fun validationAnimal() {
        try {
            var aName = edtAnimalName.text.toString()
            var aPrice = (edtAnimalPrice.text.toString()).toDouble()
            var aAmount = (edtAnimalAmount.text.toString()).toInt()
            var aWeight = (edtAnimalWeight.text.toString()).toInt()
            var aCategory = spnList.get(spnAnimalCategory.selectedItemPosition)
            var aGender = "Female"
            if (rdMale.isChecked) {
                aGender = "Male"
            }

            if (aName.length < 5) {
                clearAllTextInputLayout()
                tilAnimalName.error = resources.getString(R.string.errorAnimalNameRestrict)
            } else if (aPrice < 10000) {
                tilAnimalName.error = null
                tilAnimalPrice.error = resources.getString(R.string.errorPrice)
            } else if (aAmount < 0) {
                tilAnimalPrice.error = null
                tilAnimalAmount.error = resources.getString(R.string.errorAmount)
            } else if (aWeight < 0) {
                tilAnimalAmount.error = null
                tilAnimalWeight.error = resources.getString(R.string.errorWeight)
            } else if (petDatabase.insertAnimal(
                    aName, aGender, aPrice, aAmount, imgAddition, true
                    , "Pet Lovers headquarters Store", aWeight, aCategory
                ) == true) {
                clearAllEditText()
                clearAllTextInputLayout()
                imgAddition.setImageResource(R.drawable.img_addition)
                showMessage(resources.getString(R.string.completeAnimalAdded), true)
            } else if (petDatabase.insertAnimal(
                    aName, aGender, aPrice, aAmount, imgAddition, true
                    , "abc", aWeight, aCategory
                ) == false
            ) {
                clearAllTextInputLayout()
                showMessage(resources.getString(R.string.failedAnimalAdded), false)
            }
        } catch (e: NumberFormatException) {
            showMessage(resources.getString(R.string.errorNumberFormat), false)
        }
    }

    fun clearAllTextInputLayout() {
        tilAnimalName.error = null
        tilAnimalPrice.error = null
        tilAnimalAmount.error = null
        tilAnimalWeight.error = null
    }

    fun clearAllEditText() {
        edtAnimalName.setText("")
        edtAnimalPrice.setText("")
        edtAnimalAmount.setText("")
        edtAnimalWeight.setText("")
    }

    fun initView() {
        edtAnimalWeight = rootview.edtAnimalWeight
        petDatabase = PetDatabase(context!!)
        imgAddition = rootview.imgAddition

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
    }

        fun registerContextMenu() {
            registerForContextMenu(imgAddition)
            imgAddition.setOnClickListener {
                activity!!.openContextMenu(imgAddition)
            }
        }


        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            super.onCreateContextMenu(menu, v, menuInfo)
            activity!!.menuInflater.inflate(R.menu.menu_user_trade, menu)
            menu!!.setHeaderTitle(resources.getString(R.string.menuTitle))
            menu!!.setHeaderIcon(R.drawable.img_camera)
        }

        override fun onContextItemSelected(item: MenuItem?): Boolean {

            if (item!!.itemId == R.id.menuCamera) {
                if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity!!,
                            Manifest.permission.CAMERA
                        )
                    ) {
                    } else {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_CAMERA
                        )
                    }
                } else {
                    var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CODE_CAMERA)
                }
            } else if (item.itemId == R.id.menuFolder) {
                if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity!!,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                    } else {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_CAMERA
                        )
                    }
                } else {
                    var intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_CODE_FOLDER)
                }
            } else {
                return false
            }
            return true
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
                var bitmap: Bitmap = data.extras.get("data") as Bitmap
                imgAddition.setImageBitmap(bitmap)
            } else if (requestCode == REQUEST_CODE_FOLDER && resultCode == Activity.RESULT_OK && data != null) {
                var uri: Uri = data.data
                try {
                    var inputStream: InputStream = activity!!.contentResolver.openInputStream(uri)
                    var bitmap = BitmapFactory.decodeStream(inputStream)
                    imgAddition.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {

                }
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
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