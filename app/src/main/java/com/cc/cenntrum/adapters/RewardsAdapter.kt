package com.cc.cenntrum.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.ItemIncentiveBinding
import com.cc.cenntrum.databinding.ItemRewardBinding
import com.cc.cenntrum.models.RewardResponse
import com.cc.cenntrum.ui.fragments.decodeBase64Image
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Ali Ahmad on 9,September,2022
 * CodeCoy,
 * Lahore, Pakistan.
 */
class RewardsAdapter(
    private var mList: List<RewardResponse.Data>,
    val context: Context,

    ) : RecyclerView.Adapter<RewardsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsViewHolder {
        return RewardsViewHolder(
            ItemRewardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RewardsViewHolder, position: Int) {
        val model = mList[position]
//        Picasso.get().load("https://cenntrum.codecoyapps.com/storage/app/${model?.incImg}")
//            .placeholder(R.drawable.logo)
//            .into(holder.mBinding.img)

        holder.mBinding.img.decodeBase64Image(model?.incImg.toString())
        holder.mBinding.tvId.text = "${position + 1}"
        holder.mBinding.tvName.text = model.incName.toString()
        holder.mBinding.tvIncenAvail.text = getDate(model.dateTime!!, "dd/MM/yyyy")
        holder.mBinding.tvIncenValue.text = getDate(model.dateTime!!, "hh:mm:ss")
        holder.mBinding.tvRequiPoints.text = model.points.toString()

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }


}

class RewardsViewHolder(val mBinding: ItemRewardBinding) : RecyclerView.ViewHolder(mBinding.root)