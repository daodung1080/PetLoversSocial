package com.dung.dungdaopetstore.firebase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.StaffUserAdapter
import com.dung.dungdaopetstore.loginsignup.SignUpActivity
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_sign_up.*
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
                   password: String, money: Int, bannedTime: Int, tradeTime: Int, phoneNumber: String): Boolean{
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
                        bannedTime, tradeTime,phoneNumber,"$username-$password",false)
                ).addOnFailureListener { result = false }
            }
        }

        return result
    }

    fun getAllUser(list: ArrayList<User>, adapter: StaffUserAdapter){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().userTable).addChildEventListener(object: ChildEventListener{
            var totalChild = -1
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var user = p0.getValue(User::class.java)
                for(i in 0..totalChild){
                    var same = list.get(i)
                    if((user!!.username).equals(same.username) ){
                        var position = i
                        list.set(position, user)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var user = p0.getValue(User::class.java)
                list.add(user!!)
                adapter.notifyDataSetChanged()
                totalChild++
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var user = p0.getValue(User::class.java)
                for(i in 0..totalChild){
                    var same = list.get(i)
                    if((user!!.username).equals(same.username) ){
                        var position = i
                        list.removeAt(position)
                        adapter.notifyDataSetChanged()
                        totalChild--
                    }
                }
            }

        })
    }

    fun getUserInformation(rUsername: String, u: TextView, img: ImageView, m: TextView,
                           f: TextView, p: TextView, t: TextView, times: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().userTable).child(rUsername)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    u.text = user!!.username
                    Picasso.get().load(user.image).into(img)
                    var fm = DecimalFormat("###,###,###")
                    m.text = "${fm.format(user.money)} VND"
                    f.text = user.fullname
                    p.text = user.phoneNumber
                    t.text = "${user.tradeTime} $times"
                }

            })
    }

}