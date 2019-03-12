package com.dung.dungdaopetstore.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dung.dungdaopetstore.R
import com.dung.dungdaopetstore.base.BaseFragment

class UserProfileFragment: BaseFragment() {

    lateinit var rootview: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootview = inflater.inflate(R.layout.fragment_user_profile, container, false)
        return rootview

    }

}