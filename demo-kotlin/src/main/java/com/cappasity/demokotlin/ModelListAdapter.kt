//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2021. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demokotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cappasity.framework.CappasityModel
import kotlinx.android.synthetic.main.item_model.view.*

class ModelListAdapter(
    private val onItemClicked: (CappasityModel) -> Unit
) : RecyclerView.Adapter<ModelListAdapter.ModelListViewHolder>() {

    private val items = mutableListOf<CappasityModel>()

    fun updateItems(items: List<CappasityModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ModelListViewHolder, position: Int) =
        holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelListViewHolder {
        return ModelListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_model, parent, false)
        )
    }

    inner class ModelListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: CappasityModel) {
            itemView.apply {
                setOnClickListener { onItemClicked.invoke(model) }
                tvName.text = model.title
                Glide.with(imageView).load(model.getPreviewUrl()).fitCenter().into(imageView)
            }
        }

    }

}