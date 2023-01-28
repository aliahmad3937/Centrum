package com.cc.cenntrum.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cc.cenntrum.R
import com.cc.cenntrum.models.IncentiveResponse
import com.cc.cenntrum.ui.fragments.decodeBase64Image
import com.squareup.picasso.Picasso

class SpinnerAdapter(val context: Context, var dataSource: List<IncentiveResponse.Data>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_spinner_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = dataSource.get(position).name

        //    val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
        // vh.img.setBackgroundResource(id)
//        Picasso.get()
//            .load("https://cenntrum.codecoyapps.com/storage/app/${dataSource.get(position).img}")
//            .placeholder(R.drawable.logo)
//            .into(vh.img)
       vh.img.decodeBase64Image(dataSource.get(position).img.toString())




        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        val img: ImageView

        init {
            label = row?.findViewById(R.id.text) as TextView
            img = row?.findViewById(R.id.img) as ImageView
        }
    }

}