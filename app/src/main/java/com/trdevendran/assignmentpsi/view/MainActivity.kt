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
import com.trdevendran.assignmentpsi.model.RegionMetaDatum
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
    private var mMarkerMap: HashMap<String, RegionMetaDatum> = HashMap()

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

        mMap!!.setOnMarkerClickListener { marker ->

            val name = mMarkerMap[marker.id]!!.name

            CommonSnippets.createAlertDialogWithView(
                this@MainActivity,
                String.format(getString(R.string.readings_title), name),
                getReadingInfo(name),
                getString(R.string.ok),
                DialogInterface.OnClickListener { p0, _ -> p0!!.dismiss() }, null
            ).show()

            // Returns as false to utilise the tool tip
            false
        }

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

                        //Keeps the region data with attached marker to show on later
                        val marker = mMap!!.addMarker(options)
                        mMarkerMap[marker.id] = it

                        //prepare bounds to focus except national i.e, shows singapore region alone
                        if (Constants.APIKeys.NATIONAL != it.name) {
                            markers.add(marker)
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

    private fun getReadingInfo(name: String): String {

        val info = mViewModel.getNewsRepository()!!

        var o3SubIndex = ""
        var pm10TwentyFourHourly = ""
        var pm10SubIndex = ""
        var coSubIndex = ""
        var pm25TwentyFourHourly = ""
        var so2SubIndex = ""
        var coEightHourMax = ""
        var no2OneHourMax = ""
        var so2TwentyFourHourly = ""
        var pm25SubIndex = ""
        var psiTwentyFourHourly = ""
        var o3EightHourMax = ""
        info.value!!.items.forEach {
            when (name) {
                "east" -> {
                    o3SubIndex = it.readings.o3SubIndex.east.toString()
                    pm10TwentyFourHourly = it.readings.pm10TwentyFourHourly.east.toString()
                    pm10SubIndex = it.readings.pm10SubIndex.east.toString()
                    coSubIndex = it.readings.coSubIndex.east.toString()
                    pm25TwentyFourHourly = it.readings.pm25TwentyFourHourly.east.toString()
                    so2SubIndex = it.readings.so2SubIndex.east.toString()
                    coEightHourMax = it.readings.coEightHourMax.east.toString()
                    no2OneHourMax = it.readings.no2OneHourMax.east.toString()
                    so2TwentyFourHourly = it.readings.so2TwentyFourHourly.east.toString()
                    pm25SubIndex = it.readings.pm25SubIndex.east.toString()
                    psiTwentyFourHourly = it.readings.psiTwentyFourHourly.east.toString()
                    o3EightHourMax = it.readings.o3EightHourMax.east.toString()
                }
            "west" -> {
                o3SubIndex = it.readings.o3SubIndex.west.toString()
                pm10TwentyFourHourly = it.readings.pm10TwentyFourHourly.west.toString()
                pm10SubIndex = it.readings.pm10SubIndex.west.toString()
                coSubIndex = it.readings.coSubIndex.west.toString()
                pm25TwentyFourHourly = it.readings.pm25TwentyFourHourly.west.toString()
                so2SubIndex = it.readings.so2SubIndex.west.toString()
                coEightHourMax = it.readings.coEightHourMax.west.toString()
                no2OneHourMax = it.readings.no2OneHourMax.west.toString()
                so2TwentyFourHourly = it.readings.so2TwentyFourHourly.west.toString()
                pm25SubIndex = it.readings.pm25SubIndex.west.toString()
                psiTwentyFourHourly = it.readings.psiTwentyFourHourly.west.toString()
                o3EightHourMax = it.readings.o3EightHourMax.west.toString()
            }
                "north" -> {
                    o3SubIndex = it.readings.o3SubIndex.north.toString()
                    pm10TwentyFourHourly = it.readings.pm10TwentyFourHourly.north.toString()
                    pm10SubIndex = it.readings.pm10SubIndex.north.toString()
                    coSubIndex = it.readings.coSubIndex.north.toString()
                    pm25TwentyFourHourly = it.readings.pm25TwentyFourHourly.north.toString()
                    so2SubIndex = it.readings.so2SubIndex.north.toString()
                    coEightHourMax = it.readings.coEightHourMax.north.toString()
                    no2OneHourMax = it.readings.no2OneHourMax.north.toString()
                    so2TwentyFourHourly = it.readings.so2TwentyFourHourly.north.toString()
                    pm25SubIndex = it.readings.pm25SubIndex.north.toString()
                    psiTwentyFourHourly = it.readings.psiTwentyFourHourly.north.toString()
                    o3EightHourMax = it.readings.o3EightHourMax.north.toString()
                }
                "south" -> {
                    o3SubIndex = it.readings.o3SubIndex.south.toString()
                    pm10TwentyFourHourly = it.readings.pm10TwentyFourHourly.south.toString()
                    pm10SubIndex = it.readings.pm10SubIndex.south.toString()
                    coSubIndex = it.readings.coSubIndex.south.toString()
                    pm25TwentyFourHourly = it.readings.pm25TwentyFourHourly.south.toString()
                    so2SubIndex = it.readings.so2SubIndex.south.toString()
                    coEightHourMax = it.readings.coEightHourMax.south.toString()
                    no2OneHourMax = it.readings.no2OneHourMax.south.toString()
                    so2TwentyFourHourly = it.readings.so2TwentyFourHourly.south.toString()
                    pm25SubIndex = it.readings.pm25SubIndex.south.toString()
                    psiTwentyFourHourly = it.readings.psiTwentyFourHourly.south.toString()
                    o3EightHourMax = it.readings.o3EightHourMax.south.toString()
                }
                "central" -> {
                    o3SubIndex = it.readings.o3SubIndex.central.toString()
                    pm10TwentyFourHourly = it.readings.pm10TwentyFourHourly.central.toString()
                    pm10SubIndex = it.readings.pm10SubIndex.central.toString()
                    coSubIndex = it.readings.coSubIndex.central.toString()
                    pm25TwentyFourHourly = it.readings.pm25TwentyFourHourly.central.toString()
                    so2SubIndex = it.readings.so2SubIndex.central.toString()
                    coEightHourMax = it.readings.coEightHourMax.central.toString()
                    no2OneHourMax = it.readings.no2OneHourMax.central.toString()
                    so2TwentyFourHourly = it.readings.so2TwentyFourHourly.central.toString()
                    pm25SubIndex = it.readings.pm25SubIndex.central.toString()
                    psiTwentyFourHourly = it.readings.psiTwentyFourHourly.central.toString()
                    o3EightHourMax = it.readings.o3EightHourMax.central.toString()
                }
                "national" -> {
                    o3SubIndex = it.readings.o3SubIndex.national.toString()
                    pm10TwentyFourHourly = it.readings.pm10TwentyFourHourly.national.toString()
                    pm10SubIndex = it.readings.pm10SubIndex.national.toString()
                    coSubIndex = it.readings.coSubIndex.national.toString()
                    pm25TwentyFourHourly = it.readings.pm25TwentyFourHourly.national.toString()
                    so2SubIndex = it.readings.so2SubIndex.national.toString()
                    coEightHourMax = it.readings.coEightHourMax.national.toString()
                    no2OneHourMax = it.readings.no2OneHourMax.national.toString()
                    so2TwentyFourHourly = it.readings.so2TwentyFourHourly.national.toString()
                    pm25SubIndex = it.readings.pm25SubIndex.national.toString()
                    psiTwentyFourHourly = it.readings.psiTwentyFourHourly.national.toString()
                    o3EightHourMax = it.readings.o3EightHourMax.national.toString()
                }
            }
        }
        return String.format(
            getString(
                R.string.readings_info,
                o3SubIndex,
                pm10TwentyFourHourly,
                pm10SubIndex,
                coSubIndex,
                pm25TwentyFourHourly,
                so2SubIndex,
                coEightHourMax,
                no2OneHourMax,
                so2TwentyFourHourly,
                pm25SubIndex,
                psiTwentyFourHourly,
                o3EightHourMax
            )
        )
    }
}