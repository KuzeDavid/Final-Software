package mapa

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

import com.proyecto.botndepnico.R
import kotlinx.android.synthetic.main.activity_mapa.*

class Mapa : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val REQUEST_CODE_AUTOCOMPLETE_FROM = 1
        private const val REQUEST_CODE_AUTOCOMPLETE_TO = 2
        private const val TAG = "MAPAS"
    }

    private lateinit var mMap: GoogleMap
    private var mMarkerFrom: Marker? = null
    private var mMarkerTo: Marker? = null
    private lateinit var mFromLatLng: LatLng
    private lateinit var mToLatLng: LatLng

    private lateinit var tvDistance: TextView
    private lateinit var tvTime: TextView

    private var polyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        tvDistance = findViewById(R.id.tvDistance)
        tvTime = findViewById(R.id.tvTime)

        setupMap()
        setupPlaces()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupPlaces() {
        Places.initialize(applicationContext, getString(R.string.apiMapa))

        btnFrom.setOnClickListener {
            starAutocomplete(REQUEST_CODE_AUTOCOMPLETE_FROM)
        }

        btnTo.setOnClickListener {
            starAutocomplete(REQUEST_CODE_AUTOCOMPLETE_TO)
        }

        tvFrom.text = getString(R.string.label_from, getString(R.string.no_place_selected))
        tvTo.text = getString(R.string.label_to, getString(R.string.no_place_selected))
    }

    private fun starAutocomplete(requestCode: Int) {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            processAutocompleteResult(resultCode, data) { place ->
                tvFrom.text = getString(R.string.label_from, place.address)
                place.latLng?.let {
                    mFromLatLng = it
                }
                setMarkerFrom(mFromLatLng)

                if (::mToLatLng.isInitialized) {
                    calculateDistanceAndTime(mFromLatLng, mToLatLng)
                }
            }
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO) {
            processAutocompleteResult(resultCode, data) { place ->
                tvTo.text = getString(R.string.label_to, place.address)
                place.latLng?.let {
                    mToLatLng = it
                }
                setMarkerTo(mToLatLng)

                if (::mFromLatLng.isInitialized) {
                    calculateDistanceAndTime(mFromLatLng, mToLatLng)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processAutocompleteResult(
        resultCode: Int,
        data: Intent?,
        callback: (Place) -> Unit
    ) {
        Log.d(TAG, "processAutocompleteResult(resultCode=$resultCode)")

        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    Log.i(TAG, "Place : $place")
                    callback(place)
                }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    status.statusMessage?.let { message ->
                        Log.i(TAG, message)
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(12f)
        mMap.setMaxZoomPreference(120f)
    }

    private fun addMarker(latLng: LatLng, title: String): Marker {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(title)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        return mMap.addMarker(markerOptions)
    }

    private fun setMarkerFrom(latLng: LatLng) {
        mMarkerFrom?.remove()
        mMarkerFrom = addMarker(latLng, getString(R.string.marker_title_from))
    }

    private fun setMarkerTo(latLng: LatLng) {
        mMarkerTo?.remove()
        mMarkerTo = addMarker(latLng, getString(R.string.marker_title_to))
    }

    private fun calculateDistanceAndTime(fromLatLng: LatLng, toLatLng: LatLng) {
        // Simulating distance and time calculation
        val distance = calculateDistance(fromLatLng, toLatLng)
        val time = calculateTime(distance)

        // Displaying the calculated values
        tvDistance.text = getString(R.string.label_distance, distance)
        tvTime.text = getString(R.string.label_time, time)

        // Draw the route
        val directionsResult = getDirections(fromLatLng, toLatLng)
        drawRoute(directionsResult)

    }

    private fun calculateDistance(fromLatLng: LatLng, toLatLng: LatLng): Double {
        val earthRadius = 6371 // Radio de la Tierra en kilómetros
        val latDistance = Math.toRadians(toLatLng.latitude - fromLatLng.latitude)
        val lngDistance = Math.toRadians(toLatLng.longitude - fromLatLng.longitude)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(fromLatLng.latitude)) * Math.cos(Math.toRadians(toLatLng.latitude)) *
                Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadius * c * 1000 // Distancia en metros
        return distance
    }

    private fun calculateTime(distance: Double): Int {
        val speed = 50 // Velocidad promedio en km/h
        val time = distance / (speed * 1000 / 3600) // Tiempo en segundos
        return time.toInt()
    }

    private fun drawRoute(directionsResult: DirectionsResult) {
        val path = directionsResult.routes[0].overviewPolyline.decodePath()

        val polylineOptions = PolylineOptions()
            .addAll(path.map { LatLng(it.lat, it.lng) })
            .color(Color.RED)
            .width(10f)

        polyline?.remove()
        polyline = mMap.addPolyline(polylineOptions)
    }

    private fun getDirections(fromLatLng: LatLng, toLatLng: LatLng): DirectionsResult {
        val context = GeoApiContext.Builder()
            .apiKey(getString(R.string.apiMapa))
            .build()

        val result = DirectionsApi.newRequest(context)
            .mode(TravelMode.DRIVING)
            .origin(com.google.maps.model.LatLng(fromLatLng.latitude, fromLatLng.longitude))
            .destination(com.google.maps.model.LatLng(toLatLng.latitude, toLatLng.longitude))
            .await()

        return result
    }

}
