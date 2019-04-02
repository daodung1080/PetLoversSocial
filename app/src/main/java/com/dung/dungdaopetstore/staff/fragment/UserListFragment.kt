package com.dung.dungdaopetstore.staff.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.staff.StaffUserAdapter
import com.dung.dungdaopetstore.base.BaseFragment
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.UserDatabase
import com.dung.dungdaopetstore.model.User
import com.dung.dungdaopetstore.staff.StaffChatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_staff_user_list.view.*

class UserListFragment: BaseFragment() {

    lateinit var rootview: View
    lateinit var rvUserList: RecyclerView
    lateinit var list: ArrayList<User>
    lateinit var adapter: StaffUserAdapter
    lateinit var userDatabase: UserDatabase
    lateinit var mData: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.fragment_staff_user_list, container,false)
        initView()
        return rootview
    }

    private fun initView() {
        mData = FirebaseDatabase.getInstance().reference
        userDatabase = UserDatabase(context!!)
        rvUserList = rootview.rvUserList
        list = ArrayList()
        adapter = StaffUserAdapter(context!!, list,this)
        rvUserList.layoutManager = LinearLayoutManager(context)
        rvUserList.setHasFixedSize(true)
        rvUserList.adapter = adapter

        userDatabase.getAllUser(list,adapter)

    }

    fun banMember(position: Int, isBanned: Boolean){
        var user = list.get(position)
        var alertDialog = AlertDialog.Builder(context)
        if(isBanned == false){
            alertDialog.setIcon(R.drawable.img_release)
            alertDialog.setTitle(resources.getString(R.string.titleUserUnBan))
            alertDialog.setMessage("${resources.getString(R.string.warningUnBanMember)} ${user.username}?\n${resources.getString(R.string.warningUnBanMember1)}")
            alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                mData.child(Constants().userTable).child(user.username).child("ban").setValue(false)
                    .addOnFailureListener { showMessage(resources.getString(R.string.checkInternet),false) }
            })
            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        }else{
            alertDialog.setIcon(R.drawable.img_ban)
            alertDialog.setTitle(resources.getString(R.string.titleUserBan))
            alertDialog.setMessage("${resources.getString(R.string.warningBanMember)} ${user.username}?\n${resources.getString(R.string.warningBanMember1)}")
            alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                mData.child(Constants().userTable).child(user.username).child("ban").setValue(true)
                    .addOnSuccessListener {
                        mData.child(Constants().userTable).child(user.username).child("bannedTime")
                            .addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    showMessage(resources.getString(R.string.checkInternet),false)
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    var banTime = p0.getValue(Int::class.java)
                                    var banPlus = banTime!! + 1
                                    mData.child(Constants().userTable).child(user.username).child("bannedTime").setValue(banPlus)
                                }

                            })
                    }
                    .addOnFailureListener { showMessage(resources.getString(R.string.checkInternet),false) }
            })
            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        }
        var dialog = alertDialog.create()
        dialog.show()
    }

    fun meetPeople(position: Int){
        var intent = Intent(context, StaffChatActivity::class.java)
        var user = list.get(position)
        intent.putExtra("people",user.username)
        startActivity(intent)
    }

}