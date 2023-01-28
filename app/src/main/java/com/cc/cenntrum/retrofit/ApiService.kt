package com.cc.cenntrum.retrofit


import com.cc.cenntrum.models.*
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>
//
//
//

    @POST("register")
    suspend fun createUser(@Body user: UserRequest): Response<UserResponse>


    @POST("login")
    suspend fun loginUser(@Body user: User): Response<UserResponse>

    @GET("user_detail")
    suspend fun getUserDetail(@Query("user_id") user_id: Int): Response<UserResponse>

    @GET("friend_list")
    suspend fun getFriendList(@Query("user_id") user_id: Int): Response<FriendResponse>


    @GET("users_list")
    suspend fun getOtherPeopleList(@Query("user_id") user_id: Int): Response<FriendResponse>

    @GET("friend_req_list")
    suspend fun getFriendRequestList(@Query("user_id") user_id: Int): Response<FriendResponse>

    @GET("my_rewards")
    suspend fun getRewardList(@Query("user_id") user_id: Int): Response<RewardResponse>

    @POST("my_focus")
    suspend fun getMyFocusList(@Body user_id: IncentiveRequest): Response<MyFocusResponse>


    @POST("{fullUrl}")
    suspend fun addPoints(
        @Path("fullUrl") fullUrl: String,
        @Body points: Points
    ): Response<UserResponse>

    @POST("add_friend")
    suspend fun sendFriendRequest(@Body friend: FriendRequest): Response<FriendResponse>

    @POST("accept_friend")
    suspend fun acceptFriendRequest(@Body friend: FriendRequest): Response<AcceptFriendRequestResponse>


    @POST("rem_friend")
    suspend fun rejectFriendRequest(@Body friend: FriendRequest): Response<RemFriendRequestResponse>

    @Multipart
    @POST("update_profile")
    suspend fun updateProfile(
        @Part("user_id") userID: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("country") country: RequestBody,
        @Part pic: MultipartBody.Part?
    ): Response<UserResponse>

    @POST("update_profile")
    suspend fun updateProfile(@Body user: User): Response<UserResponse>


    @POST("incentive_list")
    suspend fun getIncentiveList(@Body incentiveRequest: IncentiveRequest): Response<IncentiveResponse>

    @POST("my_points")
    suspend fun getAccumulatedPointsList(@Body incentiveRequest: IncentiveRequest): Response<PointsResponse>

    @POST("my_points")
    suspend fun getFilteredPoints(@Body filterPointsRequest: FilterRequest): Response<FilterPointsResponse>

    @POST("my_focus")
    suspend fun getFilteredFocus(@Body filterPointsRequest: FilterRequest): Response<MyFocusResponse>

    @POST("buy_incentive")
    suspend fun buyIncentive(@Body buyIncentiveRequest: BuyIncentiveRequest): Response<IncentiveResponse>

    @POST("add_wallet")
    suspend fun addCryptoAddress(@Body walletAddressRequest: WalletAddressRequest): Response<WalletResponse>


    @GET("coins_list")
    suspend fun getCoinsList(): Response<PackagesResponse>


    @GET("subs_list")
    suspend fun getSubscriptionList(): Response<PackagesResponse>

    @POST("buy_subscription")
    suspend fun addPurchaseSubscription(@Body purchaseRequest: PurchaseRequest): Response<UserResponse>


    @POST("buy_coins")
    suspend fun addPurchasePoints(@Body purchaseRequest: PurchaseRequest): Response<UserResponse>

    @POST("forgot_password")
    suspend fun senForgotPasswordEmail(
        @Query("email") email: String,
    ): Response<UserResponse>

    @POST("reset_password")
    suspend fun resetPassword(@Body passwordResetRequest: PasswordResetRequest): Response<UserResponse>

//
//
//    @FormUrlEncoded
//    @POST("booking_details")
//    suspend fun bookSession(
//        @Field("user_id") id: Int,
//        @Field("class_id") classId: Int,
//        @Field("user_name") name: String,
//        @Field("user_email") email: String,
//        @Field("phone") phn: String,
//        @Field("class_type") type: String,
//    ): Response<BookingResponse>
//
//
//    @GET("eqp_details")
//    fun getEquipmentsDetail(): Call<EquipmentResponse>
//
//    @GET("eqp_details")
//    suspend fun getEquipmentsDetail2(): Response<EquipmentResponse>
//
//    @GET("class_details")
//    fun getClassDetail(): Call<EquipmentResponse>
//
//    @GET("recomended_video")
//    suspend fun getRecomendedEquipments(
//        @Query("eqp_id") eqp_id:Int
//    ): Response<EquipmentResponse>




}