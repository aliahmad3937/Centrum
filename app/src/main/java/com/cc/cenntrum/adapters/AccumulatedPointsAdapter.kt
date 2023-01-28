package com.cc.cenntrum.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.databinding.ItemAccumulatedPointsBinding
import com.cc.cenntrum.models.PointsResponse
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Ali Ahmad on 9,September,2022
 * CodeCoy,
 * Lahore, Pakistan.
 */
class AccumulatedPointsAdapter(
    private var mList: List<PointsResponse.Data>,
    val context: Context

) : RecyclerView.Adapter<AccumulatedPointsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAccumulatedPointsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]


        holder.mBinding.tvId.text = "${position + 1}"

        holder.mBinding.tvDate.text = getDate(model.endTime!!, "dd/MM/yyyy")
        holder.mBinding.tvStartTime.text = getDate(model.startTime!!, "hh:mm:ss")
        holder.mBinding.tvEndTime.text = getDate(model.endTime!!, "hh:mm:ss")

        holder.mBinding.tvPoints.text = model.points.toString()


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

    class ViewHolder(val mBinding: ItemAccumulatedPointsBinding) :
        RecyclerView.ViewHolder(mBinding.root)

}

