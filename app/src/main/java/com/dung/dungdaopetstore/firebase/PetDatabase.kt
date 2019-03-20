package com.dung.dungdaopetstore.firebase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dung.dungdaopetstore.adapter.UserMarketAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.model.Animal
import com.dung.dungdaopetstore.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PetDatabase(var context: Context){

//    fun remainAmount(petID: String, bAmount: Int, pAmount: Int, totalPetMoney: Int, username: String): Boolean{
//        var result = true
//        var mData = FirebaseDatabase.getInstance().reference
//        mData.child(Constants().userTable).orderByChild("username").equalTo(username)
//            .addChildEventListener(object: ChildEventListener{
//                override fun onCancelled(p0: DatabaseError) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//
//                }
//
//                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                    var user = p0.getValue(User::class.java)
//                    var money = user!!.money
//                    Toasty.success(context, "money: ${money} - username: $username - totalCost: $totalPetMoney").show()
//                    if(money < totalPetMoney){
//                        Toasty.error(context, "You dont have enough money to buy").show()
////                        resource.getString(R.string.errorNotEnoughMoney) --- String dont recreate
//                    }else {
//                        mData.child(Constants().userTable).child(username).child("money")
//                            .setValue(money - totalPetMoney)
//                        mData.child(Constants().petTable).child(petID).child("amount")
//                            .setValue(pAmount - bAmount)
//                    }
//                }
//
//                override fun onChildRemoved(p0: DataSnapshot) {
//
//                }
//            })
//        return result
//    }

    fun insertAnimal(aName: String, aGender: String, aPrice: Double,
                     aAmount: Int, img: ImageView, confirm: Boolean, seller: String
                     ,petWeight: Int, aCategory: String): Boolean{
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
                    Animal(id,aName, aGender, aPrice, aAmount, downloadURL,confirm,seller,petWeight,aCategory))
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

        mData.child(Constants().petTable).orderByChild("confirm").equalTo(true)
            .addChildEventListener(object : ChildEventListener{
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

    fun getInformationByName(petID: String, img: ImageView, name: TextView,
                             gender: TextView, seller: TextView, amount: TextView, price: TextView
                             , left: String,weight: TextView,category: TextView){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().petTable).child(petID)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var animal = p0.getValue(Animal::class.java)
                    Picasso.get().load(animal!!.image).into(img)
                    name.setText(animal.name)
                    gender.setText(animal.gender)
                    seller.setText(animal.seller)
                    amount.setText("${animal.amount} ${left}")
                    var fm = DecimalFormat("###,###,###")
                    price.setText("${fm.format(animal.price)} VND")
                    weight.setText("${animal.weight} kg")
                    category.setText(animal.category)
                }

            })
        }

}
