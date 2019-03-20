package com.dung.dungdaopetstore.firebase

import android.content.Context
import com.dung.dungdaopetstore.adapter.UserOrderBuyAdapter
import com.dung.dungdaopetstore.adapter.UserOrderSellAdapter
import com.dung.dungdaopetstore.model.Animal
import com.dung.dungdaopetstore.model.Order
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class OrderDatabase(var context: Context) {

    fun getBuyOrderByUsername(list: ArrayList<Order>, adapter: UserOrderBuyAdapter, username: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().orderTable).orderByChild("username")
            .equalTo(username).addChildEventListener(object: ChildEventListener{
                var totalCount = -1
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    var order = p0.getValue(Order::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((order!!.idOrder).equals(same.idOrder) ){
                            var position = i
                            list.set(position, order)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var order = p0.getValue(Order::class.java)
                    list.add(order!!)
                    adapter.notifyDataSetChanged()
                    totalCount++
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    var order = p0.getValue(Order::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((order!!.idOrder).equals(same.idOrder) ){
                            var position = i
                            list.removeAt(position)
                            adapter.notifyDataSetChanged()
                            totalCount--
                        }
                    }
                }

            })
    }

    fun getSellOrderByUsername(list: ArrayList<Animal>, adapter: UserOrderSellAdapter, username: String){
        var mData = FirebaseDatabase.getInstance().reference
        mData.child(Constants().petTable).orderByChild("seller")
            .equalTo(username).addChildEventListener(object: ChildEventListener{
                var totalCount = -1
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    var order = p0.getValue(Animal::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((order!!.id).equals(same.id) ){
                            var position = i
                            list.set(position, order)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    var order = p0.getValue(Animal::class.java)
                    list.add(order!!)
                    adapter.notifyDataSetChanged()
                    totalCount++
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    var order = p0.getValue(Animal::class.java)
                    for(i in 0..totalCount){
                        var same = list.get(i)
                        if((order!!.id).equals(same.id) ){
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