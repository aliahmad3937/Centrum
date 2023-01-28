package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.adapters.AccumulatedPointsAdapter
import com.cc.cenntrum.databinding.FragmentAccumulatedPointsBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.IncentiveRequest
import com.cc.cenntrum.models.PointsResponse
import com.cc.cenntrum.models.RewardResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.LoadingUtils
import com.cc.cenntrum.utils.MyDatePickerDialogFragment
import com.cc.cenntrum.utils.SavedPreference
import com.cc.cenntrum.utils.ToastUtils
import com.cc.cenntrum.viewmodels.MainViewModel
import com.ycuwq.datepicker.date.DatePickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.util.ToastUtil
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class AccumulatedPoints : Fragment() {
    private lateinit var binding: FragmentAccumulatedPointsBinding
    private lateinit var mContext: MainActivity
    private val viewModel: MainViewModel by viewModels()
    private var mList: ArrayList<PointsResponse.Data>? = null
    private var tempList: ArrayList<PointsResponse.Data>? = null
    private var mAdapter: AccumulatedPointsAdapter? = null
    private var totalPoints:Int = 0


    private var from: Long? = null
    private var to: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_accumulated_points,
            container,
            false
        )
        setUpRecyclerView()

        binding.layoutPoints.text =
            "Available Points:"
        viewModel.getAccumulatedPointsList(
            IncentiveRequest(
                userId = SavedPreference.getUserData(
                    mContext
                )?.id
            )
        )


        viewModel.accumulatedPointsResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    LoadingUtils.pauseLoading()
                    ToastUtils.showToast(requireContext(), "${it.message}")
                    Log.v("TAG6", "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    LoadingUtils.pauseLoading()
                    val response = it.data as PointsResponse
                    binding.layoutPoints.text =
                        "Available Points:${response.totalPoints}"

                    if (response.status == true) {
                        mList?.clear()
                        tempList?.clear()
                        mList!!.addAll(response.data)
                        tempList!!.addAll(response.data)
                        mAdapter!!.notifyDataSetChanged()
                    } else {
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {
                    //       Log.v("TAG6","response starting")
                }
            }
        })


        binding.etStartdate.setOnClickListener(View.OnClickListener { v: View? ->
            val datePickerDialogFragment = MyDatePickerDialogFragment()

            datePickerDialogFragment.run {
                setOnDateChooseListener { year, month, day ->
                    var day1 = day.toString()
                    var month1 = month.toString()
                    day1 = if (day1.length == 1) "0$day1" else day1
                    month1 = if (month1.length == 1) "0$month1" else month1
                    binding.etStartdate.setText("$day1-$month1-$year")
                    from = getMilliFromDate("$day1/$month1/$year")
                    mList?.clear()
                    mList?.addAll(tempList!!)
                    if (to != null) {
                        var list = mList!!.filter {
                           if (it.startTime!! >= from!! && it.startTime!! <= to!!){
                               totalPoints += it.points!!
                               true
                           }else{
                               false
                           }
                        }

                        if(list != null && list.isNotEmpty()){
                            mList?.clear()
                            mList!!.addAll(list)
                            mAdapter!!.notifyDataSetChanged()
                            binding.layoutPoints.text =
                                "Available Points:"+totalPoints

                            binding.etEnddate.setText("")
                            binding.etStartdate.setText("")
                            totalPoints = 0
                        }else{
                            ToastUtil.showToast("No Record b/w selected date range!")
                        }

                        from = null
                        to = null
                    } else {
                        //  ToastUtils.showToast(requireContext(), "end null")
                    }

                }

                show(requireActivity().fragmentManager, "DatePickerDialogFragment")
            }
        })

        binding.etEnddate.setOnClickListener(View.OnClickListener { v: View? ->
            val datePickerDialogFragment = MyDatePickerDialogFragment()
            datePickerDialogFragment.run {
                setOnDateChooseListener { year, month, day ->
                    var day1 = day.toString()
                    var month1 = month.toString()
                    day1 = if (day1.length == 1) "0$day1" else day1
                    month1 = if (month1.length == 1) "0$month1" else month1
                    binding.etEnddate.setText("$day1-$month1-$year")

                    to = getMilliFromDate("$day1/$month1/$year")
                    mList?.clear()
                    mList?.addAll(tempList!!)

                    if (from != null) {
                        var list = mList!!.filter {
                            if (it.startTime!! >= from!! && it.startTime!! <= to!!){
                                totalPoints += it.points!!
                                true
                            }else{
                                false
                            }
                        }
                        if(list != null && list.isNotEmpty()){
                            mList?.clear()
                            mList!!.addAll(list)
                            binding.layoutPoints.text =
                                "Total Points:"+totalPoints
                            mAdapter!!.notifyDataSetChanged()
                            binding.etEnddate.setText("")
                            binding.etStartdate.setText("")
                            totalPoints = 0

                        }else{
                            ToastUtil.showToast("No Record b/w selected date range!")
                        }

                        from = null
                        to = null
                    } else {
                        //  ToastUtils.showToast(requireContext(), "start null")
                    }


                }

                show(requireActivity().fragmentManager, "DatePickerDialogFragment")
            }
        })





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
        tempList = ArrayList()
        mAdapter = AccumulatedPointsAdapter(mList!!, requireContext())
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvIncentives.layoutManager = linearLayoutManager
        binding.rvIncentives.adapter = mAdapter
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    fun getTimeInMillis(day: Int, month: Int, year: Int): Long {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.getTimeInMillis()
    }


}