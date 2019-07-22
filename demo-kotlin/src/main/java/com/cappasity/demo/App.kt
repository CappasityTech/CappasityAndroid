//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2019. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demo

import android.app.Application
import com.cappasity.framework.Cappasity

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Cappasity.set(this, "CHANGE_IT") // set your user alias here
    }
}