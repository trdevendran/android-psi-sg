package com.trdevendran.assignmentpsi.network

import com.trdevendran.assignmentpsi.model.PSIInfoResponse
import retrofit2.Call
import retrofit2.http.*


interface EndPointService {

    /**
     * Fetches the recent information from the PSI
     */
    @GET("environment/psi")
    fun getLatestInfo(): Call<PSIInfoResponse>

    /**
     * Fetches the date and time based information from the PSI
     */
    @GET("environment/psi")
    fun getInfoByDate(@Query("psi?date_time") dateTime: String, @Query("date") date: String): Call<PSIInfoResponse>
}