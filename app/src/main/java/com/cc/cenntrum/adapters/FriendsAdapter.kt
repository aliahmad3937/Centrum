package com.cc.cenntrum.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.callbacks.FriendCallback
import com.cc.cenntrum.databinding.ItemFriendBinding
import com.cc.cenntrum.databinding.ItemNotificationBinding
import com.cc.cenntrum.models.FriendResponse
import com.cc.cenntrum.models.NotificationItems
import com.squareup.picasso.Picasso


class FriendsAdapter(
    val context: Context,
    var list: MutableList<FriendResponse.Data>,
    val isFriend: Boolean,
    val callback: FriendCallback
) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (isFriend) {
            if (position == 7) {
                //  holder.binding.friendImage.setImageDrawable(context.getDrawable(R.drawable.menu))
                //  holder.binding.friendName.setTextColor(context.getColor(R.color.btn_color))
                //    holder.binding.friendName.text = "See More"
                //  holder.binding.friendName.setTextSize(18F)

                holder.binding.layoutSeemore.visibility = View.VISIBLE
                holder.binding.layoutUser.visibility = View.GONE

            } else {

                when (list[position].type) {
                    0 -> {
                        holder.binding.type.visibility = View.GONE
                    }
                    1 -> {
                        holder.binding.type.text = "Driving"
                        holder.binding.type.visibility = View.VISIBLE
                    }
                    2 -> {
                        holder.binding.type.text = "Studying"
                        holder.binding.type.visibility = View.VISIBLE
                    }
                    3 -> {
                        holder.binding.type.text = "Working"
                        holder.binding.type.visibility = View.VISIBLE
                    }
                    4 -> {
                        holder.binding.type.text = "Family"
                        holder.binding.type.visibility = View.VISIBLE
                    }
                    5 -> {
                        holder.binding.type.text = "Exercising"
                        holder.binding.type.visibility = View.VISIBLE
                    }
                    6 -> {
                        holder.binding.type.text = "Other"
                        holder.binding.type.visibility = View.VISIBLE
                    }
                }
                holder.binding.friendName.text =
                    list[position].firstname + " " + list[position].lastname
                holder.binding.friendImage.setImageDrawable(context.getDrawable(R.drawable.no_image))

                holder.binding.layoutSeemore.visibility = View.GONE
                holder.binding.layoutUser.visibility = View.VISIBLE
            }
        } else {
            holder.binding.type.visibility = View.GONE
            if (position == 11) {
                //  holder.binding.friendImage.setImageDrawable(context.getDrawable(R.drawable.menu))
                //  holder.binding.friendName.setTextColor(context.getColor(R.color.btn_color))
                //   holder.binding.friendName.text = "See More"
                //     holder.binding.friendName.setTextSize(18F)

                holder.binding.layoutSeemore.visibility = View.VISIBLE
                holder.binding.layoutUser.visibility = View.GONE
            } else {
                holder.binding.friendName.text =
                    list[position].firstname + " " + list[position].lastname
                holder.binding.friendImage.setImageDrawable(context.getDrawable(R.drawable.no_image))

                holder.binding.layoutSeemore.visibility = View.GONE
                holder.binding.layoutUser.visibility = View.VISIBLE
            }
        }


        holder.itemView.setOnClickListener {
            callback.onFriendClick(position, list[position], isFriend)
        }


    }

    override fun getItemCount(): Int {
        return if (list == null || list.isEmpty()) 0 else list.size
    }


    fun updateAdapter(list: MutableList<FriendResponse.Data>) {
        // this.list.clear()
        Log.v("TAG8", "size : ${list.size}")
        this.list = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemFriendBinding

        init {
            binding = ItemFriendBinding.bind(itemView)
        }
    }


}