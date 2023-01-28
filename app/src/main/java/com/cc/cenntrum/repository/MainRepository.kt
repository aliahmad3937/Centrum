package com.cc.cenntrum.repository

import com.cc.cenntrum.models.*
import com.cc.cenntrum.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepository @Inject constructor(private val retrofitService: ApiService) {
//
//


    //
//
//    fun getEquipmentsDetail()  = retrofitService.getEquipmentsDetail()
//
//
//
//    suspend fun getEquipmentsDetail2() {
//        //    recomendedEquipment.postValue(APIResponse.Loading)
//       CoroutineScope(Dispatchers.IO ).launch {
//            val response = retrofitService.getEquipmentsDetail2()
//            withContext(Dispatchers.IO) {
//                if (response.isSuccessful) {
//                    MyApp.equipmentList = response.body()!!.data
//                } else {
//                    MyApp.equipmentList = null
//                }
//            }
//        }
//    }
//
//
//    fun getClassDetail()  = retrofitService.getClassDetail()
//
//
//    suspend fun getRecomendedEquipments(id:Int)  = retrofitService.getRecomendedEquipments(id)
//
//
//
//    suspend fun reviewClass(id:Int,classId:Int,review:String,difficulty:Int,instructor:Int)  = retrofitService.reviewClass(id,classId,review,difficulty,instructor)
//
//
//
    suspend fun createUser(user: UserRequest) = retrofitService.createUser(user)
    suspend fun loginUser(user: User) = retrofitService.loginUser(user)
    suspend fun getUserDetail(id: Int) = retrofitService.getUserDetail(id)
    suspend fun getFriendList(id: Int) = retrofitService.getFriendList(id)
    suspend fun getOtherPeopleList(id: Int) = retrofitService.getOtherPeopleList(id)
    suspend fun getFriendRequestList(id: Int) = retrofitService.getFriendRequestList(id)
    suspend fun getRewardList(id: Int) = retrofitService.getRewardList(id)
    suspend fun getMyFocusList(id: IncentiveRequest) = retrofitService.getMyFocusList(id)
    suspend fun sendFriendRequest(friend: FriendRequest) = retrofitService.sendFriendRequest(friend)
    suspend fun acceptFriendRequest(friend: FriendRequest) =
        retrofitService.acceptFriendRequest(friend)

    suspend fun rejectFriendRequest(friend: FriendRequest) =
        retrofitService.rejectFriendRequest(friend)

    suspend fun addPoints(endPoint: String, points: Points) =
        retrofitService.addPoints(fullUrl = endPoint, points = points)

    suspend fun updateProfile(
        userID: RequestBody,
        firstname: RequestBody,
        lastname: RequestBody,
        dob: RequestBody,
        gender: RequestBody,
        phone: RequestBody,
        country: RequestBody,
        pic: MultipartBody.Part?
    ) = retrofitService.updateProfile(userID, firstname, lastname, dob, gender, phone, country, pic)

    suspend fun updateProfile(user: User) = retrofitService.updateProfile(user)
    suspend fun getIncentiveList(incentiveRequest: IncentiveRequest) =
        retrofitService.getIncentiveList(incentiveRequest)

    suspend fun getAccumulatedPointsList(incentiveRequest: IncentiveRequest) =
        retrofitService.getAccumulatedPointsList(incentiveRequest)

    suspend fun getFilteredPoints(filterPointsRequest: FilterRequest) =
        retrofitService.getFilteredPoints(filterPointsRequest)

    suspend fun getFilteredFocus(filterPointsRequest: FilterRequest) =
        retrofitService.getFilteredFocus(filterPointsRequest)

    suspend fun buyIncentive(buyIncentiveRequest: BuyIncentiveRequest) =
        retrofitService.buyIncentive(buyIncentiveRequest)

    suspend fun addCryptoAddress(walletAddressRequest: WalletAddressRequest) =
        retrofitService.addCryptoAddress(walletAddressRequest)

    suspend fun getCoinsList() = retrofitService.getCoinsList()
    suspend fun getSubscriptionList() = retrofitService.getSubscriptionList()


    suspend fun addPurchaseSubscription(purchaseRequest: PurchaseRequest) =
        retrofitService.addPurchaseSubscription(purchaseRequest)

    suspend fun addPurchasePoints(purchaseRequest: PurchaseRequest) =
        retrofitService.addPurchasePoints(purchaseRequest)

    suspend fun senForgotPasswordEmail(email: String)  = retrofitService.senForgotPasswordEmail(email)
   suspend fun resetPassword(passwordResetRequest: PasswordResetRequest)  = retrofitService.resetPassword(passwordResetRequest)


}