package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentMyFocusBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.FilterRequest
import com.cc.cenntrum.models.IncentiveRequest
import com.cc.cenntrum.models.MyFocusResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.*
import com.cc.cenntrum.viewmodels.MainViewModel
import com.ycuwq.datepicker.date.DatePickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.util.ToastUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MyFocus : Fragment() {
    private lateinit var binding: FragmentMyFocusBinding
    private lateinit var mContext: MainActivity
    private val viewModel: MainViewModel by viewModels()
   private val args:MyFocusArgs by navArgs()


    private var from: String? = null
    private var to: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_focus, container, false)

         if(args.id != 0){
             viewModel.getMyFocusList(
                 IncentiveRequest(userId = args.id)
             )
             binding.continueBtn.text = args.name +" Focus"

         }else {
             binding.continueBtn.text = "My Focus"
             if (GlobalData.myFocusResponse == null) {
                 viewModel.getMyFocusList(
                     IncentiveRequest(userId = SavedPreference.getUserData(mContext)?.id!!)
                 )
               //  ToastUtil.showToast("${SavedPreference.getUserData(mContext)?.id!!}")
             } else {
                 updateView(GlobalData.myFocusResponse!!)
             }
         }





        binding.etStartdate.setOnClickListener(View.OnClickListener { v: View? ->
            val datePickerDialogFragment = MyDatePickerDialogFragment()

            datePickerDialogFragment.run {
                setOnDateChooseListener { year, month, day ->
                    var day1 = day.toString()
                    var month1 = month.toString()
                    day1 = if (day1.length == 1) "0$day1" else day1
                    month1 = if (month1.length == 1) "0$month1" else month1
                    binding.etStartdate.setText("$day1-$month1-$year")
                   from = getMilliFromDate("$day1/$month1/$year").toString()
                    Log.v("TAG55","time :$from")
                    Log.v("TAG55","date :${getNormalDate(from!!.toLong())}")


                    if (to != null) {
                        //  ToastUtils.showToast(requireContext(), "end not null")
                        viewModel.getFilteredFocus(
                            FilterRequest(
                                SavedPreference.getUserData(mContext)?.id!!,
                                from, to
                            )
                        )
                        from = null
                        to = null
                    }else{
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
                    from = getMilliFromDate("$day1/$month1/$year").toString()

                    if (from != null) {
                        //   ToastUtils.showToast(requireContext(), "start not null")
                        viewModel.getFilteredFocus(
                            FilterRequest(
                                SavedPreference.getUserData(mContext)?.id!!,
                                from, to
                            )
                        )
                        from = null
                        to = null
                    }else{
                        //  ToastUtils.showToast(requireContext(), "start null")
                    }


                }

                show(requireActivity().fragmentManager, "DatePickerDialogFragment")
            }
        })



        viewModel.myFocusResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    LoadingUtils.pauseLoading()
                    ToastUtils.showToast(requireContext(), "error :${it.message}")
                    Log.v("TAG6", "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    LoadingUtils.pauseLoading()
                    val response = it.data as MyFocusResponse

                    if (response.status == true) {
                        if(args.id == 0) {
                            GlobalData.myFocusResponse = response
                        }
                        updateView(response)
                        Log.v("TAG85","message: ${response.message}")
                        Log.v("TAG85",";ist: ${response.data!!.driving.toString()}")
                    } else {
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {
                    //       Log.v("TAG6","response starting")
                }
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

    fun getNormalDate(timeInMillies: Long): String? {
        var date: String? = null
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        date = formatter.format(timeInMillies)
        println("Today is $date")
        return date
    }



    private fun updateView(model: MyFocusResponse) {
        binding.tvFocusName1.text = "Driving"
        binding.tvFocusName2.text = "Studying"
        binding.tvFocusName3.text = "Working"
        binding.tvFocusName4.text = "Friends"
        binding.tvFocusName5.text = "Exercise"
        binding.tvFocusName6.text = "Others"

        binding.tvFocusTime1.text = getDate(model.data?.driving?.time!!.toLong(), "hh:mm:ss")
        binding.tvFocusTime2.text = getDate(model.data?.studying?.time!!.toLong(), "hh:mm:ss")
        binding.tvFocusTime3.text = getDate(model.data?.working?.time!!.toLong(), "hh:mm:ss")
        binding.tvFocusTime4.text = getDate(model.data?.friends?.time!!.toLong(), "hh:mm:ss")
        binding.tvFocusTime5.text = getDate(model.data?.exercise?.time!!.toLong(), "hh:mm:ss")
        binding.tvFocusTime6.text = getDate(model.data?.others?.time!!.toLong(), "hh:mm:ss")

        binding.tvFocusDate1.text = model.data?.driving?.per.toString()
        binding.tvFocusDate2.text = model.data?.studying?.per.toString()
        binding.tvFocusDate3.text = model.data?.working?.per.toString()
        binding.tvFocusDate4.text = model.data?.friends?.per.toString()
        binding.tvFocusDate5.text = model.data?.exercise?.per.toString()
        binding.tvFocusDate6.text = model.data?.others?.per.toString()

    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }



}

fun getMilliFromDate(dateFormat: String?): Long {
    var date = Date()
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    try {
        date = formatter.parse(dateFormat)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    println("Today is $date")
    return date.time
}