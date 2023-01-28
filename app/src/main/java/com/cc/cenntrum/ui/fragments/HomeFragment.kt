package com.cc.cenntrum.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentHomeBinding
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.*
import com.cc.cenntrum.utils.Constants.REQUEST_GPS
import com.cc.cenntrum.utils.ToastUtils.showToast
import com.cc.cenntrum.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var onSwitchPosition: Int = 1

//    private val viewModel: MainViewModel by viewModels()

    private lateinit var viewModel: MainViewModel
    private lateinit var mContext:  MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(mContext).get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // onMobileGps(mContext)
        MyApp.homeFragment = this
        MyApp.locationManager =
            mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager




        binding.continueBtn.setOnClickListener {
            onMobileGps(mContext)
        }

        binding.apply {
            switch1.isChecked = true
            switch1.setOnClickListener {
                if(switch1.isChecked){
                    onSwitchPosition = 1
                    offOtherSwitches()
                }else{
                    onSwitchPosition = 0
                }
            }
            switch2.setOnClickListener {
                if(switch2.isChecked){
                    onSwitchPosition = 2
                    offOtherSwitches()
                }else{
                    onSwitchPosition = 0
                }
            }
            switch3.setOnClickListener {
                if(switch3.isChecked){
                    onSwitchPosition = 3
                    offOtherSwitches()
                }else{
                    onSwitchPosition = 0
                }
            }
            switch4.setOnClickListener {
                if(switch4.isChecked){
                    onSwitchPosition = 4
                    offOtherSwitches()
                }else{
                    onSwitchPosition = 0
                }
            }
            switch5.setOnClickListener {
                if(switch5.isChecked){
                    onSwitchPosition = 5
                    offOtherSwitches()
                }else{
                    onSwitchPosition = 0
                }
            }
            switch6.setOnClickListener {
                if(switch6.isChecked){
                    onSwitchPosition = 6
                    offOtherSwitches()
                }else{
                    onSwitchPosition = 0
                }
            }
        }






        return binding.root
    }

    private fun offOtherSwitches( ) {
        when (onSwitchPosition) {
            1 -> {
                binding.apply {

                    switch2.isChecked = false
                    switch3.isChecked = false
                    switch4.isChecked = false
                    switch5.isChecked = false
                    switch6.isChecked = false
                 //   onSwitchPosition = if(isChecked) 1 else 0
                }
            }
            2 -> {
                binding.apply {
                    switch1.isChecked = false

                    switch3.isChecked = false
                    switch4.isChecked = false
                    switch5.isChecked = false
                    switch6.isChecked = false
                }
            }
            3 -> {
                binding.apply {
                    switch1.isChecked = false
                    switch2.isChecked = false

                    switch4.isChecked = false
                    switch5.isChecked = false
                    switch6.isChecked = false
                }
            }
            4 -> {
                binding.apply {
                    switch1.isChecked = false
                    switch2.isChecked = false
                    switch3.isChecked = false

                    switch5.isChecked = false
                    switch6.isChecked = false
                }
            }
            5 -> {
                binding.apply {
                    switch1.isChecked = false
                    switch2.isChecked = false
                    switch3.isChecked = false
                    switch4.isChecked = false

                    switch6.isChecked = false
                }
            }
            6 -> {
                binding.apply {
                    switch1.isChecked = false
                    switch2.isChecked = false
                    switch3.isChecked = false
                    switch4.isChecked = false
                    switch5.isChecked = false

                }
            }
        }
    }

    private fun onMobileGps(context: Context) {
        // check whether device is GPS supported or not?
        if (Permissions.hasGPSDevice()) {
            // Supported

            // check whether is GPS on or not?
            if (Permissions.isGpsEnabled()) {
                // on

                // check whether  runtime permissions is enable on not?
                if (Permissions.checkNetworkPermissions(context)) {
                    // Enable
                    try {
                        if(onSwitchPosition != 0){
                            mContext.selectedOption = onSwitchPosition
                            val action = HomeFragmentDirections.actionHomeFragmentToMapFragment(onSwitchPosition)
                            findNavController().navigate(action)
                        }else{
                            showToast(requireContext(),"please select type!")
                        }

                    } catch (e: Exception) {
                    }
                } else {
                    // not enable
                    // request permission now
                    Permissions.requestNetworkPermissions(context)
                }
            } else {
                // off
                showToast(context, "Gps not enabled")
                // send request to turn on Location(GPS)
                Permissions.enableGPS(context)
            }
        } else {
            // not Supported
            showToast(context, "Gps not Supported")
            //  finish()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.v("TAG1", "Fragment")
        when (requestCode) {
            REQUEST_GPS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.v("TAG1", "GPS on")

                    try {

                        onMobileGps(mContext)

                    } catch (e: Exception) {
                        Log.d(
                            "TAG1",
                            "" +
                                    "onActivityResult Error:${e.localizedMessage}"
                        )
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Log.v("TAG1", "GPS OFF")
                    Permissions.googleApiClient = null
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.v("TAG1", "Permission result  fragment")

        when (requestCode) {
            Constants.MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    if (Permissions.checkNetworkPermissions(mContext)) {

//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient()
//                        }
//                        mMap!!.isMyLocationEnabled = true

                        onMobileGps(mContext)
                        Log.v("TAG2", "onRequestPermissionsResult granted 196")
                    }
                } else {
                    Toast.makeText(
                        requireContext(), "permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }


    }


    override fun onResume() {
        super.onResume()
        if(GlobalData.user == null)
        SavedPreference.getUserID(mContext)?.let { viewModel.getUserDetail(it) }
    }


}