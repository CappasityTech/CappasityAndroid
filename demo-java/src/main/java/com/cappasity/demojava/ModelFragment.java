//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2020. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demojava;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.cappasity.framework.*;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;

public class ModelFragment extends Fragment {

    static String ARG_MODEL = "model";

    interface OnInteractionListener {
        void openModel(CappasityModel cappasityModel);
    }

    private OnInteractionListener onInteractionListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnInteractionListener) {
            onInteractionListener = (OnInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onInteractionListener = null;
    }

    private CappasityModel model;
    private View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            model = arguments.getParcelable(ARG_MODEL);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (model != null) {
            root = inflater.inflate(R.layout.fragment_model, container, false);
            final CappasityModelView modelView = root.findViewById(R.id.modelView);
            final ProgressBar progressBar = root.findViewById(R.id.pb);
            final CappasityModelViewParams params = new CappasityModelViewParams.Builder()
                    .autoRun(true)
                    .closeButton(false)
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final SearchView searchView =  (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (query.contains("/")) {
                    processLinkQuery(query);
                } else {
                    processSkuOrIdQuery(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void processLinkQuery(@NonNull String query) {
        Cappasity.getModelService().getModelByLink(
                query,
                new CappasityModelService.Callback() {
                    @Override
                    public void onSuccess(@NotNull CappasityModel model) {
                        onInteractionListener.openModel(model);
                    }

                    @Override
                    public void onFailure(@NotNull CappasityException exception) {
                        ShowError(exception);
                    }
                }
        );
    }

    private void processSkuOrIdQuery(@NonNull String query) {
        Cappasity.getModelService().getModel(
                query,
                new CappasityModelService.Callback() {
                    @Override
                    public void onSuccess(@NotNull CappasityModel model) {
                        onInteractionListener.openModel(model);
                    }

                    @Override
                    public void onFailure(@NotNull CappasityException exception) {
                        ShowError(exception);
                    }
                }
        );
    }

    private void ShowError(CappasityException exception) {
        String message = exception.getLocalizedMessage();
        if (message.isEmpty()) {
            message = exception.getMessage();
        }
        if (message.isEmpty()) {
            message = "Error " + Integer.toString(exception.getCode());
        }
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
    }
}