package com.cc.cenntrum.models

sealed class APIResponse{
    data class Success<T>(val data: T) : APIResponse()
    data class Error(val message: String) : APIResponse()
    object Loading : APIResponse()
    object Starting : APIResponse()
}

//sealed class Response<T>(var data: T? = null, var errorMsg: String? = null){
//    class Success<T>(data: T?) : Response<T>(data = data)
//    class Error<T> (errorMsg : String, data: T? = null) : Response<T>(data = data , errorMsg = errorMsg)
//}

//sealed class Resource{
//    object Loading : Resource()
//    data class Result<T>(val result: Response<T>) : Resource()
//}
