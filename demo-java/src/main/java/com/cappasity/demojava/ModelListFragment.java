//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2021. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demojava;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cappasity.common.exception.CappasityException;
import com.cappasity.framework.Cappasity;
import com.cappasity.framework.CappasityModel;
import com.cappasity.framework.CappasityModelListService;
import com.cappasity.framework.CappasityModelService;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ModelListFragment extends Fragment {

    interface OnInteractionListener {
        void openModel(CappasityModel cappasityModel);
    }

    private final ModelListAdapter rvAdapter = new ModelListAdapter(new ModelListAdapter.OnItemClickListener() {
        @Override
        public void onItemClicked(CappasityModel model) {
            onInteractionListener.openModel(model);
        }
    });

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_model_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setAdapter(rvAdapter);
        Cappasity.getModelListService().getModelList(0, 30, new CappasityModelListService.Callback() {

            @Override
            public void onFailure(@NotNull CappasityException e) {
                showError(e);
            }

            @Override
            public void onSuccess(@NotNull List<CappasityModel> list) {
                rvAdapter.updateItems(list);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
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
                        showError(exception);
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
                        showError(exception);
                    }
                }
        );
    }

    private void showError(CappasityException exception) {
        String message = exception.getLocalizedMessage();
        if (message.isEmpty()) {
            message = exception.getMessage();
        }
        if (message.isEmpty()) {
            message = "Error " + exception.getCode();
        }
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

}
