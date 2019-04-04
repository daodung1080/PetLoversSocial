package com.dung.dungdaopetstore.user.userbuy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.adapter.user.UserMarketAdapter
import com.dung.dungdaopetstore.base.BaseActivity
import com.dung.dungdaopetstore.firebase.Constants
import com.dung.dungdaopetstore.firebase.PetDatabase
import com.dung.dungdaopetstore.model.Animal
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_market_buy.*
import kotlinx.android.synthetic.main.content_user_market_buy.*
import kotlin.collections.ArrayList

class UserBuyActivity : BaseActivity() {

    lateinit var petDatabase: PetDatabase
    lateinit var adapter: UserMarketAdapter
    lateinit var list: ArrayList<Animal>
    lateinit var username: String
    lateinit var petAdapter: ArrayAdapter<String>
    lateinit var petList: List<String>
    lateinit var mData : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_buy)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.img_back)
        activityAnim(this)

        var sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username","")

        initView()
        getAllAnimals()
        getPetByCategory()

    }

    // init all View and Class
    private fun initView() {
        petDatabase = PetDatabase(this)
        mData = FirebaseDatabase.getInstance().reference
        petList = listOf(resources.getString(R.string.all),resources.getString(R.string.dog),resources.getString(R.string.cat),resources.getString(R.string.fish),
            resources.getString(R.string.turtle),resources.getString(R.string.mouse),resources.getString(R.string.bird),
            resources.getString(R.string.another))
        petAdapter = ArrayAdapter(this,R.layout.spinner_custom_text_category,petList)
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCategoryPet.adapter = petAdapter
    }

    // get all pet when user clicked Category item
    private fun getPetByCategory() {
        spnCategoryPet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getPetListByCategory(petList.get(position))
            }

        }
    }

    // get all pet by Category
    private fun getPetListByCategory(petCategory: String) {
        if(petCategory.equals(resources.getString(R.string.all))){
            petDatabase.getAllAnimals(adapter,list)
        }else{
            mData.child(Constants().petTable).addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    showMessage(resources.getString(R.string.checkInternet),false)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    p0.children.forEach {
                        var animal = it.getValue(Animal::class.java)
                        if(animal!!.category.equals(petCategory) && animal.amount > 0){
                            list.add(animal)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

            })
        }
    }


    // get all pet from database then put into Recycler View
    private fun getAllAnimals() {
        list = ArrayList()
        adapter = UserMarketAdapter(this, list)

        // set adapter for recyclerView
        rvUserBuy.layoutManager = GridLayoutManager(this,2)
        rvUserBuy.setHasFixedSize(true)
        rvUserBuy.adapter = adapter

        petDatabase.getAllAnimals(adapter,list)

    }

    // switch activity when user click into Pet Icon
    fun showPetInformation(position: Int){
        var animal = list.get(position)
        var intent = Intent(this@UserBuyActivity, UserBuyInformationActivity::class.java)
        intent.putExtra("petID",animal.id)
        startActivity(intent)
    }

    // Back button animation
    override fun onBackPressed() {
        super.onBackPressed()
        activityAnim(this)
    }

}
