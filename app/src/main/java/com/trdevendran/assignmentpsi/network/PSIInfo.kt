package com.trdevendran.assignmentpsi.network

import androidx.lifecycle.MutableLiveData
import com.trdevendran.assignmentpsi.model.PSIInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class covers all the data transformations between the server and client side for data of PSI
 */
class PSIInfo{

    private var endPointService: EndPointService = RetrofitBuilder.getInstance()

    fun getLatestInfo(): MutableLiveData<PSIInfoResponse> {
        val psiData = MutableLiveData<PSIInfoResponse>()
        endPointService.getLatestInfo().enqueue(object : Callback<PSIInfoResponse> {

            override fun onFailure(call: Call<PSIInfoResponse>, t: Throwable) {
                psiData.value = null
            }

            override fun onResponse(
                call: Call<PSIInfoResponse>,
                response: Response<PSIInfoResponse>
            ) {
                if (response.isSuccessful) {
                    psiData.value = response.body()
                }
            }
        })
        return psiData
    }

    companion object{

        fun getInstance(): PSIInfo {
            if (INSTANCE == null) {
                INSTANCE = PSIInfo()
            }
            return INSTANCE as PSIInfo
        }

        private var INSTANCE: PSIInfo? = null
    }
}