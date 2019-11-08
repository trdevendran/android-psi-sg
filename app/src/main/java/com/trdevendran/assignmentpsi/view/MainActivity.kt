package com.trdevendran.assignmentpsi.view

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.trdevendran.assignmentpsi.R
import com.trdevendran.assignmentpsi.model.PSIInfoResponse
import com.trdevendran.assignmentpsi.util.CommonSnippets
import com.trdevendran.assignmentpsi.util.Constants
import com.trdevendran.assignmentpsi.viewmodel.PSIInfoViewModel
import kotlinx.android.synthetic.main.activity_maps.*

/**
 * This class behaves the main page of the application
 * It contains map specified pin points which provided from PSI server data
 */
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mViewModel: PSIInfoViewModel
    private var mMap: GoogleMap? = null

    /**
     * This callback is triggered when activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets layout for the user interface
        setContentView(R.layout.activity_maps)
        (mapFragment as SupportMapFragment).getMapAsync(this)

        mViewModel = ViewModelProviders.of(this).get(PSIInfoViewModel::class.java)
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

        //Checks internet connectivity
        if (CommonSnippets.isNetworkAvailable(this)) {
            fetchInfo()
        } else {
            CommonSnippets.createAlertDialog(
                this,
                getString(R.string.no_internet),
                getString(R.string.please_check_internet),
                getString(R.string.ok),
                DialogInterface.OnClickListener { p0, _ -> p0!!.dismiss() }).show()
        }
    }

    /**
     * It fetches the PSI information from server and displays the points on the map
     * It exactly shows the result when both Map and server data
     */
    private fun fetchInfo() {

        mViewModel.init()

        //Sets observer to capture every changes with aware of lifecycle changes
        mViewModel.getNewsRepository()!!.observe(this,
            Observer<PSIInfoResponse> { t ->
                if (t != null && mMap != null) {
                    val markers = mutableListOf<Marker>()
                    t.regionMetadata.forEach {

                        val options =
                            MarkerOptions().position(
                                LatLng(
                                    it.labelLocation.latitude,
                                    it.labelLocation.longitude
                                )
                            ).title(it.name)

                        //prepare bounds to focus except national i.e, shows singapore region alone
                        if (Constants.APIKeys.NATIONAL != it.name) {
                            markers.add(mMap!!.addMarker(options))
                        } else {
                            mMap!!.addMarker(options)
                        }
                    }
                    val builder = LatLngBounds.Builder()
                    for (marker in markers) {
                        builder.include(marker.position)
                    }

                    // Calculates percentage of the screen's width & height set the boundaries
                    val width = resources.displayMetrics.widthPixels
                    val height = resources.displayMetrics.heightPixels
                    val padding = (width * 0.10).toInt() // offset from edges of

                    //prepares data to focus the bounds on the map
                    val cu =
                        CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, padding)

                    //animates map
                    mMap!!.animateCamera(cu)
                }
            })
    }
}