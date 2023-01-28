package com.cc.cenntrum.ui.fragments


import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.*
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentMapBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.Points
import com.cc.cenntrum.models.UserResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.*
import com.cc.cenntrum.utils.GlobalData.pointsStartTime
import com.cc.cenntrum.utils.GlobalData.typeStartTime
import com.cc.cenntrum.utils.MapUtils.destinationLatitude
import com.cc.cenntrum.utils.MapUtils.destinationLongitude
import com.cc.cenntrum.utils.MapUtils.getDirection
import com.cc.cenntrum.utils.MapUtils.getDirectionURL
import com.cc.cenntrum.utils.MapUtils.originLatitude
import com.cc.cenntrum.utils.MapUtils.originLongitude
import com.cc.cenntrum.utils.ToastUtils.showToast
import com.cc.cenntrum.viewmodels.MainViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import android.os.Handler
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import com.cc.cenntrum.ui.activities.MapBoxTurnByTurnActivity
import gun0912.tedimagepicker.util.ToastUtil
import kotlinx.coroutines.coroutineScope

@AndroidEntryPoint
class MapFragment : Fragment(), View.OnClickListener, OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private lateinit var binding: FragmentMapBinding

    private var prevSelectedOption: Int = 1

    private lateinit var mContext: MainActivity

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private var mLocationRequest: LocationRequest? = null
    private var mMap: GoogleMap? = null


    private lateinit var mapFragment: SupportMapFragment
//    private lateinit var countDownTimer: Timer
//    private lateinit var timerRunnable: Runnable
//    private val timerHandler: Handler = Handler()

    //    private val viewModel: MainViewModel by viewModels()
    //  private val args: MapFragmentArgs by navArgs()
    private lateinit var viewModel: MainViewModel
    private var phraseTimerTask: TimerTask? = null
    private var phraseTimerTask2: TimerTask? = null


    private var changePhrase: Timer? = null
    private var changePhrase2: Timer? = null

    private var userID: Int? = null
    private var earningPoint: Int? = null
    private var pointsTime: Long? = null

    lateinit var hour: TextView
    lateinit var minutes: TextView
    lateinit var seconds: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        MyApp.isMapVisit = true
        viewModel = ViewModelProvider(mContext).get(MainViewModel::class.java)
        initViews()
        autoCompleteSuggestion()



        viewModel.pointsResponse.observe(mContext, androidx.lifecycle.Observer {
            when (it) {
                is APIResponse.Loading -> {
                    //  LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    //    LoadingUtils.pauseLoading()
                    //    showToast(requireContext(), "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    //   LoadingUtils.pauseLoading()
                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                    val response = it.data as UserResponse

                    if (response.status == true) {

                        //  showToast(requireContext(), response.message.toString())

                        if (response.message == "Points Added") {
                            //   mContext.playSound()
                            updatePoints()
                            updatePhrase()
                        }
                    } else {
                        //  showToast(requireContext(), response.message.toString())
                    }

                }
                APIResponse.Starting -> {}
            }
        })


        return binding.root
    }

    var autocompleteSupportFragment1: AutocompleteSupportFragment? = null
    private fun autoCompleteSuggestion() {
        // Initialize Autocomplete Fragments
        // from the main activity layout file
        if (autocompleteSupportFragment1 == null)
            autocompleteSupportFragment1 =
                childFragmentManager.findFragmentById(R.id.autocomplete_fragment1) as AutocompleteSupportFragment?

        // Information that we wish to fetch after typing
        // the location and clicking on one of the options
        autocompleteSupportFragment1!!.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.LAT_LNG,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL
            )
        )





        // Display the fetched information after clicking on one of the options
        autocompleteSupportFragment1!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                MapUtils.isMapRunning = false

                // Information about the place
                val name = place.name
                val address = place.address
                // val phone = place.phoneNumber.toString()
                val latlng = place.latLng
                val latitude = latlng?.latitude
                val longitude = latlng?.longitude

                val isOpenStatus: String = if (place.isOpen == true) {
                    "Open"
                } else {
                    "Closed"
                }

                val rating = place.rating
                val userRatings = place.userRatingsTotal

                mMap!!.clear()


                // add current location
                //   MapUtils.addMarker(originLatitude, originLongitude, mMap!!, mContext)
                // add search location
                MapUtils.addMarker(latlng, address, mMap!!)

                destinationLatitude = latitude!!
                destinationLongitude = longitude!!
                binding.getDirection.isEnabled = true


//                binding.tv1.text = "Name: $name \nAddress: $address \nPhone Number: phone \n" +
//                        "Latitude, Longitude: $latitude , $longitude \nIs open: $isOpenStatus \n" +
//                        "Rating: $rating \nUser ratings: $userRatings"
            }

            override fun onError(status: Status) {
                MapUtils.isMapRunning = false
                Toast.makeText(
                    mContext,
                    "Some error occurred : ${status.statusMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.v("TAG566", "Some error occurred : ${status.statusMessage}")
            }
        })
    }


    private fun initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Permissions.checkNetworkPermissions(mContext))
                Permissions.requestNetworkPermissions(mContext)
        }


//        mContext.selectedOption = args.position
//        prevSelectedOption = args.position


        prevSelectedOption = mContext.selectedOption

        updateView(mContext.selectedOption)
        // data update
        updatePoints()

        Log.v("TAG2", "onCreateView")
        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(
                mContext.applicationContext,
                (mContext.applicationContext as MyApp).apiKey
            )
        }

        typeStartTime = System.currentTimeMillis().toString()


        // Obtain the SupportMapFragment and get notified
        // when the map is ready to be used.
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        // at last we calling our map fragment to update.
        // getSupportFragmentManager().findFragmentById(R.id.map).getMapAsync(this)
        mapFragment.getMapAsync(this)

        updatePhrase()


        binding.getDirection.isEnabled = false

        binding.layout1.setOnClickListener(this)
        binding.layout2.setOnClickListener(this)
        binding.layout3.setOnClickListener(this)
        binding.layout4.setOnClickListener(this)
        binding.layout5.setOnClickListener(this)
        binding.layout6.setOnClickListener(this)


        binding.drawer.setOnClickListener(this)
        binding.back.setOnClickListener(this)
        binding.getDirection.setOnClickListener(this)

        // initialize left and right drawer menu


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


        hour = binding.layoutTimer.findViewById(R.id.tv_hour)
        minutes = binding.layoutTimer.findViewById(R.id.tv_minute)
        seconds = binding.layoutTimer.findViewById(R.id.tv_second)

    }


    private fun updatePhrase() {
        val rn = Random()
        val answer: Int = rn.nextInt(478) + 1
        val answer2: Int = rn.nextInt(26) + 1
        Log.v("TAG33", "value :$answer")
        Log.v("TAG33", "value2 :$answer2")
        CoroutineScope(Dispatchers.Main).launch {
            binding.phrase.text = Constants.map.get(answer)
            //    binding.phrase.visibility = View.GONE
            binding.image.setImageResource(Constants.imgMap.get(answer2)!!)
        }
    }

    private fun updatePoints() {
        val userData = SavedPreference.getUserData(mContext)
        userID = userData?.id
        earningPoint = userData?.earningPoint
        pointsTime = userData?.pointTime!!.toLong() * 1000

        //  ToastUtil.showToast("points time :$pointsTime")

        binding.chmpName.text = userData?.championDetail?.userName
        binding.chmpCountry.text = userData?.championDetail?.country
        binding.chmpPoints.text = userData?.championDetail?.points

        binding.myPoints.text = userData?.totalPoints.toString()
        binding.tvRanking.text = userData?.userRank.toString()
        binding.tvRanking2.text = userData?.userRank.toString()
    }

    private fun updateView(pos: Int) {
        when (pos) {
            1 -> {
                binding.tv1.setBackgroundResource(R.drawable.bg_driving)
                binding.iv1.setImageResource(R.drawable.circle_filled)
                mContext.selectedOption = 1
                binding.latoutImage.visibility = View.GONE
                binding.timerOuterGroup.visibility = View.VISIBLE
                binding.latoutMap.visibility = View.VISIBLE
                viewModel.sendDrivingNotification("${SavedPreference.getUserName(mContext)} is driving now!")
            }
            2 -> {
                binding.tv2.setBackgroundResource(R.drawable.bg_driving)
                binding.iv2.setImageResource(R.drawable.circle_filled)
                mContext.selectedOption = 2
                binding.latoutImage.visibility = View.VISIBLE
                binding.timerOuterGroup.visibility = View.INVISIBLE
                binding.latoutMap.visibility = View.GONE
                updatePhrase()
            }

            3 -> {
                binding.tv3.setBackgroundResource(R.drawable.bg_driving)
                binding.iv3.setImageResource(R.drawable.circle_filled)
                mContext.selectedOption = 3
                binding.latoutImage.visibility = View.VISIBLE
                binding.timerOuterGroup.visibility = View.INVISIBLE
                binding.latoutMap.visibility = View.GONE
                updatePhrase()
            }

            4 -> {
                binding.tv4.setBackgroundResource(R.drawable.bg_driving)
                binding.iv4.setImageResource(R.drawable.circle_filled)
                mContext.selectedOption = 4
                binding.latoutImage.visibility = View.VISIBLE
                binding.latoutMap.visibility = View.GONE
                binding.timerOuterGroup.visibility = View.INVISIBLE
                updatePhrase()
            }

            5 -> {
                binding.tv5.setBackgroundResource(R.drawable.bg_driving)
                binding.iv5.setImageResource(R.drawable.circle_filled)
                mContext.selectedOption = 5
                binding.latoutImage.visibility = View.VISIBLE
                binding.latoutMap.visibility = View.GONE
                binding.timerOuterGroup.visibility = View.INVISIBLE
                updatePhrase()
            }

            6 -> {
                binding.tv6.setBackgroundResource(R.drawable.bg_driving)
                binding.iv6.setImageResource(R.drawable.circle_filled)
                mContext.selectedOption = 6
                binding.latoutImage.visibility = View.VISIBLE
                binding.latoutMap.visibility = View.GONE
                binding.timerOuterGroup.visibility = View.INVISIBLE
                updatePhrase()
            }
        }
    }


    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.layout1 -> {
                viewModel.addPoints(
                    endPoint = "start_activity",
                    Points(
                        userId = userID,
                        type = mContext.selectedOption,
                        startTime = typeStartTime,
                        endTime = System.currentTimeMillis().toString()
                    )
                )
                updateView(1)
            }
            R.id.layout2 -> {
                viewModel.addPoints(
                    endPoint = "start_activity",
                    Points(
                        userId = userID,
                        type = mContext.selectedOption,
                        startTime = typeStartTime,
                        endTime = System.currentTimeMillis().toString()
                    )
                )
                updateView(2)
            }

            R.id.layout3 -> {
                viewModel.addPoints(
                    endPoint = "start_activity",
                    Points(
                        userId = userID,
                        type = mContext.selectedOption,
                        startTime = typeStartTime,
                        endTime = System.currentTimeMillis().toString()
                    )
                )
                updateView(3)
            }

            R.id.layout4 -> {
                viewModel.addPoints(
                    endPoint = "start_activity",
                    Points(
                        userId = userID,
                        type = mContext.selectedOption,
                        startTime = typeStartTime,
                        endTime = System.currentTimeMillis().toString()
                    )
                )
                updateView(4)
            }

            R.id.layout5 -> {
                viewModel.addPoints(
                    endPoint = "start_activity",
                    Points(
                        userId = userID,
                        type = mContext.selectedOption,
                        startTime = typeStartTime,
                        endTime = System.currentTimeMillis().toString()
                    )
                )
                updateView(5)
            }

            R.id.layout6 -> {
                viewModel.addPoints(
                    endPoint = "start_activity",
                    Points(
                        userId = userID,
                        type = mContext.selectedOption,
                        startTime = typeStartTime,
                        endTime = System.currentTimeMillis().toString()
                    )
                )
                updateView(6)
            }
            R.id.drawer -> {
                mContext.toggleLeftDrawer(binding.drawerLayout, binding.leftDrawerMenu.root)
                return
            }
            R.id.back -> {
                mContext.toggleRightDrawer(binding.drawerLayout, binding.rightDrawerMenu.root)
                return
            }
            R.id.get_direction -> {
                MapUtils.isMapRunning = true
                binding.getDirection.isEnabled = false
                startActivity(Intent(mContext, MapBoxTurnByTurnActivity::class.java))

//                mapFragment.getMapAsync {
//                    mMap = it
//                    val originLocation = LatLng(originLatitude, originLongitude)
//                    mMap!!.addMarker(MarkerOptions().position(originLocation))
//                    val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
//                    mMap!!.addMarker(MarkerOptions().position(destinationLocation))
//                    val urll = getDirectionURL(
//                        originLocation,
//                        destinationLocation,
//                        (mContext.applicationContext as MyApp).apiKey
//                    )
//                    CoroutineScope(Dispatchers.Main).launch {
//                        getDirection(urll, binding.tvDistance, mMap!!)
//                    }
//                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
//                }
                return
            }

        }

        typeStartTime = System.currentTimeMillis().toString()
        mContext.countDownTimer.cancel()
        mContext.startCountDownTimer()
        mContext.timerStop()
        mContext.timerStart(System.currentTimeMillis())
        if (prevSelectedOption != mContext.selectedOption) {
            updatePreviousOption(prevSelectedOption)
            prevSelectedOption = mContext.selectedOption
        }
    }

    private fun updatePreviousOption(option: Int) {
        when (option) {
            1 -> {
                binding.tv1.setBackgroundResource(R.drawable.bg_driving_unselected)
                binding.iv1.setImageResource(R.drawable.circle)
            }
            2 -> {
                binding.tv2.setBackgroundResource(R.drawable.bg_driving_unselected)
                binding.iv2.setImageResource(R.drawable.circle)

            }

            3 -> {
                binding.tv3.setBackgroundResource(R.drawable.bg_driving_unselected)
                binding.iv3.setImageResource(R.drawable.circle)

            }

            4 -> {
                binding.tv4.setBackgroundResource(R.drawable.bg_driving_unselected)
                binding.iv4.setImageResource(R.drawable.circle)

            }

            5 -> {
                binding.tv5.setBackgroundResource(R.drawable.bg_driving_unselected)
                binding.iv5.setImageResource(R.drawable.circle)

            }

            6 -> {
                binding.tv6.setBackgroundResource(R.drawable.bg_driving_unselected)
                binding.iv6.setImageResource(R.drawable.circle)

            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
//
//        mMap = googleMap!!
//        val originLocation = LatLng(originLatitude, originLongitude)
//        mMap!!.clear()
//        mMap!!.addMarker(MarkerOptions().position(originLocation))
//        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
//
        Log.v("TAG2", "onMapReady :505")
        //     MyApp.showToast(mContext, "onMapReady")


        mMap = googleMap
        mMap?.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            uiSettings.isCompassEnabled = true
        }

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Permissions.checkNetworkPermissions(mContext)) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            } else {
                Permissions.requestNetworkPermissions(mContext)
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }

    }

    private fun getCurrentLocation() {
        val locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager?
        val provider = locationManager!!.getBestProvider(Criteria(), true)
        if (!Permissions.checkNetworkPermissions(mContext)) {
            return
        }
        val locations = locationManager.getLastKnownLocation(provider!!)
        val providerList = locationManager.allProviders
        if (null != locations && null != providerList && providerList.size > 0) {
            originLatitude = locations.longitude
            originLongitude = locations.latitude

        }
    }


    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(mContext)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
        Log.v("TAG2", "buildGoogleApiClient :538")
    }


    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (Permissions.checkNetworkPermissions(mContext)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient!!,
                mLocationRequest!!, this
            )
        } else {
            Permissions.requestNetworkPermissions(mContext)
        }

        Log.v("TAG2", "onConnected :554")
        // showToast(mContext, "onConnected")
    }

    override fun onConnectionSuspended(i: Int) {
        Log.v("TAG2", "onConnectionSuspended :558")
        showToast(mContext, "onConnectionSuspended")

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        showToast(mContext, "onConnectionFailed")
    }


    override fun onLocationChanged(location: Location) {
        Log.v("TAG2", "onLocationChanged :565")
        //  showToast(mContext, "Location is changing...")

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Showing Current Location Marker on Map
        originLatitude = location.latitude
        originLongitude = location.longitude
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
//        val locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager?
//        val provider = locationManager!!.getBestProvider(Criteria(), true)
//        if (!Permissions.checkNetworkPermissions(mContext)) {
//            return
//        }
//        val locations = locationManager.getLastKnownLocation(provider!!)
//        val providerList = locationManager.allProviders
//        if (null != locations && null != providerList && providerList.size > 0) {
//            val longitude = locations.longitude
//            val latitude = locations.latitude
//            val geocoder = Geocoder(
//                mContext,
//                Locale.getDefault()
//            )
//            try {
//                val listAddresses = geocoder.getFromLocation(
//                    latitude,
//                    longitude, 1
//                )
//                if (null != listAddresses && listAddresses.size > 0) {
//                    val state = listAddresses[0].adminArea
//                    val country = listAddresses[0].countryName
//                    val subLocality = listAddresses[0].subLocality
//                    markerOptions.title(
//                        "" + latLng + "," + subLocality + "," + state
//                                + "," + country
//                    )
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient!!,
                this
            )
        }
    }


//    private fun timerStart(currentTimeMillis: Long) {
//
//        pointsStartTime = currentTimeMillis.toString()
//        timerRunnable = object : Runnable {
//            override fun run() {
//                val elapsedTime = System.currentTimeMillis() - currentTimeMillis
//                binding.tvTimer.text = TimerUtils.timeFormater(elapsedTime.toFloat())
//                timerHandler.postDelayed(this, 1000)
//                val secs = (elapsedTime / 1000)
//
//            }
//        }
//        //   if (recordTimerHandler == null) recordTimerHandler = Handler()
//        timerHandler.post(timerRunnable)
//    }
//
//    private fun timerStop() {
//        timerHandler.removeCallbacks(timerRunnable)
//    }
//
//    private fun startCountDownTimer() {
//
//        countDownTimer = Timer()
//        countDownTimer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                Handler(Looper.getMainLooper()).post(Runnable {
//                    //   showToast(mContext, getString(R.string.points_added))
//
//                    viewModel.addPoints(
//                        endPoint = "add_point",
//                        Points(
//                            userId = userID,
//                            type = mContext.selectedOption,
//                            startTime = pointsStartTime,
//                            endTime = System.currentTimeMillis().toString(),
//                            points = earningPoint
//                        )
//                    )
//                    pointsStartTime = System.currentTimeMillis().toString()
//                })
//            }
//        }, pointsTime!!, pointsTime!!)
//    }


    override fun onPause() {
        Log.v("TAG2", "onPause")
        MyApp.selectedOption = mContext.selectedOption


       //     Log.v("TAG2", "onPause2 :${autocompleteSupportFragment1!!.requireView().isEnabled}")



//        countDownTimer.cancel()
//        timerStop()

//
//        changePhrase?.cancel()
//        changePhrase = null
//        phraseTimerTask = null


        changePhrase2?.cancel()
        changePhrase2 = null
        phraseTimerTask2 = null

        super.onPause()
    }

    override fun onDestroy() {
        Log.v("TAG2", "onDestroy")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.v("TAG2", "onDestroyView")
//        countDownTimer.cancel()
//        timerStop()
        super.onDestroyView()
    }


    override fun onResume() {
        Log.v("TAG2", "onRe4sume")
     //   Log.v("TAG2", "onRe4sume2 :${autocompleteSupportFragment1!!.requireView().isEnabled} : ${autocompleteSupportFragment1!!.isStateSaved}")
        if (binding.tvDistance.text.toString().isNotEmpty()) {
            binding.tvDistance.text = ""
        }


        mContext.binding.bottomNav.visibility = View.VISIBLE

        if (!mContext.isTimerRunning) {
            //  mContext.tvTimer = binding.tvTimer

            mContext.isTimerRunning = true
            mContext.startCountDownTimer()
            mContext.timerStart(System.currentTimeMillis())
        }
//        startCountDownTimer()
//        timerStart(System.currentTimeMillis())

//        if (changePhrase == null) {
//            changePhrase = Timer()
//
//            if(phraseTimerTask == null) {
//                phraseTimerTask = object : TimerTask() {
//                    @RequiresApi(Build.VERSION_CODES.O)
//                    override fun run() {
//                        updatePhrase()
//                    }
//
//                }
//            }
//            changePhrase?.scheduleAtFixedRate(phraseTimerTask, 0, 5000)
//        }

        if (changePhrase2 == null) {
            changePhrase2 = Timer()

            if (phraseTimerTask2 == null) {
                phraseTimerTask2 = object : TimerTask() {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun run() {
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.tvTimer.text = mContext.time
                            val str = mContext.time.split(":")
                            hour.text = str[0]
                            minutes.text = str[1]
                            seconds.text = str[2]
                        }
                    }

                }
            }
            changePhrase2?.scheduleAtFixedRate(phraseTimerTask2, 0, 1000)
        }

        super.onResume()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }


}