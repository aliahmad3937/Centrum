package com.cc.cenntrum.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentPointsBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.FilterRequest
import com.cc.cenntrum.models.FilterPointsResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.LoadingUtils
import com.cc.cenntrum.utils.SavedPreference
import com.cc.cenntrum.utils.ToastUtils
import com.cc.cenntrum.viewmodels.MainViewModel
import com.ycuwq.datepicker.date.DatePickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Points : Fragment() {

    private lateinit var binding: FragmentPointsBinding
    private lateinit var mContext: MainActivity
    private val viewModel: MainViewModel by viewModels()

    private var from: String? = null
    private var to: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_points, container, false)


        mContext.isPurchase.observe(viewLifecycleOwner){
            if(it){
                updateViews()
            }
        }

        updateViews()

        viewModel.filterPointsResponse.observe(viewLifecycleOwner, Observer{
            when (it) {
                is APIResponse.Loading -> {
                    LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    LoadingUtils.pauseLoading()
                    ToastUtils.showToast(requireContext(), "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    LoadingUtils.pauseLoading()
                    val response = it.data as FilterPointsResponse

                    if (response.status == true) {
                        ToastUtils.showToast(requireContext(), response.totalPoints.toString())
                    } else {
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {}
            }
        })



        binding.etStartdate.setOnClickListener(View.OnClickListener { v: View? ->
            val datePickerDialogFragment = DatePickerDialogFragment()

            datePickerDialogFragment.run {
                setOnDateChooseListener { year, month, day ->
                    var day1 = day.toString()
                    var month1 = month.toString()
                    day1 = if (day1.length == 1) "0$day1" else day1
                    month1 = if (month1.length == 1) "0$month1" else month1
                    binding.etStartdate.text = "$day1-$month1-$year"
                    from = getTimeInMillis(day, month, year).toString()

                    if (to != null) {
                      //  ToastUtils.showToast(requireContext(), "end not null")
                        viewModel.getFilteredPoints(
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
            val datePickerDialogFragment = DatePickerDialogFragment()
            datePickerDialogFragment.run {
                setOnDateChooseListener { year, month, day ->
                    var day1 = day.toString()
                    var month1 = month.toString()
                    day1 = if (day1.length == 1) "0$day1" else day1
                    month1 = if (month1.length == 1) "0$month1" else month1
                    binding.etEnddate.text = "$day1-$month1-$year"

                    to = getTimeInMillis(day, month, year).toString()

                    if (from != null) {
                     //   ToastUtils.showToast(requireContext(), "start not null")
                        viewModel.getFilteredPoints(
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

    private fun updateViews() {
        val exchPoints = SavedPreference.getUserData(mContext)?.exchangePoints
        val availPoints = SavedPreference.getUserData(mContext)?.totalPoints
        binding.btnAccum.text = "Accumulated: ${availPoints!! + exchPoints!!}"
        binding.btnExchange.text = "Exchanged: $exchPoints"
        binding.btnAvailable.text = "Available: $availPoints"



        binding.btnAccum.setOnClickListener {

            findNavController().navigate(R.id.action_points_to_accumulatedPoints)
        }

        binding.btnExchange.setOnClickListener {

            findNavController().navigate(R.id.action_points_to_exchangePoints)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }



}

fun getTimeInMillis(day: Int, month: Int, year: Int): Long {
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    return calendar.timeInMillis
}