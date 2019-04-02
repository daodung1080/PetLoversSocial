package com.dung.dungdaopetstore.firebase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.dung.dungdaopetstore.adapter.user.UserPetListAdapter
import com.dung.dungdaopetstore.adapter.user.UserSellAdapter
import com.dung.dungdaopetstore.model.Owner
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class OwnerDatabase(var context: Context) {

    fun insertOwner(rUsername: String,petName: String,petGender: String
                    ,petWeight: Int, petCategory: String,img: ImageView): Boolean{
        var result = true
        var mData: DatabaseReference = FirebaseDatabase.getInstance().reference
        var mStorage: FirebaseStorage = FirebaseStorage.getInstance("gs://dungdaopetstore.appspot.com")

        var sRef = mStorage.reference
        var calendar = Calendar.getInstance()
        var milis = calendar.timeInMillis

        var imageRef = sRef.child("dung$milis.png")
        img.isDrawingCacheEnabled = true
        img.buildDrawingCache()
        var bitmap = (img.drawable as BitmapDrawable).bitmap
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        var data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Log.e("errorUploadImage","ErrorUpLoadImage")
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                var timetoMiliis = Calendar.getInstance().timeInMillis
                var ownerID = "owner$timetoMiliis"
                var downloadURL = it.toString()
                mData.child(Constants().ownerTable).child(ownerID).setValue(
                    Owner(rUsername,petName,petGender,petWeight,petCategory,downloadURL,ownerID)
                )
                    .addOnFailureListener {
                        Log.e("UserMarketError",it.toString())
                    }
            }
        }
        return result
    }

    fun addOwner(rUsername: String,petName: String,petGender: String
                 ,petWeight: Int, petCategory: String,img: String){
        var timetoMiliis = Calendar.getInstance().timeInMillis
        var ownerID = "owner$timetoMiliis"
        var mData: DatabaseReference = FirebaseDatabase.getInstance().reference
        mData.child(Constants().ownerTable).child(ownerID).setValue(
            Owner(rUsername,petName,petGender,petWeight,petCategory,img,ownerID)
        )
    }

    fun getAllMyOwner(list: ArrayList<Owner>, adapter: UserPetListAdapter, rUsername: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().ownerTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var owner = it.getValue(Owner::class.java)
                    if(owner!!.username.equals(rUsername)){
                        list.add(owner)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    fun getAllMyOwner1(list: ArrayList<Owner>, adapter: UserSellAdapter, rUsername: String, checkList: TextView,
                       allowSell: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().ownerTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var owner = it.getValue(Owner::class.java)
                    if(owner!!.username.equals(rUsername)){
                        list.add(owner)
                    }
                }
                if (list.size > 0){
                    checkList.text = allowSell
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

}