package com.dung.dungdaopetstore.firebase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.dung.dungdaopetstore.adapter.staff.StaffUserAdapter
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.util.*

class UserDatabase(var context: Context) {

//    fun checkSignUp(username: String): Boolean{
//        var result = true
//        var mData = FirebaseDatabase.getInstance().reference
//        var query = mData.child(Constants().userTable).orderByChild("username").equalTo(username)
//
//        query.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//            override fun onDataChange(p0: DataSnapshot) {
//                if(p0.childrenCount > 0){
//                    context.showBoolean(false)
//                    result = false
//                }else{
//                    context.showBoolean(true)
//                }
//            }
//        })
//        return result
//    }

    fun insertUser(img: CircleImageView, fullname: String, username: String,
                   password: String, money: Int, bannedTime: Int, tradeTime: Int, phoneNumber: String, address: String): Boolean{
        var result = true

        var mData = FirebaseDatabase.getInstance().reference
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
            result = false
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                var downloadURL = it.toString()
                mData.child(Constants().userTable).child(username).setValue(
                    User(fullname,username,password,downloadURL, money,
                        bannedTime, tradeTime,phoneNumber,"$username-$password",false, address)
                ).addOnFailureListener { result = false }
            }
        }

        return result
    }

    fun getAllUser(list: ArrayList<User>, adapter: StaffUserAdapter){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().userTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var user = it.getValue(User::class.java)
                    list.add(user!!)
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    fun getUserInformation(rUsername: String, u: TextView, img: ImageView, m: TextView,
                           f: TextView, p: TextView, t: TextView,a: TextView, times: String,
                           hideshowimg: ImageView,background: Int,background1: Int){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().userTable).child(rUsername)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    var money = "*"
                    u.text = user!!.username
                    Picasso.get().load(user.image).into(img)
                    for(i in 2..user.money.toString().length){
                        money = money + "*"
                    }
                    var fm = DecimalFormat("###,###,###")
                    m.text = "${fm.format(user.money)} VND"
                    var hideshow = true
                    hideshowimg.setOnClickListener {
                        if(hideshow == true){
                            hideshow = false
                            m.text = "${money} VND"
                            hideshowimg.setImageResource(background)
                        }else{
                            hideshow = true
                            m.text = "${fm.format(user.money)} VND"
                            hideshowimg.setImageResource(background1)
                            m.text = "${fm.format(user.money)} VND"
                        }
                    }
                    f.text = user.fullname
                    p.text = user.phoneNumber
                    t.text = "${user.tradeTime} $times"
                    a.text = user.address
                }

            })
    }


    fun getUserInformation1(f: TextView, a: TextView, p: TextView, pw: TextView, rUsername: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().userTable).child(rUsername)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    f.setText(user!!.fullname)
                    a.setText(user.address)
                    p.setText(user.phoneNumber)
                    pw.setText(user.password)
                }

            })
    }

}