package com.dung.dungdaopetstore.firebase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.dung.dungdaopetstore.adapter.UserPetListAdapter
import com.dung.dungdaopetstore.adapter.UserSellAdapter
import com.dung.dungdaopetstore.model.Animal
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

    fun getAllMyOwner(list: ArrayList<Owner>,adapter: UserPetListAdapter, rUsername: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().ownerTable).orderByChild("username").equalTo(rUsername)
            .addChildEventListener(object: ChildEventListener{
                var totalCount = -1
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                    var owner = p0.getValue(Owner::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((owner!!.ownerID).equals(same.ownerID) ){
                            var position = i
                            list.set(position, owner)
                            adapter.notifyDataSetChanged()
                        }
                    }

                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var owner = p0.getValue(Owner::class.java)
                    list.add(owner!!)
                    adapter.notifyDataSetChanged()

                    totalCount++
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    var owner = p0.getValue(Owner::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((owner!!.ownerID).equals(same.ownerID) ){
                            var position = i
                            list.removeAt(position)
                            adapter.notifyDataSetChanged()
                            totalCount--
                        }
                    }
                }

            })
    }

    fun getAllMyOwner1(list: ArrayList<Owner>,adapter: UserSellAdapter, rUsername: String,checkList: TextView){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().ownerTable).orderByChild("username").equalTo(rUsername)
            .addChildEventListener(object: ChildEventListener{
                var totalCount = -1
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                    var owner = p0.getValue(Owner::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((owner!!.ownerID).equals(same.ownerID) ){
                            var position = i
                            list.set(position, owner)
                            adapter.notifyDataSetChanged()
                        }
                    }

                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var owner = p0.getValue(Owner::class.java)
                    list.add(owner!!)
                    adapter.notifyDataSetChanged()

                    if(p0.childrenCount <= 0){
                        checkList.text = "Please add pet first then you can sell it"
                    }else{
                        checkList.text = "Select a pet you wanna sell"
                    }

                    totalCount++
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    var owner = p0.getValue(Owner::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((owner!!.ownerID).equals(same.ownerID) ){
                            var position = i
                            list.removeAt(position)
                            adapter.notifyDataSetChanged()
                            totalCount--
                        }
                    }
                }

            })
    }

}