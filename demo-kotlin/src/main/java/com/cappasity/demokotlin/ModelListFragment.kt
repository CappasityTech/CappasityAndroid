//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2021. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demokotlin

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.cappasity.common.exception.CappasityException
import com.cappasity.framework.Cappasity
import com.cappasity.framework.CappasityModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_model_list.*

class ModelListFragment : Fragment(R.layout.fragment_model_list) {

    private val rvAdapter by lazy { ModelListAdapter(::onModelSelected) }

    private var onInteractionListener: OnInteractionListener? = null

    interface OnInteractionListener {
        fun openModel(cappasityModel: CappasityModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInteractionListener) {
            onInteractionListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onInteractionListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        recyclerView.adapter = rvAdapter
        Cappasity.modelListService.getModelList(
            offset = 0,
            limit = 30,
            onSuccess = rvAdapter::updateItems,
            onFailure = ::print
        )
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val searchView = menu.findItem(R.id.menu_item_search)?.actionView as SearchView?
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                query?.let {
                    if (query.contains('/')) processLinkQuery(query)
                    else processSkuOrIdQuery(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    private fun processLinkQuery(query: String) {
        Cappasity.modelService.getModelByLink(
            query,
            onSuccess = ::onModelSelected,
            onFailure = ::showError
        )
    }

    private fun processSkuOrIdQuery(query: String) {
        Cappasity.modelService.getModel(
            query,
            onSuccess = ::onModelSelected,
            onFailure = ::showError
        )
    }

    private fun onModelSelected(model: CappasityModel) {
        onInteractionListener?.openModel(model)
    }

    private fun showError(exception: CappasityException) {
        var message = exception.localizedMessage
        if (message.isEmpty()) {
            message = if (!exception.message.isNullOrEmpty()) {
                exception.message!!
            } else {
                "Error " + exception.code.toString()
            }
        }
        view?.rootView?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }

}