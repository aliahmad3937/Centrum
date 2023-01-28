package com.cc.cenntrum.ui.activities

import android.app.AlertDialog
import android.app.KeyguardManager
import android.app.Notification
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.ActivityMainBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.Points
import com.cc.cenntrum.models.PurchaseRequest
import com.cc.cenntrum.models.UserResponse
import com.cc.cenntrum.utils.*
import com.cc.cenntrum.utils.Constants.REQUEST_GPS
import com.cc.cenntrum.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SpaceOnClickListener, BillingProcessor.IBillingHandler {
    // Initialize variables
    lateinit var binding: ActivityMainBinding
    var navController: NavController? = null
    private var mediaPlayer: MediaPlayer? = null

    private val viewModel: MainViewModel by viewModels()
    private lateinit var bp: BillingProcessor
    private lateinit var purchaseInfo: PurchaseInfo
    private var amount: String = "4.99"

    var myKM: KeyguardManager? = null

    val isPurchase:MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Assign variable
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        myKM = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        bp = BillingProcessor(
            this,
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmoz9iCQ4GNtXYLBvqXxRDv9qKF8U+akdO2lws11FZud8tVo9oahEsZLImVOEog8UzRuqOH1NIefg/WASmzzF5GcHIr1ls2FGgQb7w+qku03hIKmlnuPttnK29sc52rkBlH3FqsAA66TSA4n+n3Cz7YwnLXjsAhMRS9xT28rWnNKcApxZSG6CZRhc/9as4B1mB53fv3ZWf+GW2QoEuYqhuVrz7jTtQNeXp0upamPNvpyxGUFXYME2rYj2tNEUH7iitT6/NlD2XWHaYWjDsppAp0YqYyzApro2aY55JqK5h5u4jYlaVycuIjKErguMgDd48nFq+rSHUU5zpfFxWq+ffwIDAQAB",
            this
        )
        bp.initialize()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment!!.navController

        navController!!.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.splashScreen
                || destination.id == R.id.login
                || destination.id == R.id.signUp
                || destination.id == R.id.forgetPassword
                || destination.id == R.id.moreFriends
                || destination.id == R.id.homeFragment
                || destination.id == R.id.mapFragment
            ) {
                binding.bottomNav.visibility = View.GONE
                // ToastUtils.showToast(this,"if")
            } else {

                binding.bottomNav.visibility = View.VISIBLE

            }
        }

        viewModel.getPackagesList()


        addingTabsInBottomBar()





        viewModel.pointsResponse.observe(this, androidx.lifecycle.Observer {
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
                          // playSound()
                              isPurchase.postValue(true)
                        }
                    } else {
                        ToastUtils.showToast(this, response.message.toString())
                    }

                }
                APIResponse.Starting -> {}
            }
        })


    }

    private fun addingTabsInBottomBar() {
        binding.bottomNavigationView.addSpaceItem(
            SpaceItem(
                getString(R.string.my_points),
                R.drawable.ic_my_points
            )
        )
        binding.bottomNavigationView.addSpaceItem(
            SpaceItem(
                getString(R.string.my_rewards),
                R.drawable.ic_my_rewards
            )
        )
        binding.bottomNavigationView.addSpaceItem(
            SpaceItem(
                getString(R.string.incentives),
                R.drawable.ic_incentives
            )
        )
        binding.bottomNavigationView.addSpaceItem(
            SpaceItem(
                getString(R.string.notifications),
                R.drawable.family
            )
        )

        binding.bottomNavigationView.showIconOnly()
        binding.bottomNavigationView.setSpaceOnClickListener(this)
        binding.bottomNavigationView.setCentreButtonSelectable(true)
        binding.bottomNavigationView.setCentreButtonSelected()
        binding.bottomNavigationView.setCentreButtonColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.purple_700
            )
        )
        binding.bottomNavigationView.setActiveCentreButtonBackgroundColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.purple_700
            )
        )


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.v("TAG1", "Main Activity")
        if (requestCode == REQUEST_GPS) {
            MyApp.homeFragment.onActivityResult(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.v("TAG1", "Permission result  activity")
        MyApp.homeFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun showNotificationBadge(badgeCountText: Int) {

        binding.bottomNavigationView.showBadgeAtIndex(
            3,
            badgeCountText,
            ContextCompat.getColor(this, R.color.red)
        );
    }

    fun hideNotificationBadge() {
        binding.bottomNavigationView.hideBadgeAtIndex(3)
    }


    fun promptLogoutConfirmation() {
        //Use the context of current activity
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.logout_2)
        builder.setTitle("Logout!")
        builder.setMessage("Are you sure you want to logout ?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->

                //dont forget to clear any user related data in your preferences

                SavedPreference.clearUserData(this)
                isTimerRunning = false
                countDownTimer.cancel()
                timerStop()
                navController!!.navigate(R.id.action_global_login)
            })
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


    fun drawerClickListener(fragmentID: Int) {
        when (fragmentID) {
            R.id.points -> navController!!.navigate(R.id.points)
            R.id.rewards -> navController!!.navigate(R.id.rewards)
            R.id.incentives -> navController!!.navigate(R.id.incentives)
            R.id.exchange_points -> navController!!.navigate(R.id.exchangePoints)
            R.id.profile -> navController!!.navigate(R.id.profileFragment)
            R.id.buy_subscription -> {}
            R.id.buy_points -> ToastUtils.showToast(this, "buy points")
            R.id.focus -> navController!!.navigate(R.id.myFocus)
            R.id.friends -> navController!!.navigate(R.id.friendsFragment)
            R.id.crypto_wallet -> navController!!.navigate(R.id.myCryptoWallet)
            R.id.terms_conditions -> navController!!.navigate(R.id.termsConditions)
            R.id.policy -> navController!!.navigate(R.id.privacyPolicy)
        }

    }

    fun drawerClickListener(
        fragmentID: Int, drawerLayout: DrawerLayout,
        rightDrawerMenu: View
    ) {
        when (fragmentID) {
            R.id.points -> navController!!.navigate(R.id.points)
            R.id.rewards -> navController!!.navigate(R.id.rewards)
            R.id.incentives -> navController!!.navigate(R.id.incentives)
            R.id.exchange_points -> navController!!.navigate(R.id.exchangePoints)
            R.id.profile -> navController!!.navigate(R.id.profileFragment)
            R.id.buy_subscription -> {
                toggleRightDrawer(drawerLayout, rightDrawerMenu)
                subscModalBottomSheet()
            }
            R.id.buy_points -> {
                toggleRightDrawer(drawerLayout, rightDrawerMenu)
                pointsModalBottomSheet()
            }
            R.id.focus -> navController!!.navigate(R.id.myFocus)
            R.id.friends -> navController!!.navigate(R.id.friendsFragment)
            R.id.crypto_wallet -> navController!!.navigate(R.id.myCryptoWallet)
            R.id.terms_conditions -> navController!!.navigate(R.id.termsConditions)
            R.id.policy -> navController!!.navigate(R.id.privacyPolicy)
        }

    }

    private fun subscModalBottomSheet() {

        Log.v("TAG65", "sublist :" + MyApp.subscripList!![1].title)

        val dialog = BottomSheetDialog(this)


        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)


        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        val btnClose = view.findViewById<ImageView>(R.id.close_sheet)
        val titlePlan1 = view.findViewById<TextView>(R.id.subscr_plan_1)
        val titlePlan2 = view.findViewById<TextView>(R.id.subscr_plan_2)
        val coinsPlan1 = view.findViewById<TextView>(R.id.tv_points_count_1)
        val coinsPlan2 = view.findViewById<TextView>(R.id.tv_points_count_2)
        val plan1Amount = view.findViewById<TextView>(R.id.tv_subscr_amount_1)
        val plan2Amount = view.findViewById<TextView>(R.id.tv_subscr_amount_2)
        val plan1 = view.findViewById<LinearLayout>(R.id.linearLayout1)
        val plan2 = view.findViewById<LinearLayout>(R.id.linearLayout2)

        if (MyApp.subscripList != null) {
            titlePlan1.text = MyApp.subscripList!![0].title
            titlePlan2.text = MyApp.subscripList!![1].title
            coinsPlan1.text = "${MyApp.subscripList!![0].points}X Points"
            coinsPlan2.text = "${MyApp.subscripList!![1].points}X Points"
            //   plan1Amount.text = "$ ${MyApp.subscripList!![0].price}"
            //    plan2Amount.text = "$ ${MyApp.subscripList!![1].points}"


            plan1.setOnClickListener {
                amount = "4.99"
              //  bp.subscribe(this, "cenntrumfivedollars")

                viewModel.addPurchaseSubscription(
                    PurchaseRequest(
                        userId = SavedPreference.getUserID(this) ,
                        pkgId = MyApp.subscripList!![0].id.toString()
                    )
                )

                dialog.dismiss()

            }

            plan2.setOnClickListener {
                amount = "9.99"
              //  bp.subscribe(this, "cenntrumtendollars")
                viewModel.addPurchaseSubscription(
                    PurchaseRequest(
                        userId = SavedPreference.getUserID(this) ,
                        pkgId = MyApp.subscripList!![1].id.toString()
                    )
                )

                dialog.dismiss()
            }


        }


        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnClose.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    private fun pointsModalBottomSheet() {
        Log.v("TAG65", "coinsList :" + MyApp.coinsList!![1].title)
        val dialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_points, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        val btnClose = view.findViewById<ImageView>(R.id.close_sheet)
        val titlePlan1 = view.findViewById<TextView>(R.id.points_plan_1)
        val titlePlan2 = view.findViewById<TextView>(R.id.points_plan_2)
        val titlePlan3 = view.findViewById<TextView>(R.id.points_plan_3)
        val coinsPlan1 = view.findViewById<TextView>(R.id.tv_points_count_1)
        val coinsPlan2 = view.findViewById<TextView>(R.id.tv_points_count_2)
        val coinsPlan3 = view.findViewById<TextView>(R.id.tv_points_count_3)
        val plan1Amount = view.findViewById<TextView>(R.id.tv_buy_amount_1)
        val plan2Amount = view.findViewById<TextView>(R.id.tv_buy_amount_2)
        val plan3Amount = view.findViewById<TextView>(R.id.tv_buy_amount_3)
        val plan1 = view.findViewById<LinearLayout>(R.id.linearLayout1)
        val plan2 = view.findViewById<LinearLayout>(R.id.linearLayout2)
        val plan3 = view.findViewById<LinearLayout>(R.id.linearLayout3)

        if (MyApp.coinsList != null) {
            titlePlan1.text = MyApp.coinsList!![0].title
            titlePlan2.text = MyApp.coinsList!![1].title
            titlePlan3.text = MyApp.coinsList!![2].title
            coinsPlan1.text = "${MyApp.coinsList!![0].coins} Points"
            coinsPlan2.text = "${MyApp.coinsList!![1].coins} Points"
            coinsPlan3.text = "${MyApp.coinsList!![2].coins} Points"
            //   plan1Amount.text = "$ ${MyApp.coinsList!![0].price}"
            //   plan2Amount.text = "$ ${MyApp.coinsList!![1].points}"
            //   plan3Amount.text = "$ ${MyApp.coinsList!![1].points}"


            plan1.setOnClickListener {
             //   ToastUtil.showToast("Plan 1")
                // amount = "4.99"
             //   bp.purchase(this, "")

                       viewModel.addPurchasePoints(
                        PurchaseRequest(
                            userId = SavedPreference.getUserID(this) ,
                            pkgId = MyApp.coinsList!![0].id.toString()
                        )
                    ){
                           isPurchase.postValue(true)
                       }
                dialog.dismiss()
            }

            plan2.setOnClickListener {
               // ToastUtil.showToast("Plan 2")
                // amount = "4.99"

              //  bp.purchase(this, "")


                viewModel.addPurchasePoints(
                    PurchaseRequest(
                        userId = SavedPreference.getUserID(this) ,
                        pkgId = MyApp.coinsList!![1].id.toString()
                    )
                ){
                    isPurchase.postValue(true)
                }

                dialog.dismiss()
            }

            plan3.setOnClickListener {
              //  ToastUtil.showToast("Plan 3")
                //   amount = "4.99"
             //   bp.purchase(this, "")

                viewModel.addPurchasePoints(
                    PurchaseRequest(
                        userId = SavedPreference.getUserID(this) ,
                        pkgId = MyApp.coinsList!![2].id.toString()
                    )
                ){
                    isPurchase.postValue(true)
                }
                dialog.dismiss()
            }

        }


        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnClose.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    fun leftDrawerClickListener(
        layout: View,
        drawerLayout: DrawerLayout,
        leftDrawerMenu: View
    ) {

        layout.findViewById<ImageView>(R.id.close_drawer).setOnClickListener {
            toggleLeftDrawer(drawerLayout = drawerLayout, leftDrawerMenu = leftDrawerMenu)
        }

        layout.findViewById<TextView>(R.id.points).setOnClickListener {
            drawerClickListener(R.id.points)
        }

        layout.findViewById<TextView>(R.id.rewards).setOnClickListener {
            drawerClickListener(R.id.rewards)
        }


        layout.findViewById<TextView>(R.id.incentives).setOnClickListener {
            drawerClickListener(R.id.incentives)
        }


        layout.findViewById<TextView>(R.id.exchange_points).setOnClickListener {
            drawerClickListener(R.id.exchange_points)
        }

        layout.findViewById<TextView>(R.id.logout).setOnClickListener {
            promptLogoutConfirmation()
        }

        layout.findViewById<TextView>(R.id.drawer_user_name).text = SavedPreference.getUserName(this)
    }


    fun rightDrawerClickListener(
        layout: View,
        drawerLayout: DrawerLayout,
        rightDrawerMenu: View
    ) {

        layout.findViewById<ImageView>(R.id.close_drawer).setOnClickListener {
            toggleRightDrawer(drawerLayout = drawerLayout, rightDrawerMenu = rightDrawerMenu)
        }

        layout.findViewById<TextView>(R.id.profile).setOnClickListener {
            drawerClickListener(R.id.profile)

        }

        layout.findViewById<TextView>(R.id.buy_subscription).setOnClickListener {
            drawerClickListener(R.id.buy_subscription, drawerLayout, rightDrawerMenu)
        }


        layout.findViewById<TextView>(R.id.buy_points).setOnClickListener {
            drawerClickListener(R.id.buy_points, drawerLayout, rightDrawerMenu)
        }


        layout.findViewById<TextView>(R.id.focus).setOnClickListener {
            drawerClickListener(R.id.focus)
        }

        layout.findViewById<TextView>(R.id.friends).setOnClickListener {
            drawerClickListener(R.id.friends)
        }

        layout.findViewById<TextView>(R.id.crypto_wallet).setOnClickListener {
            drawerClickListener(R.id.crypto_wallet)
        }

        layout.findViewById<TextView>(R.id.terms_conditions).setOnClickListener {
            drawerClickListener(R.id.terms_conditions)

        }

        layout.findViewById<TextView>(R.id.policy).setOnClickListener {
            drawerClickListener(R.id.policy)
        }

        layout.findViewById<TextView>(R.id.drawer_user_name).text = SavedPreference.getUserName(this)
    }


    fun toggleLeftDrawer(drawerLayout: DrawerLayout, leftDrawerMenu: View) {
        leftDrawerMenu.findViewById<TextView>(R.id.drawer_user_name).text =
            SavedPreference.getUserName(this)
        if (drawerLayout.isDrawerOpen(leftDrawerMenu)) drawerLayout.closeDrawer(leftDrawerMenu) else drawerLayout.openDrawer(
            leftDrawerMenu
        )
    }

    fun toggleRightDrawer(drawerLayout: DrawerLayout, rightDrawerMenu: View) {
        rightDrawerMenu.findViewById<TextView>(R.id.drawer_user_name).text =
            SavedPreference.getUserName(this)
        if (drawerLayout.isDrawerOpen(rightDrawerMenu)) drawerLayout.closeDrawer(rightDrawerMenu) else drawerLayout.openDrawer(
            rightDrawerMenu
        )
    }

    override fun onCentreButtonClick() {
        if (MyApp.isMapVisit) {
            val bundle = Bundle()
            bundle.putInt("position", selectedOption)
            navController!!.navigate(R.id.mapFragment, bundle)
        } else {
            navController!!.navigate(R.id.homeFragment)
        }

        binding.bottomNavigationView.setCentreButtonSelected()

        //    binding.bottomNavigationView.setActiveCentreButtonBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.black))
        //    binding.bottomNavigationView.setCentreButtonColor(ContextCompat.getColor(this@MainActivity, R.color.black))
        //  Toast.makeText(this@MainActivity, "onCentreButtonClick", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(itemIndex: Int, itemName: String) {
        // binding.bottomNavigationView.setCentreButtonSelected()
        //    binding.bottomNavigationView.setActiveCentreButtonBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.purple_700))
        binding.bottomNavigationView.setCentreButtonColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.purple_700
            )
        )
//                binding.bottomNavigationView.setCentreButtonSelected()
        when (itemIndex) {
            0 -> navController!!.navigate(R.id.points)
            1 -> navController!!.navigate(R.id.rewards)
            2 -> navController!!.navigate(R.id.incentives)
            3 -> navController!!.navigate(R.id.notifications)
        }

        //  Toast.makeText(this@MainActivity, "$itemIndex $itemName", Toast.LENGTH_SHORT).show()
    }

    override fun onItemReselected(itemIndex: Int, itemName: String) {
        // Toast.makeText(this@MainActivity, "$itemIndex $itemName", Toast.LENGTH_SHORT).show()
        when (itemIndex) {
            0 -> {
                navController!!.navigate(R.id.points)
            }
            1 -> navController!!.navigate(R.id.rewards)
            2 -> navController!!.navigate(R.id.incentives)
            3 -> navController!!.navigate(R.id.notifications)
        }

    }

    fun playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        }
        mediaPlayer?.start()
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {

        if (details?.purchaseData?.purchaseState.toString() == "PurchasedSuccessfully") {
//            viewModel.addSubscription(
//                MyApp.data?.id.toString(),
//                amount,
//                details?.purchaseData?.purchaseTime.toString(),
//                "Subscription"
//            )
//            Toast.makeText(mContext, "call api now ", Toast.LENGTH_SHORT).show()
        } else {
            //  Toast.makeText(mContext, "not successfull", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onPurchaseHistoryRestored() {
        //  Toast.makeText(mContext, "onPurchaseHistoryRestored", Toast.LENGTH_SHORT).show()
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        //    Toast.makeText(mContext, "call api to cancel subscription", Toast.LENGTH_SHORT).show()
    }

    override fun onBillingInitialized() {

        bp.loadOwnedPurchasesFromGoogleAsync(object : BillingProcessor.IPurchasesResponseListener {
            override fun onPurchasesSuccess() {
                //         Toast.makeText(mContext, "onPurchasesSuccess", Toast.LENGTH_SHORT).show()
            }

            override fun onPurchasesError() {

                //         Toast.makeText(mContext, "onPurchasesError", Toast.LENGTH_SHORT).show()
            }

        })

        if (bp.getSubscriptionPurchaseInfo("cenntrumfivedollars") != null) {

            purchaseInfo = bp.getSubscriptionPurchaseInfo("cenntrumfivedollars")!!
            Log.v(
                "TAG6",
                "purchase Info 5 :time :${purchaseInfo.purchaseData.purchaseTime}   status :${purchaseInfo.purchaseData.purchaseState}"
            )

            if (purchaseInfo != null) {
                if (purchaseInfo.purchaseData.autoRenewing) {
                    //      Toast.makeText(mContext, "5 already subscribed", Toast.LENGTH_SHORT).show()
                } else {
                    amount = "4.99"
                    val date = Date(System.currentTimeMillis())
//                    viewModel.addSubscription(
//                        MyApp.data?.id.toString(),
//                        amount,
//                        date.toString(),
//                        "Unsubscribed"
//                    )
                    //     Toast.makeText(mContext, "5 Not subscribed", Toast.LENGTH_SHORT).show()
                }

            } else {
                //     Toast.makeText(mContext, "5 Expired!", Toast.LENGTH_SHORT).show()
            }
        } else {

            // check whether the user already subscribe this subscription from db
            // if subscribe then update db with cancel subscription status

//            Toast.makeText(mContext, "5 Not subscribed", Toast.LENGTH_SHORT).show()
//             if(SavedPreference.getUserSubscriptions(mContext).contains("4.99")){
//
//             }

        }



        if (bp.getSubscriptionPurchaseInfo("cenntrumtendollars") != null) {

            purchaseInfo = bp.getSubscriptionPurchaseInfo("cenntrumtendollars")!!
            Log.v(
                "TAG6",
                "purchase Info 10 :time :${purchaseInfo.purchaseData.purchaseTime}   status :${purchaseInfo.purchaseData.purchaseState}"
            )

            if (purchaseInfo != null) {
                if (purchaseInfo.purchaseData.autoRenewing) {
                    //  Toast.makeText(mContext, "10 already subscribed", Toast.LENGTH_SHORT).show()
                } else {
                    amount = "9.99"
                    val date = Date(System.currentTimeMillis())
//                    viewModel.addSubscription(
//                        SavedPreference.getUserData(this)?.id,
//                        amount,
//                        date.toString(),
//                        "Un Subscribe"
//                    )
                    //     Toast.makeText(mContext, "10 Not subscribed", Toast.LENGTH_SHORT).show()
                }

            } else {
                //   Toast.makeText(mContext, "10 Expired!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // check whether the user already subscribe this subscription from db
            // if subscribe then update db with cancel subscription status

            //     Toast.makeText(mContext, "10 Not subscribed", Toast.LENGTH_SHORT).show()


        }


    }

    lateinit var countDownTimer: Timer
    lateinit var timerRunnable: Runnable
    val timerHandler: Handler = Handler()
  //  var tvTimer: TextView? = null
    var tvTimer2: TextView? = null
    var isTimerRunning = false
    var time: String = "00:00:00"

    var selectedOption: Int = 1
    fun startCountDownTimer() {
        val userData = SavedPreference.getUserData(this)
        val pointsTime = userData?.pointTime!!.toLong() * 1000
        countDownTimer = Timer()
        countDownTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post(Runnable {
                    //   showToast(mContext, getString(R.string.points_added))

                    viewModel.addPoints(
                        endPoint = "add_point",
                        Points(
                            userId = userData?.id,
                            type = selectedOption,
                            startTime = GlobalData.pointsStartTime,
                            endTime = System.currentTimeMillis().toString(),
                            points = userData?.earningPoint
                        )
                    )
                    GlobalData.pointsStartTime = System.currentTimeMillis().toString()
                })
            }
        }, pointsTime!!, pointsTime!!)
    }

    fun timerStart(currentTimeMillis: Long) {

        GlobalData.pointsStartTime = currentTimeMillis.toString()
        timerRunnable = object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - currentTimeMillis
                time = TimerUtils.timeFormater(elapsedTime.toFloat())
                Log.v("TAG222", "time :${time}")

                timerHandler.postDelayed(this, 1000)
                val secs = (elapsedTime / 1000)

            }
        }
        //   if (recordTimerHandler == null) recordTimerHandler = Handler()
        timerHandler.post(timerRunnable)
    }

    fun timerStop() {
        timerHandler.removeCallbacks(timerRunnable)
    }

    private var isCancel = false

    override fun onPause() {
        Log.v("TAG2", "onPause Main Activity")

        if (isTimerRunning && !myKM!!.inKeyguardRestrictedInputMode()  && !MapUtils.isMapRunning) {
            isCancel = true
            countDownTimer.cancel()
            timerStop()
        }

        super.onPause()
    }

    override fun onResume() {
        Log.v("TAG2", "onResume Main Activity")
        if (isTimerRunning && isCancel) {
            isCancel = false
            startCountDownTimer()
        timerStart(System.currentTimeMillis())
        }
        super.onPause()
    }


    override fun onDestroy() {
        Log.v("TAG2", "onDestroy Main Activity")
        if (bp != null) {
            bp.release()
        }
        super.onDestroy()
    }



}