package com.cc.cenntrum.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.callbacks.IncentiveClickListener
import com.cc.cenntrum.databinding.ItemIncentiveBinding
import com.cc.cenntrum.models.IncentiveResponse
import com.cc.cenntrum.ui.fragments.decodeBase64Image
import com.squareup.picasso.Picasso


/**
 * Created by Ali Ahmad on 9,September,2022
 * CodeCoy,
 * Lahore, Pakistan.
 */
class IncentivesAdapter(
    private var mList: List<IncentiveResponse.Data>,
    val context: Context,
    val incentiveClickListener: IncentiveClickListener

    ) : RecyclerView.Adapter<IncentivesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncentivesViewHolder {
        return IncentivesViewHolder(
            ItemIncentiveBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: IncentivesViewHolder, position: Int) {
        val model = mList[position]
//        Picasso.get().load("https://cenntrum.codecoyapps.com/storage/app/${model?.img}")
//            .placeholder(R.drawable.logo)
//            .into(holder.mBinding.img)

       holder.mBinding.img.decodeBase64Image(model?.img.toString())

        holder.mBinding.tvId.text = "${position + 1}"
        holder.mBinding.tvName.text = model.name.toString()
        holder.mBinding.tvIncenAvail.text = model.quantity.toString()
        holder.mBinding.tvIncenValue.text = model.value.toString()
        holder.mBinding.tvRequiPoints.text = model.reqPoint.toString()


        holder.itemView.setOnClickListener {
            incentiveClickListener.onIncentiveClick(model)
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }


}

class IncentivesViewHolder(val mBinding: ItemIncentiveBinding) : RecyclerView.ViewHolder(mBinding.root)