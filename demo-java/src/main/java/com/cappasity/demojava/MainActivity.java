//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2020. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demojava;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.cappasity.framework.CappasityModel;

public class MainActivity extends AppCompatActivity implements ModelFragment.OnInteractionListener {

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public void openModel(CappasityModel cappasityModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ModelFragment.ARG_MODEL, cappasityModel);
        navController.navigate(R.id.action_modelFragment_self, bundle);
    }
}