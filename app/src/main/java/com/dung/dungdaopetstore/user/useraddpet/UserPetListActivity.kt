package com.dung.dungdaopetstore.user.useraddpet

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.UserPetListAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.OwnerDatabase
import com.dung.dungdaopetstore.model.Owner
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_pet_list.*
import kotlinx.android.synthetic.main.dialog_new_feed_pet_image.view.*

class UserPetListActivity : BaseActivity() {

    lateinit var list: ArrayList<Owner>
    lateinit var adapter: UserPetListAdapter
    lateinit var rUsername: String
    lateinit var ownerDatabase: OwnerDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pet_list)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        initView()
        addPetList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_test,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.menuAddPet){
            startActivity(Intent(this@UserPetListActivity,UserAddPetActivity::class.java))
        }
        return true
    }

    private fun addPetList() {
        ownerDatabase.getAllMyOwner(list,adapter,rUsername)
    }

    private fun initView() {
        rUsername = getRootUsername()
        list = ArrayList()
        adapter = UserPetListAdapter(this,list)
        rvUserPetList.layoutManager = LinearLayoutManager(this)
        rvUserPetList.adapter = adapter
        ownerDatabase = OwnerDatabase(this)
    }

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
    }

}
