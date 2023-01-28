package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.adapters.IncentivesAdapter
import com.cc.cenntrum.callbacks.IncentiveClickListener
import com.cc.cenntrum.databinding.FragmentIncentivesBinding
import com.cc.cenntrum.models.*
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.*
import com.cc.cenntrum.utils.ToastUtils.showToast
import com.cc.cenntrum.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList


@AndroidEntryPoint
class Incentives : Fragment(), IncentiveClickListener {
    private lateinit var binding: FragmentIncentivesBinding
    private lateinit var mContext: MainActivity
    private val viewModel: MainViewModel by viewModels()
    private var mList: ArrayList<IncentiveResponse.Data>? = null
    private var mAdapter: IncentivesAdapter? = null

    private lateinit var item: IncentiveResponse.Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_incentives, container, false)

        binding.layoutPoints.text =
            "Available Points:${SavedPreference.getUserData(mContext)?.totalPoints}"
        setUpRecyclerView()

        Handler(Looper.getMainLooper()).postDelayed({
            LoadingUtils.pauseLoading()
        }
        ,20000)

//        if (GlobalData.incentiveResponse == null) {
//            viewModel.getIncentiveList(
//                IncentiveRequest(
//                    SavedPreference.getUserData(mContext)?.id,
//                    "24.4444",
//                    "76.552552"
//                )
//            )
//        } else {
//            mList?.clear()
//            mList!!.addAll(GlobalData.incentiveResponse!!)
//            mAdapter!!.notifyDataSetChanged()
//        }

        viewModel.getIncentiveList(
            IncentiveRequest(
                SavedPreference.getUserData(mContext)?.id,
                "24.4444",
                "76.552552"
            )
        )

        viewModel.incentiveResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    LoadingUtils.showLoading(mContext)
                }
                is APIResponse.Error -> {
                    LoadingUtils.pauseLoading()
                    showToast(requireContext(), "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    LoadingUtils.pauseLoading()
                    val response = it.data as IncentiveResponse

                    if (response.status == true) {
                        mList?.clear()
                        GlobalData.incentiveResponse = response.data
                        mList!!.addAll(response.data)
                        mAdapter!!.notifyDataSetChanged()
                    } else {
                        showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {
                    //       Log.v("TAG6","response starting")
                }
                else -> {
                    LoadingUtils.pauseLoading()
                }
            }
        })


        binding.btnNo.setOnClickListener {
            changeLayout(incentiveVisibility = View.VISIBLE, exchangeVisibility = View.GONE)
        }

        binding.btnYes.setOnClickListener {
            if(binding.btnYes.text.toString().lowercase() == "yes" ){
                binding.btnYes.text = "Send"
                binding.tvTitle.text = "You have EXCHANGE ${item.reqPoint}\nPoints For ${item.value} ${item.name}?"
                binding.btnNo.visibility = View.GONE
                binding.etWalletAddress.visibility = View.VISIBLE
                binding.etWalletAddress.setText(item.address.toString())
            }else if(binding.btnYes.text.toString().lowercase() == "send" ){

                // call api now
                viewModel.buyIncentive(BuyIncentiveRequest(
                    SavedPreference.getUserData(mContext)!!.id,
                    item.id.toString()
                ))
                binding.layoutPoints.text =
                    "Available Points:${point!! - item.reqPoint!!}"

                binding.animationView.visibility = View.VISIBLE
                binding.tvAvailPoints.text = "Available Points :${point!! - item.reqPoint!!}"
                binding.btnYes.text = "OK"
                binding.etWalletAddress.visibility = View.GONE
                binding.etWalletAddress.setText("")
                binding.tvCoinName.visibility = View.GONE
                binding.tvTitle.text = "In the Next 48 Hour you will\nReceive ${item.value} PETRO?"
            }else{
                binding.btnYes.text = "YES"

                binding.btnNo.visibility = View.VISIBLE
                binding.tvCoinName.visibility = View.VISIBLE
                binding.animationView.visibility = View.GONE

                changeLayout(incentiveVisibility = View.VISIBLE, exchangeVisibility = View.GONE)
            }
        }


        binding.drawer.setOnClickListener {
            mContext.toggleLeftDrawer(binding.drawerLayout, binding.leftDrawerMenu.root)
        }

        binding.back.setOnClickListener {
            mContext.toggleRightDrawer(binding.drawerLayout, binding.rightDrawerMenu.root)
        }



        // initialize left and right drawer menu
        mContext.leftDrawerClickListener(
            binding.leftDrawerMenu.root,
            binding.drawerLayout,
            binding.leftDrawerMenu.root
        )
        mContext.rightDrawerClickListener(
            binding.rightDrawerMenu.root,
            binding.drawerLayout,
            binding.rightDrawerMenu.root
        )





        return binding.root
    }


    private fun setUpRecyclerView() {
        mList = ArrayList()
        mAdapter =
            IncentivesAdapter(mList!!, requireContext(), this)
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvIncentives.layoutManager = linearLayoutManager
        binding.rvIncentives.adapter = mAdapter
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    fun changeLayout(incentiveVisibility: Int, exchangeVisibility: Int) {
        binding.groupIncentives.visibility = incentiveVisibility
        binding.groupExchange.visibility = exchangeVisibility

    }

    private  var point :Int? = null
    override fun onIncentiveClick(item: IncentiveResponse.Data) {
        this.item = item
        point = SavedPreference.getUserData(requireContext())?.totalPoints!!
        if(point!! >= item.reqPoint!!){
            changeLayout(incentiveVisibility = View.GONE, exchangeVisibility = View.VISIBLE)
            binding.tvCoinName.text =item.name
            binding.tvAvailPoints.text = "Available Points :$point"
            binding.tvTitle.text = "Are you sure you want to\nExchange ${item.reqPoint} Points\nFor ${item.value} ${item.name}?"
            binding.continueBtn.text = "Exchanged # ${item.id}"
            binding.ivIncenti.decodeBase64Image(item?.img.toString())
        }else{
         Toast.makeText(requireContext(),getString(R.string.not_enough_points),Toast.LENGTH_LONG).show()
        }
    }

}

fun ImageView.decodeBase64Image(encodedString :String ){
    val pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
    val decodedString: ByteArray = Base64.decode(pureBase64Encoded,Base64.DEFAULT)
    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    this.setImageBitmap(decodedByte)
}