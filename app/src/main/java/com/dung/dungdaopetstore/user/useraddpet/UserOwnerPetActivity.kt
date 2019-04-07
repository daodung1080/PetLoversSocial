package com.dung.dungdaopetstore.user.useraddpet

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserPetListAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.OwnerDatabase
import com.dung.dungdaopetstore.model.Owner
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_pet_owner.*
import kotlinx.android.synthetic.main.dialog_new_feed_pet_image.view.*

class UserOwnerPetActivity : BaseActivity() {

    lateinit var list: ArrayList<Owner>
    lateinit var adapter: UserPetListAdapter
    lateinit var rUsername: String
    lateinit var ownerDatabase: OwnerDatabase
    lateinit var mData: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pet_owner)

        // Create toolbar with new back button
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)

        // Config animation when switch activity
        activityAnim(this)

        initView()
        addPetList()

    }

    // Create option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_pet,menu)
        return true
    }

    // option item Function
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.menuAddPet){
            startActivity(Intent(this@UserOwnerPetActivity,UserAddPetActivity::class.java))
        }else if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }

    // get All pet from Owner Database
    private fun addPetList() {
        ownerDatabase.getAllMyOwner(list,adapter,rUsername)
    }

    // init All View and Class
    private fun initView() {
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserPetListAdapter(this, list)
        // set adapter for recycler View
        rvUserPetList.layoutManager = LinearLayoutManager(this)
        rvUserPetList.setHasFixedSize(true)
        rvUserPetList.adapter = adapter
        ownerDatabase = OwnerDatabase(this)
    }

    // show Pet image when clicked pet Image Icon
    fun getPetImage(position: Int){
        var owner = list.get(position)
        var alertDialog = AlertDialog.Builder(this)
        var view = layoutInflater.inflate(R.layout.dialog_new_feed_pet_image,null)
        var imgDialogNewFeedPetImage = view.imgDialogNewFeedPetImage
        Picasso.get().load(owner.petImage).into(imgDialogNewFeedPetImage)
        var anim = AnimationUtils.loadAnimation(this, R.anim.anim_scale_up_image)
        imgDialogNewFeedPetImage.startAnimation(anim)
        alertDialog.setView(view)
        var dialog = alertDialog.create()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dissmissDialog(dialog,imgDialogNewFeedPetImage)
    }

    // dissmiss dialog when user click the image again
    fun dissmissDialog(alertDialog: AlertDialog, img: ImageView){
        img.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    // Show dialog ask user want to remove pet
    fun removePet(position: Int){
        var owner = list.get(position)
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.img_trash)
        alertDialog.setTitle(resources.getString(R.string.titleRemovePet))
        alertDialog.setMessage("${resources.getString(R.string.removePet)} ${owner.petName}?")
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var mData = FirebaseDatabase.getInstance().reference
            // remove pet
            mData.child(Constants().ownerTable).child(owner.ownerID).removeValue()
                .addOnFailureListener {
                    showMessage(resources.getString(R.string.petRemoveFailed),false)
                }
        })
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        var dialog = alertDialog.create()
        dialog.show()
    }

    // switch activity update pet information when click Config Icon
    fun configPet(position: Int){
        var owner = list.get(position)
        var intent = Intent(this@UserOwnerPetActivity, UserPetUpdateActivity::class.java)
        intent.putExtra("ownerID",owner.ownerID)
        startActivity(intent)
    }

    // switch activity share pet when click Share Icon
    fun sharePet(position: Int){
        var owner = list.get(position)
        var intent = Intent(this@UserOwnerPetActivity, UserPetShareActivity::class.java)
        intent.putExtra("ownerID",owner.ownerID)
        startActivity(intent)
    }

    // Back Button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
