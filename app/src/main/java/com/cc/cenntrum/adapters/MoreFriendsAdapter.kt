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


class MoreFriendsAdapter(val context: Context,  lis: MutableList<FriendResponse.Data>, val isFriend:Boolean, val callback: FriendCallback ) :
    RecyclerView.Adapter<MoreFriendsAdapter.ViewHolder>() {
    var list: MutableList<FriendResponse.Data> = ArrayList()
    var templist: MutableList<FriendResponse.Data> = ArrayList()

    init {
        list.addAll(lis)
        templist.addAll(lis)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.friendName.text = list[position].firstname +" "+ list[position].lastname

        holder.itemView.setOnClickListener{
            callback.onFriendClick(position,list[position] , isFriend )
        }


    }
    override fun getItemCount(): Int {
        return if (list == null || list.isEmpty()) 0 else list.size
    }


    fun updateAdapter(list: MutableList<FriendResponse.Data>){
       // this.list.clear()
        Log.v("TAG8","size : ${list.size}")
        this.list = list
        notifyDataSetChanged()
    }

    fun getFilter(query:String?) {

        Log.v("TAG867TAG867","query : ${query}")
        if (query != null && query.isNotEmpty()) {
          var filterList = templist.filter {
              Log.v("TAG867TAG867","${it.firstname}")
              it.firstname.toString().toLowerCase().contains(query)
          }
            if(filterList != null && filterList.size > 0){
                list.clear()
                list.addAll(filterList)
                notifyDataSetChanged()
            }else{
                list.clear()
                notifyDataSetChanged()
            }

        }else{
            list.clear()
            list.addAll(templist)
            notifyDataSetChanged()

        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemFriendBinding

        init {
            binding = ItemFriendBinding.bind(itemView)
        }
    }


}