package com.dung.dungdaopetstore.firebase

import android.content.Context
import com.dung.dungdaopetstore.adapter.user.UserOrderBuyAdapter
import com.dung.dungdaopetstore.adapter.user.UserOrderSellAdapter
import com.dung.dungdaopetstore.model.Animal
import com.dung.dungdaopetstore.model.Order
import com.google.firebase.database.*

class OrderDatabase(var context: Context) {

    fun getBuyOrderByUsername(list: ArrayList<Order>, adapter: UserOrderBuyAdapter, username: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().orderTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var order = it.getValue(Order::class.java)
                    if(order!!.username.equals(username)){
                        list.add(order)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    fun getSellOrderByUsername(list: ArrayList<Animal>, adapter: UserOrderSellAdapter, username: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().petTable).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    var animal = it.getValue(Animal::class.java)
                    if(animal!!.seller.equals(username)){
                        list.add(animal)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

}