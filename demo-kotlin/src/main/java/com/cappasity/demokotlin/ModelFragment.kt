//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2020. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demokotlin

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cappasity.framework.CappasityModel
import com.cappasity.framework.CappasityModelViewParams
import kotlinx.android.synthetic.main.fragment_model.*


class ModelFragment : Fragment(R.layout.fragment_model) {

    companion object {
        const val ARG_MODEL = "model"
    }

    private val model: CappasityModel
        get() = arguments?.getParcelable(ARG_MODEL)!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        val modelViewParams = CappasityModelViewParams(
            autoRun = true
        )
        modelView.loadModel(
            model,
            modelViewParams
        )

        modelView.setOnModelLoadListener {
            modelView.visibility = VISIBLE
            pb.visibility = GONE
        }
    }

}
