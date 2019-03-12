package com.dung.dungdaopetstore.firebase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.dung.dungdaopetstore.adapter.UserMarketAdapter
import com.dung.dungdaopetstore.model.Animal
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PetDatabase(var context: Context) {

    fun insertAnimal(aName: String, aGender: String, aPrice: Double,
                     aAmount: Int, img: ImageView, confirm: Boolean, seller: String): Boolean{
        var result: Boolean = true
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
                var id = "animal$timetoMiliis"
                var downloadURL = it.toString()
                mData.child(Constants().petTable).child(id).setValue(
                    Animal(id,aName, aGender, aPrice, aAmount, downloadURL,confirm,seller))
                    .addOnFailureListener {
                        result = false
                        Log.e("UserMarketError",it.toString())
                    }
            }
        }

        return result
    }

    fun getAllAnimals(adapter: UserMarketAdapter,list: ArrayList<Animal>){
        var mData = FirebaseDatabase.getInstance().reference

        mData.child(Constants().petTable).addChildEventListener(object : ChildEventListener{
            var totalChild = -1
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var animal = p0.getValue(Animal::class.java)
                for(i in 0..totalChild){
                    var same = list.get(i)
                    if((animal!!.id).equals(same.id) ){
                        var position = i
                        list.set(position, animal)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var animal = p0.getValue(Animal::class.java)
                for(i in 0..totalChild){
                    var same = list.get(i)
                    if((animal!!.id).equals(same.id) ){
                        var position = i
                        list.removeAt(position)
                        adapter.notifyDataSetChanged()
                        totalChild--
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var animal = p0.getValue(Animal::class.java)
                list.add(animal!!)
                adapter.notifyDataSetChanged()

                totalChild++

            }
        })
    }

    fun findPet(adapter:UserMarketAdapter,list: ArrayList<Animal>, petName: String, error: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().petTable).orderByChild("name").startAt(petName).endAt(petName)
            .addChildEventListener(object: ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toasty.error(context, error).show()
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        if (p0.childrenCount > 0){
                            list.clear()
                            var animal = p0.getValue(Animal::class.java)
                            list.add(animal!!)
                            adapter.notifyDataSetChanged()
                        }else{
                            Toasty.error(context, error).show()
                        }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

}