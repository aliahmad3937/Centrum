package com.cc.cenntrum.viewmodels


import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cc.cenntrum.R
import com.cc.cenntrum.models.*
import com.cc.cenntrum.repository.MainRepository
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import gun0912.tedimagepicker.util.ToastUtil
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import javax.inject.Inject


// @HiltViewModel will make models to be
// created using Hilt's model factory
// @Inject annotation used to inject all
// dependencies to view model class
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    @ApplicationContext val application: Context,
)  : ViewModel() {

    val  myKM = application.getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager

     var loginResponse = MutableLiveData<APIResponse>()
     var friendResponse = MutableLiveData<APIResponse>()
     var otherPeopleResponse = MutableLiveData<APIResponse>()
     var friendRequestList = MutableLiveData<APIResponse>()
     var pointsResponse = MutableLiveData<APIResponse>()
     var equipmentResponse = MutableLiveData<APIResponse>()
     var classResponse = MutableLiveData<APIResponse>()
     var recomendedEquipment = MutableLiveData<APIResponse>()
     var reviewResponse = MutableLiveData<APIResponse>()
     var bookingResponse = MutableLiveData<APIResponse>()
     var incentiveResponse = MutableLiveData<APIResponse>()
     var rewardResponse = MutableLiveData<APIResponse>()
     var exchangePointsResponse = MutableLiveData<APIResponse>()
     var accumulatedPointsResponse = MutableLiveData<APIResponse>()
     var myFocusResponse = MutableLiveData<APIResponse>()
     var filterPointsResponse = MutableLiveData<APIResponse>()
     var walletAddressResponse = MutableLiveData<APIResponse>()

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        loginResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        friendResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        otherPeopleResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        friendRequestList.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        pointsResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        equipmentResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        classResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        recomendedEquipment.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        reviewResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        bookingResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        incentiveResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))

        rewardResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        filterPointsResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        myFocusResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        accumulatedPointsResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        exchangePointsResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        walletAddressResponse.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))
        Log.v("TAG9","Exception handled: ${throwable.localizedMessage}")

    }


    init {
        loginResponse.postValue(APIResponse.Starting)
        equipmentResponse.postValue(APIResponse.Starting)
        classResponse.postValue(APIResponse.Starting)
        recomendedEquipment.postValue(APIResponse.Starting)
        reviewResponse.postValue(APIResponse.Starting)
        bookingResponse.postValue(APIResponse.Starting)
        friendResponse.postValue(APIResponse.Starting)
        otherPeopleResponse.postValue(APIResponse.Starting)

    }





    fun createUser(user: UserRequest){
        loginResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.createUser(user)
                if (response.isSuccessful) {
                    loginResponse.postValue(APIResponse.Success(response.body()))
                } else {
                    loginResponse.postValue(APIResponse.Error("Errors : ${response.errorBody().toString()} "))
                }
        }
    }

    fun loginUser(user: User){
        loginResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.loginUser(user)
                if (response.isSuccessful) {
                    loginResponse.postValue(APIResponse.Success(response.body()))
                } else {
                    loginResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                }
        }
    }


    fun addPoints(endPoint:String ,points: Points){
        pointsResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.addPoints(endPoint , points)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if(endPoint == "add_point") {
                        SavedPreference.setUserData(application, response.body()?.data!!)
                        Log.v("TAG222", "points added :")
                        if (myKM!!.inKeyguardRestrictedInputMode()) {

                            val notificationManager =
                                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.sendNotification(
                                mutableMapOf("msg" to "Points Added"),
                                application
                            )
                        }
                        playSound()
                        pointsResponse.postValue(APIResponse.Success(response.body()))
                    }
                } else {
                    pointsResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                }
            }
        }
    }

    private var mediaPlayer: MediaPlayer? = null
    fun playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(application, R.raw.sound)
        }
        mediaPlayer?.start()
    }


    fun getUserDetail(id: Int){
     //   loginResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getUserDetail(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    GlobalData.user = response.body()?.data!!
                    SavedPreference.setUserData(application,response.body()?.data!!)
                    Log.v("TAG8",GlobalData.user.toString())
                  //  loginResponse.postValue(APIResponse.Success(response.body()))
                } else {
                   // loginResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                }
            }
        }
    }

    fun getFriendList(id: Int){
        friendResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getFriendList(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    friendResponse.postValue(APIResponse.Success(response.body()))
                } else {
                    friendResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                }
            }
        }
    }


    fun getOtherPeopleList(id: Int){
        otherPeopleResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getOtherPeopleList(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    otherPeopleResponse.postValue(APIResponse.Success(response.body()))
                } else {
                    otherPeopleResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                }
            }
        }
    }

    fun getFriendRequestList(id: Int){
        friendRequestList.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getFriendRequestList(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    friendRequestList.postValue(APIResponse.Success(response.body()))
                } else {
                    friendRequestList.postValue(APIResponse.Error("Error : ${response.message()} "))
                }
            }
        }
    }

    fun sendFriendRequest(request: FriendRequest, friend: FriendResponse.Data,){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.sendFriendRequest(request)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    sendTokenNotification("${GlobalData.user!!.firstname} ${GlobalData.user!!.lastname} send you Friend request!" , friend.deviceToken!!)

                  Log.v("TAG9","friend request send successfully!")
                } else {
                    Log.v("TAG9","friend request not send")
                }
            }
        }
    }


    fun acceptFriendRequest(friend: FriendRequest, token:String, email:String){
        Log.v("TAG9","acceptFriendRequest :$token")
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.acceptFriendRequest(friend)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.v("TAG9","acceptFriendRequest  successfully :$token")
                    // send notification to user that their friend request accepted successfully!
                    // and on receiving notification from user side on its subscription
                  //  ToastUtils.showToast(application,"Accepted Successfully!")
                    sendTokenNotification("${GlobalData.user!!.firstname} ${GlobalData.user!!.lastname} Accept your Friend Request" ,true, token =  token , email = email)

                } else {
                    Log.v("TAG9","acceptFriendRequest not added")
                }
            }
        }
    }

    fun rejectFriendRequest(friend: FriendRequest, token:String, email:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.rejectFriendRequest(friend)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    Log.v("TAG9","rejectFriendRequest  successfully!")
                    sendTokenNotification("${GlobalData.user!!.firstname} ${GlobalData.user!!.lastname} Reject your Friend Request!" ,false, token = token  , email = email)
                    // ToastUtils.showToast(application,"Rejected Successfully!")
                } else {
                    Log.v("TAG9","rejectFriendRequest not ")
                }
            }
        }
    }


    fun updateProfile(userID: RequestBody , firstname: RequestBody , lastname: RequestBody , dob: RequestBody , gender: RequestBody , phone: RequestBody , country:RequestBody , pic: MultipartBody.Part? ){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.updateProfile(userID, firstname, lastname, dob, gender, phone, country, pic)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    SavedPreference.setUserData(application, data = response.body()?.data!!)
                    Log.v("TAG9","profile updated  successfully!")

                } else {
                    Log.v("TAG9","profile not updated!")
                }
            }
        }
    }
    fun updateProfile(user :User ){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.updateProfile(user)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    SavedPreference.setUserData(application, data = response.body()?.data!!)
                    Log.v("TAG9","profile updated  successfully!")

                } else {
                    Log.v("TAG9","profile not updated!")
                }
            }
        }
    }


    fun sendTokenNotification(msg:String ,token:String) {
        Log.d(
            "TAG2",
            "token : $token"
        )
        val mRequestQue = Volley.newRequestQueue(application)
        val json = JSONObject()
        try {
            json.put("to",  token)
            val notificationObj = JSONObject()
            notificationObj.put("title", "Cenntrum")
            notificationObj.put("body", msg)
            json.put("notification", notificationObj)
            val URL = "https://fcm.googleapis.com/fcm/send"
            val request: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, URL,
                json,
                Response.Listener { response: JSONObject? ->
                    Log.d(
                        "TAG2",
                        "onResponse: "
                    )
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.d(
                        "TAG2",
                        "onError: " + error.networkResponse
                    )
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] =
                        "key=AAAApNC9Y_w:APA91bGXd0vRRFUTikE-HMmTA04gXdmSdEkYPpguT_Blyb5nK_n217LLhWBP0NazeO5I1QLWTRZzrhD40NLANaDaKB8YS1OIXv8aZbvIV1myASFRksoNyFH24AnnqMQCrltA42KJVzj1"
                    return header
                }
            }
            mRequestQue.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun sendTokenNotification(msg:String ,isAccepted:Boolean ,token:String , email:String) {
        Log.d(
            "TAG2",
            "token : $token"
        )
        val mRequestQue = Volley.newRequestQueue(application)
        val json = JSONObject()
        try {
            json.put("to",  token)
            val notificationObj = JSONObject()
            notificationObj.put("msg", msg)
            notificationObj.put("isAccepted", isAccepted)
            notificationObj.put("email", email)
            json.put("data", notificationObj)
            val URL = "https://fcm.googleapis.com/fcm/send"
            val request: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, URL,
                json,
                Response.Listener { response: JSONObject? ->
                    Log.d(
                        "TAG2",
                        "onResponse: "
                    )
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.d(
                        "TAG2",
                        "onError: " + error.networkResponse
                    )
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] =  "key=AAAApNC9Y_w:APA91bGXd0vRRFUTikE-HMmTA04gXdmSdEkYPpguT_Blyb5nK_n217LLhWBP0NazeO5I1QLWTRZzrhD40NLANaDaKB8YS1OIXv8aZbvIV1myASFRksoNyFH24AnnqMQCrltA42KJVzj1"
                        return header
                }
            }
            mRequestQue.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    fun sendDrivingNotification(msg:String) {
        val mRequestQue = Volley.newRequestQueue(application)
        val json = JSONObject()
        try {
            json.put("to", "/topics/" +  SavedPreference.getUserEmail(application).substringBefore("@"))
            val notificationObj = JSONObject()
            notificationObj.put("msg", msg)
            json.put("data", notificationObj)
            val URL = "https://fcm.googleapis.com/fcm/send"
            val request: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, URL,
                json,
                Response.Listener { response: JSONObject? -> },
                Response.ErrorListener { error: VolleyError -> }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] =
                        "key=AAAApNC9Y_w:APA91bGXd0vRRFUTikE-HMmTA04gXdmSdEkYPpguT_Blyb5nK_n217LLhWBP0NazeO5I1QLWTRZzrhD40NLANaDaKB8YS1OIXv8aZbvIV1myASFRksoNyFH24AnnqMQCrltA42KJVzj1"
                    return header
                }
            }
            mRequestQue.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }






    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun getIncentiveList(incentiveRequest: IncentiveRequest) {
        incentiveResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getIncentiveList(incentiveRequest)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    incentiveResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","profile updated  successfully!")
                } else {
                    incentiveResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","profile not updated!")
                }
            }
        }

    }

    fun getRewardList(id: Int ) {
        rewardResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getRewardList(id)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    rewardResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","getRewardList  successfully!")
                } else {
                    rewardResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","getRewardList not successfully!")
                }
            }
        }

    }


    fun getAccumulatedPointsList(incentiveRequest: IncentiveRequest) {
        accumulatedPointsResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getAccumulatedPointsList(incentiveRequest)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    accumulatedPointsResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","profile updated  successfully!")
                } else {
                    accumulatedPointsResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","profile not updated!")
                }
            }
        }

    }

    fun getExchangePointsList(id: Int ) {
        exchangePointsResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getRewardList(id)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    exchangePointsResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","profile updated  successfully!")
                } else {
                    exchangePointsResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","profile not updated!")
                }
            }
        }

    }



    fun getMyFocusList(id: IncentiveRequest) {
        myFocusResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getMyFocusList(id)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    myFocusResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","profile updated  successfully!")
                } else {
                    myFocusResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","profile not updated!")
                }
            }
        }
    }

    fun getFilteredPoints(filterPointsRequest: FilterRequest) {
        filterPointsResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getFilteredPoints(filterPointsRequest)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    filterPointsResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","profile updated  successfully!")
                } else {
                    filterPointsResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","profile not updated!")
                }
            }
        }
    }

    fun getFilteredFocus(filterPointsRequest: FilterRequest) {
        myFocusResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getFilteredFocus(filterPointsRequest)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    myFocusResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","profile updated  successfully!")
                } else {
                    myFocusResponse.postValue(APIResponse.Error("errors :"+response.message()))
                    Log.v("TAG9","profile not updated!")
                }
            }
        }
    }






    fun buyIncentive(buyIncentiveRequest: BuyIncentiveRequest) {
     //   filterPointsResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.buyIncentive(buyIncentiveRequest)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                   // filterPointsResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","buyIncentive  successfully!")
                } else {
                  //  filterPointsResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","buyIncentive not successfully!")
                }
            }
        }
    }

    fun addCryptoAddress(walletAddressRequest: WalletAddressRequest) {
        walletAddressResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.addCryptoAddress(walletAddressRequest)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    walletAddressResponse.postValue(APIResponse.Success(response.body()))
                    Log.v("TAG9","profile updated  successfully!")
                } else {
                      walletAddressResponse.postValue(APIResponse.Error(response.message()))
                    Log.v("TAG9","profile not updated!")
                }
            }
        }
    }

    fun getPackagesList() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            Log.v("TAG65","line 554")
            val coinsList= async {
                repository.getCoinsList()
            }
            Log.v("TAG65","line 558")
            val subscripList = async {
                repository.getSubscriptionList()
            }
            Log.v("TAG65","line 562 : "+subscripList.await().body()?.message)
            MyApp.subscripList = subscripList.await().body()?.data!!
            Log.v("TAG65","line 564")
            MyApp.coinsList = coinsList.await().body()?.data!!
            Log.v("TAG65","line 566")
          //  SavedPreference.savePackagesList()
        }
    }



    fun addSubscription(id: String, amount: String, purchaseTime: String, status: String) {
//        addSubscriptionResponse.postValue(APIResponse.Loading)
//        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            val response = repository.addSubscription(id,amount,purchaseTime,status)
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//                    //  SavedPreference.saveUserSubscriptions(application,amount)
//                    addSubscriptionResponse.postValue(APIResponse.Success(response.body()))
//                } else {
//                    addSubscriptionResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
//                }
//            }
//        }
    }

    fun addPurchaseSubscription(purchaseRequest: PurchaseRequest) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.addPurchaseSubscription(purchaseRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if(response.body()?.status == true) {
                        //     SavedPreference.setUserData(application,response.body()?.data!!)
                        ToastUtil.showToast("Subscribed Successfully!")
                    }else{
                        ToastUtil.showToast("Not Subscribed Successfully!")
                    }
                } else {
                    ToastUtil.showToast("Subscription Fail!")
                }
            }
        }
    }

    fun addPurchasePoints(purchaseRequest: PurchaseRequest , listener:() -> Unit) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.addPurchasePoints(purchaseRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if(response.body()?.status == true) {
                        //     SavedPreference.setUserData(application,response.body()?.data!!)
                        ToastUtil.showToast("Points Purchase Successfully!")
                    }else{
                        ToastUtil.showToast("Points not Purchase!")
                    }
                } else {
                    ToastUtil.showToast("Points Purchase Fail!")
                }
            }
        }
    }

    fun senForgotPasswordEmail(email: String) {
        //    addSubscriptionResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.senForgotPasswordEmail(email)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.v("TAG66","success")
                    //  SavedPreference.saveUserSubscriptions(application,amount)
                    //   addSubscriptionResponse.postValue(APIResponse.Success(response.body()))
                } else {
                    //  addSubscriptionResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                    Log.v("TAG66","fail")
                }
            }
        }
    }

    fun resetPassword(passwordResetRequest: PasswordResetRequest) {
        ToastUtil.showToast("Password Reset Successfully!")
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.resetPassword(passwordResetRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.v("TAG66","success")

                } else {
                    //  addSubscriptionResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                    Log.v("TAG66","fail")
                }
            }
        }
    }


}
