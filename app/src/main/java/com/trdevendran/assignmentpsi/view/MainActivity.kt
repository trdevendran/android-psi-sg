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
import org.jetbrains.annotations.TestOnly

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

            mViewModel.getNewsRepository()!!.value?.let {
                getReadingInfo(
                    name,
                    it,
                    getString(R.string.readings_info)
                )
            }?.let {
                CommonSnippets.createAlertDialogWithView(
                    this@MainActivity,
                    String.format(getString(R.string.readings_title), name),
                    it,
                    getString(R.string.ok),
                    DialogInterface.OnClickListener { p0, _ -> p0!!.dismiss() }, null
                ).show()
            }

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

    /**
     * @param name the name of the region
     *
     * @param info the information about PSI to fetch the readings
     *
     * @param baseContent the static string content to populate the reading information on view
     *
     * @return a string which will summarise all the readings of the region based on the matches of [name]
     */
    fun getReadingInfo(name: String, info: PSIInfoResponse, baseContent: String): String {

        if (name == Constants.APIKeys.EAST
            || name == Constants.APIKeys.WEST
            || name == Constants.APIKeys.NORTH
            || name == Constants.APIKeys.SOUTH
            || name == Constants.APIKeys.CENTRAL
            || name == Constants.APIKeys.NATIONAL
        ) {
            info.items.forEach {
                when (name) {
                    Constants.APIKeys.EAST -> {
                        return String.format(
                            baseContent,
                            it.readings.o3SubIndex.east.toString(),
                            it.readings.pm10TwentyFourHourly.east.toString(),
                            it.readings.pm10SubIndex.east.toString(),
                            it.readings.coSubIndex.east.toString(),
                            it.readings.pm25TwentyFourHourly.east.toString(),
                            it.readings.so2SubIndex.east.toString(),
                            it.readings.coEightHourMax.east.toString(),
                            it.readings.no2OneHourMax.east.toString(),
                            it.readings.so2TwentyFourHourly.east.toString(),
                            it.readings.pm25SubIndex.east.toString(),
                            it.readings.psiTwentyFourHourly.east.toString(),
                            it.readings.o3EightHourMax.east.toString()
                        )
                    }
                    Constants.APIKeys.WEST -> {
                        return String.format(
                            baseContent,
                            it.readings.o3SubIndex.west.toString(),
                            it.readings.pm10TwentyFourHourly.west.toString(),
                            it.readings.pm10SubIndex.west.toString(),
                            it.readings.coSubIndex.west.toString(),
                            it.readings.pm25TwentyFourHourly.west.toString(),
                            it.readings.so2SubIndex.west.toString(),
                            it.readings.coEightHourMax.west.toString(),
                            it.readings.no2OneHourMax.west.toString(),
                            it.readings.so2TwentyFourHourly.west.toString(),
                            it.readings.pm25SubIndex.west.toString(),
                            it.readings.psiTwentyFourHourly.west.toString(),
                            it.readings.o3EightHourMax.west.toString()
                        )
                    }
                    Constants.APIKeys.NORTH -> {
                        return String.format(
                            baseContent,
                            it.readings.o3SubIndex.north.toString(),
                            it.readings.pm10TwentyFourHourly.north.toString(),
                            it.readings.pm10SubIndex.north.toString(),
                            it.readings.coSubIndex.north.toString(),
                            it.readings.pm25TwentyFourHourly.north.toString(),
                            it.readings.so2SubIndex.north.toString(),
                            it.readings.coEightHourMax.north.toString(),
                            it.readings.no2OneHourMax.north.toString(),
                            it.readings.so2TwentyFourHourly.north.toString(),
                            it.readings.pm25SubIndex.north.toString(),
                            it.readings.psiTwentyFourHourly.north.toString(),
                            it.readings.o3EightHourMax.north.toString()
                        )
                    }
                    Constants.APIKeys.SOUTH -> {
                        return String.format(
                            baseContent,
                            it.readings.o3SubIndex.south.toString(),
                            it.readings.pm10TwentyFourHourly.south.toString(),
                            it.readings.pm10SubIndex.south.toString(),
                            it.readings.coSubIndex.south.toString(),
                            it.readings.pm25TwentyFourHourly.south.toString(),
                            it.readings.so2SubIndex.south.toString(),
                            it.readings.coEightHourMax.south.toString(),
                            it.readings.no2OneHourMax.south.toString(),
                            it.readings.so2TwentyFourHourly.south.toString(),
                            it.readings.pm25SubIndex.south.toString(),
                            it.readings.psiTwentyFourHourly.south.toString(),
                            it.readings.o3EightHourMax.south.toString()
                        )
                    }
                    Constants.APIKeys.CENTRAL -> {
                        return String.format(
                            baseContent,
                            it.readings.o3SubIndex.central.toString(),
                            it.readings.pm10TwentyFourHourly.central.toString(),
                            it.readings.pm10SubIndex.central.toString(),
                            it.readings.coSubIndex.central.toString(),
                            it.readings.pm25TwentyFourHourly.central.toString(),
                            it.readings.so2SubIndex.central.toString(),
                            it.readings.coEightHourMax.central.toString(),
                            it.readings.no2OneHourMax.central.toString(),
                            it.readings.so2TwentyFourHourly.central.toString(),
                            it.readings.pm25SubIndex.central.toString(),
                            it.readings.psiTwentyFourHourly.central.toString(),
                            it.readings.o3EightHourMax.central.toString()
                        )
                    }
                    Constants.APIKeys.NATIONAL -> {
                        return String.format(
                            baseContent,
                            it.readings.o3SubIndex.national.toString(),
                            it.readings.pm10TwentyFourHourly.national.toString(),
                            it.readings.pm10SubIndex.national.toString(),
                            it.readings.coSubIndex.national.toString(),
                            it.readings.pm25TwentyFourHourly.national.toString(),
                            it.readings.so2SubIndex.national.toString(),
                            it.readings.coEightHourMax.national.toString(),
                            it.readings.no2OneHourMax.national.toString(),
                            it.readings.so2TwentyFourHourly.national.toString(),
                            it.readings.pm25SubIndex.national.toString(),
                            it.readings.psiTwentyFourHourly.national.toString(),
                            it.readings.o3EightHourMax.national.toString()
                        )
                    }
                }
            }
        }
        return ""
    }

    /**
     * This function only executes when runs test
     *
     * @param viewModel to set object of mocked view model via test class
     */
    @TestOnly
    fun setTestViewModel(viewModel: PSIInfoViewModel) {
        mViewModel = viewModel
    }
}