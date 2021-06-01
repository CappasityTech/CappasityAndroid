//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2020. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demokotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.cappasity.demokotlin.ModelFragment.Companion.ARG_MODEL
import com.cappasity.framework.CappasityModel

class MainActivity : AppCompatActivity(), ModelListFragment.OnInteractionListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun openModel(cappasityModel: CappasityModel) {
        val bundle = Bundle()
        bundle.putParcelable(ARG_MODEL, cappasityModel)
        navController.navigate(R.id.action_modelFragment, bundle)
    }
}