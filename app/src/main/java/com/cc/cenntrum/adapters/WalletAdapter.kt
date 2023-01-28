package com.cc.cenntrum.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.ItemCtyptoWalletBinding
import com.cc.cenntrum.databinding.ItemIncentiveBinding
import com.cc.cenntrum.models.RewardResponse
import com.cc.cenntrum.models.WalletResponse
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Ali Ahmad on 9,September,2022
 * CodeCoy,
 * Lahore, Pakistan.
 */
class WalletAdapter(
    private var mList: List<WalletResponse>,
    val context: Context,
    ) : RecyclerView.Adapter<WalletViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        return WalletViewHolder(
            ItemCtyptoWalletBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val model = mList[position]
//        Picasso.get().load("https://cenntrum.codecoyapps.com/storage/app/${model?.incImg}")
//            .placeholder(R.drawable.logo)
//            .into(holder.mBinding.img)
//
//
//        holder.mBinding.tvId.text = model.incentId.toString()
//        holder.mBinding.tvName.text = model.incName.toString()
//        holder.mBinding.tvIncenAvail.text = getDate(model.dateTime!!, "dd/MM/yyyy")
//        holder.mBinding.tvIncenValue.text = getDate(model.dateTime!!, "hh:mm:ss")
//        holder.mBinding.tvRequiPoints.text = model.points.toString()


    }

    override fun getItemCount(): Int {
        return mList.size
    }




}

class WalletViewHolder(val mBinding: ItemCtyptoWalletBinding) : RecyclerView.ViewHolder(mBinding.root)