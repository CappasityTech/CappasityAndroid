//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2020. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demojava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cappasity.framework.CappasityModel;
import com.cappasity.framework.CappasityModelView;
import com.cappasity.framework.CappasityModelViewParams;

public class ModelFragment extends Fragment {

    static String ARG_MODEL = "model";

    private CappasityModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            model = arguments.getParcelable(ARG_MODEL);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root;
        if (model != null) {
            root = inflater.inflate(R.layout.fragment_model, container, false);
            final CappasityModelView modelView = root.findViewById(R.id.modelView);
            final ProgressBar progressBar = root.findViewById(R.id.pb);
            final CappasityModelViewParams params = new CappasityModelViewParams.Builder()
                    .autoRun(true)
                    .build();
            modelView.loadModel(model, params);
            modelView.setOnModelLoadListener(new CappasityModelView.OnModelLoadListener() {
                @Override
                public void onModelLoad() {
                    modelView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
            return root;

        }
        root = inflater.inflate(R.layout.fragment_model_empty, container, false);
        return root;
    }

}