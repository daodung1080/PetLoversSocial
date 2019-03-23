package com.dung.dungdaopetstore.user.userupdate

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.UserDatabase
import com.dung.dungdaopetstore.model.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_update.*
import kotlinx.android.synthetic.main.dialog_user_update.view.*

class UserUpdateActivity : BaseActivity() {

    lateinit var rUsername: String
    lateinit var mData: DatabaseReference
    lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_update)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        initView()
        userDatabase.getUserInformation1(txtUpdateFullname,txtUpdateAddress,txtUpdatePhone,txtUpdatePassword,rUsername)
        onclick()

    }

    private fun onclick() {
        cvUpdateFullname.setOnClickListener {
            clickToUpdate(
                resources.getString(R.string.txtUserUpdateFullname),txtUpdateFullname.text.toString(),
                rUsername, "fullname", 1)
        }
        cvUpdateAddress.setOnClickListener {
            clickToUpdate(
                resources.getString(R.string.txtUserUpdateAddress),txtUpdateAddress.text.toString(),
                rUsername, "address", 1)
        }
        cvUpdatePhone.setOnClickListener {
            clickToUpdate(
                resources.getString(R.string.txtUserUpdatePhoneNumber),txtUpdatePhone.text.toString(),
                rUsername, "phoneNumber", 3)
        }
        cvUpdatePassword.setOnClickListener {
            clickToUpdate(
                resources.getString(R.string.txtUserUpdatePassword),txtUpdatePassword.text.toString(),
                rUsername, "password", 2)
        }
    }

    fun initView(){
        userDatabase = UserDatabase(this)
        rUsername = getRootUsername()
        mData = FirebaseDatabase.getInstance().reference
    }

    fun clickToUpdate(title: String, content: String,parent: String, child: String,switch: Int){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.img_update)
        alertDialog.setTitle(title)
        var view = layoutInflater.inflate(R.layout.dialog_user_update, null)
        var edtUserUpdate = view.edtUserUpdate
        alertDialog.setView(view)
        edtUserUpdate.setText(content)
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var c = edtUserUpdate.text.toString()
            if(switch == 3){
                if(c.length != 10){
                    showMessage(resources.getString(R.string.errorSignUpPhone), false)
                }else{
                    mData.child(Constants().userTable).child(parent).child(child).setValue(c)
                        .addOnFailureListener {
                            showMessage(resources.getString(R.string.updateFailed),false)
                        }.addOnSuccessListener {
                            showMessage(resources.getString(R.string.updateComplete),true)
                        }
                }
            }else{
            if(c.length < 5 || c.length > 30){
                showMessage(resources.getString(R.string.errorEmpty), false)
            }else{
                if(switch == 1){
                    mData.child(Constants().userTable).child(parent).child(child).setValue(c)
                        .addOnFailureListener {
                            showMessage(resources.getString(R.string.updateFailed),false)
                        }.addOnSuccessListener {
                            showMessage(resources.getString(R.string.updateComplete),true)
                        }
                }else if(switch == 2){
                    mData.child(Constants().userTable).child(parent).child(child).setValue(c)
                        .addOnFailureListener {
                        showMessage(resources.getString(R.string.updateFailed),false)
                    }.addOnSuccessListener {
                        showMessage(resources.getString(R.string.updateComplete),true)
                    }
                    mData.child(Constants().userTable).child(parent).child("username_password")
                        .setValue("$rUsername-$c")
                    }
                }
            }
        })
        alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss()})
        var dialog = alertDialog.create()
        dialog.show()
    }

}
