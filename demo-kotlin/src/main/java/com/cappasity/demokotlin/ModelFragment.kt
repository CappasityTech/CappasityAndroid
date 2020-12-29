//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2020. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demokotlin

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.cappasity.framework.Cappasity

import com.cappasity.framework.CappasityModel
import com.cappasity.framework.CappasityModelView
import com.cappasity.framework.CappasityModelViewParams
import com.cappasity.framework.CappasityException
import com.google.android.material.snackbar.Snackbar

const val ARG_MODEL = "model"

class ModelFragment : Fragment() {
    private var model: CappasityModel? = null
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            model = it.getParcelable(ARG_MODEL)
        }
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (model != null) {
            root = inflater.inflate(R.layout.fragment_model, container, false)
            val modelView: CappasityModelView = root.findViewById(R.id.modelView)
            val progressBar: ProgressBar = root.findViewById(R.id.pb)
            val modelViewParams = CappasityModelViewParams(autoRun = true, closeButton = false)
            modelView.loadModel(
                model!!,
                modelViewParams
            )
            modelView.setOnModelLoadListener {
                modelView.visibility = VISIBLE
                progressBar.visibility = GONE
            }
            return root
        }
        root = inflater.inflate(R.layout.fragment_model_empty, container, false)
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnInteractionListener) {
            onInteractionListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onInteractionListener = null
    }

    private var onInteractionListener: OnInteractionListener? = null

    interface OnInteractionListener {
        fun openModel(cappasityModel: CappasityModel)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val searchView = menu.findItem(R.id.menu_item_search)?.actionView as SearchView?
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                query?.let {
                    if (query.contains('/')) {
                        processLinkQuery(query)
                    } else {
                        processSkuOrIdQuery(query)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun processLinkQuery(query: String) {
        Cappasity.modelService.getModelByLink(
            query,
            onSuccess = { model ->
                onInteractionListener?.openModel(model)
            },
            onFailure = { cappasityException ->
                ShowError(cappasityException)
            }
        )
    }

    private fun processSkuOrIdQuery(query: String)
    {
        Cappasity.modelService.getModel(
            query,
            onSuccess = { model ->
                onInteractionListener?.openModel(model)
            },
            onFailure = { cappasityException ->
                ShowError(cappasityException)
            }
        )
    }

    private fun ShowError(exception: CappasityException) {
        var message = exception.localizedMessage
        if (message.isEmpty()) {
            if (!exception.message.isNullOrEmpty()) {
                message = exception.message!!
            } else {
                message = "Error " + exception.code.toString()
            }
        }
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
    }
}