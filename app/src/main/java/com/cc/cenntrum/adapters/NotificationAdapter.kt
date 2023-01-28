package com.cc.cenntrum.adapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.callbacks.FriendCallback
import com.cc.cenntrum.callbacks.NotificationCallback
import com.cc.cenntrum.databinding.ItemNotificationBinding
import com.cc.cenntrum.models.FriendResponse
import com.cc.cenntrum.models.NotificationItems


class NotificationAdapter(var context: Context, var list: MutableList<FriendResponse.Data>  , val callback: NotificationCallback) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(list.get(position)) {
            holder.binding.textView8.text = "${this.firstname} ${this.lastname}"

            holder.binding.button.setOnClickListener{
                // decline
                callback.onNotificationClick(position,this,false)
            }
            holder.binding.button2.setOnClickListener{
                // accept
                callback.onNotificationClick(position ,this,true)
            }

        }


    }

    override fun getItemCount(): Int {
        return if (list == null || list!!.isEmpty()) 0 else list!!.size
    }

    fun updateAdapter(list: MutableList<FriendResponse.Data>) {

        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemNotificationBinding

        init {
            binding = ItemNotificationBinding.bind(itemView)
        }
    }


}