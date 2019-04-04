package com.dung.dungdaopetstore.introduce

import android.content.Intent
import android.os.Bundle
import com.chyrta.onboarder.OnboarderActivity
import com.dung.dungdaopetstore.R
import com.chyrta.onboarder.OnboarderPage
import com.dung.dungdaopetstore.loginsignup.LoginActivity


class IntroduceActivity : OnboarderActivity() {

    override fun onSkipButtonPressed() {
        super.onSkipButtonPressed()
        startActivity(Intent(this@IntroduceActivity, LoginActivity::class.java))
        this@IntroduceActivity.finish()
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }

    override fun onFinishButtonPressed() {
        startActivity(Intent(this@IntroduceActivity, LoginActivity::class.java))
        this@IntroduceActivity.finish()
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }

    lateinit var onboarderPages: ArrayList<OnboarderPage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onboarderPages = ArrayList()

        // Add all introduce
        // Create your first page
        val onboarderPage1 = OnboarderPage("Step One", "You need to sign up an account to join the Application",R.drawable.img_create_account)
        val onboarderPage2 = OnboarderPage("Step Two", "You can interact with the Icon", R.drawable.img_interact)
        val onboarderPage3 = OnboarderPage("Step Three", "You can communicate with another Members",R.drawable.img_communicate)


        // Don't forget to set background color for your page
        onboarderPage1.setBackgroundColor(R.color.colorEndBtn)
        onboarderPage2.setBackgroundColor(R.color.colorStartBtn)
        onboarderPage3.setBackgroundColor(R.color.colorEndBtn2)

        // Add your pages to the list
        onboarderPages.add(onboarderPage1)
        onboarderPages.add(onboarderPage2)
        onboarderPages.add(onboarderPage3)

        onboarderPages.forEach {
        }

        setSkipButtonTitle("Skip");
        setFinishButtonTitle("Finish");

        // And pass your pages to 'setOnboardPagesReady' method
        setOnboardPagesReady(onboarderPages)
    }
}
