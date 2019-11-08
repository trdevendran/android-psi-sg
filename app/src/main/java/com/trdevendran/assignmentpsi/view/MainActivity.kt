package com.trdevendran.assignmentpsi.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.trdevendran.assignmentpsi.R
import com.trdevendran.assignmentpsi.model.PSIInfoResponse
import com.trdevendran.assignmentpsi.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    /**
     * This callback is triggered when activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets layout for the user interface
        setContentView(R.layout.activity_maps)
        RetrofitBuilder.getInstance().getLatestInfo().enqueue(object : retrofit2.Callback<PSIInfoResponse>{
            override fun onFailure(call: Call<PSIInfoResponse>, t: Throwable) {
                if (t is UnknownHostException)
                    Log.d("CALL", t.message)
            }

            override fun onResponse(
                call: Call<PSIInfoResponse>,
                response: Response<PSIInfoResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("CALL", response.body().toString())
                }
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}
