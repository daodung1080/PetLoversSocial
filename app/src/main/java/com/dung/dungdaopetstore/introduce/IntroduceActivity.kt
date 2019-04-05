package com.dung.dungdaopetstore.introduce

import android.content.Intent
import android.os.Bundle
import com.chyrta.onboarder.OnboarderActivity
import com.chyrta.onboarder.OnboarderPage
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.loginsignup.LoginActivity

class IntroduceActivity : OnboarderActivity() {

    // Skip button in introduce screen
    override fun onSkipButtonPressed() {
        super.onSkipButtonPressed()
        startActivity(Intent(this@IntroduceActivity, LoginActivity::class.java))
        this@IntroduceActivity.finish()
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }

    // Finish button introduce screen
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
        // Create pages
        val onboarderPage1 = OnboarderPage(resources.getString(R.string.title_introduce_1)
            , resources.getString(R.string.message_introduce_1),R.drawable.img_create_account)
        val onboarderPage2 = OnboarderPage(resources.getString(R.string.title_introduce_2)
            , resources.getString(R.string.message_introduce_2), R.drawable.img_interact)
        val onboarderPage3 = OnboarderPage(resources.getString(R.string.title_introduce_3)
            , resources.getString(R.string.message_introduce_3),R.drawable.img_communicate)


        // Set background for every page
        onboarderPage1.setBackgroundColor(R.color.colorIntroduce1)
        onboarderPage2.setBackgroundColor(R.color.colorIntroduce2)
        onboarderPage3.setBackgroundColor(R.color.colorIntroduce3)

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
