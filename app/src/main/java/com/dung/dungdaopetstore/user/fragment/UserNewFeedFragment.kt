package com.dung.dungdaopetstore.user.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.NewFeedAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.NewFeedDatabase
import com.dung.dungdaopetstore.model.NewFeed
import com.dung.dungdaopetstore.user.userchat.UserChatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_new_feed_pet_image.view.*
import kotlinx.android.synthetic.main.fragment_user_new_feed.view.*
import java.util.*

class UserNewFeedFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rvNewFeed: RecyclerView
    lateinit var list: ArrayList<NewFeed>
    lateinit var adapter: NewFeedAdapter
    lateinit var newFeedDatabase: NewFeedDatabase
    lateinit var rUsername: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_user_new_feed, container, false)

        initView()
        createNewFeed()

        return rootview

    }

    private fun createNewFeed() {
        rUsername = getRootUsername()
        newFeedDatabase = NewFeedDatabase(context!!)
        list = ArrayList()
        adapter = NewFeedAdapter(context!!, list, this)
        rvNewFeed.layoutManager = LinearLayoutManager(context)
        rvNewFeed.setHasFixedSize(true)
        rvNewFeed.adapter = adapter
        setAnimForView(rvNewFeed)

        newFeedDatabase.getAllNewFeed(list,adapter)

    }

    private fun initView() {
        rvNewFeed = rootview.rvNewFeed
    }

    fun getPetImage(position: Int){
        var newFeed = list.get(position)
        var alertDialog = AlertDialog.Builder(context)
        var view = layoutInflater.inflate(R.layout.dialog_new_feed_pet_image,null)
        var imgDialogNewFeedPetImage = view.imgDialogNewFeedPetImage
        Picasso.get().load(newFeed.petImage).into(imgDialogNewFeedPetImage)
        var anim = AnimationUtils.loadAnimation(context,R.anim.anim_scale_up_image)
        imgDialogNewFeedPetImage.startAnimation(anim)
        alertDialog.setView(view)
        var dialog = alertDialog.create()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dissmissDialog(dialog,imgDialogNewFeedPetImage)
    }

    fun dissmissDialog(alertDialog: AlertDialog, img: ImageView){
        img.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    fun meetPeople(position: Int){
        var intent = Intent(context, UserChatActivity::class.java)
        var newfeed = list.get(position)
        if(newfeed.username.equals(rUsername)){
            showMessage(resources.getString(R.string.errorChat),false)
        }else{
            intent.putExtra("people",newfeed.username)
            startActivity(intent)
        }
    }

}