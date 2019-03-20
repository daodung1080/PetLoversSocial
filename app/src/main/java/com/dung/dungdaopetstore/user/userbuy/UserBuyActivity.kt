package com.dung.dungdaopetstore.user.userbuy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.UserMarketAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.PetDatabase
import com.dung.dungdaopetstore.model.Animal
import kotlinx.android.synthetic.main.activity_user_buy.*
import java.util.*

class UserBuyActivity : BaseActivity() {

    lateinit var petDatabase: PetDatabase
    lateinit var adapter: UserMarketAdapter
    lateinit var list: ArrayList<Animal>
    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_buy)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.img_back)

        var sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username","")

        getAllAnimals()

    }

    private fun getAllAnimals() {
        petDatabase = PetDatabase(this)
        list = ArrayList()
        adapter = UserMarketAdapter(this,list)

        rvUserBuy.layoutManager = GridLayoutManager(this,2)
        rvUserBuy.adapter = adapter

        petDatabase.getAllAnimals(adapter,list)

    }

    fun showPetInformation(position: Int){
        var animal = list.get(position)
        var intent = Intent(this@UserBuyActivity, UserBuyInformationActivity::class.java)
        intent.putExtra("petID",animal.id)
        startActivity(intent)
    }

}
