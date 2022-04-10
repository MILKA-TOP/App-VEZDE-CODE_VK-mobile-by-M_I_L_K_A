package com.myapplication.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.myapplication.MainApp
import com.myapplication.OrderInformation
import com.myapplication.OrderStatus
import com.myapplication.databinding.MapOrdersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyMapFragment : Fragment() {

    private lateinit var binding: MapOrdersBinding
    private var scope = CoroutineScope(Dispatchers.Default)


    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapOrdersBinding.inflate(inflater, container, false)
        mMapView = binding.mapView
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        scope.launch {
            val waitingOrder =
                MainApp.instance.dataBase.orderDao()?.getInProgressOrder(OrderStatus.IN_PROGRESS)
            withContext(Dispatchers.Main) {
                if (waitingOrder != null) {
                    showShop(waitingOrder)
                }

            }
        }
    }

    private fun showShop(waitingOrder: OrderInformation) {
        mMapView!!.getMapAsync { mMap ->
            googleMap = mMap

            val nowShop = LatLng(waitingOrder.x, waitingOrder.y)
            googleMap!!.addMarker(
                MarkerOptions().position(nowShop).title(waitingOrder.shopName)
                    .snippet(waitingOrder.address)
            )


            val builder = LatLngBounds.Builder()
            builder.include(nowShop)


            if (checkPermission()) {
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0))
                return@getMapAsync
            }
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this@MyMapFragment.requireActivity())
            googleMap!!.isMyLocationEnabled = true


            fusedLocationClient.lastLocation
                .addOnSuccessListener(
                    this@MyMapFragment.requireActivity(),
                    OnSuccessListener<Location?> { location ->
                        if (location == null) return@OnSuccessListener
                        val yourLocation = LatLng(location.latitude, location.longitude)

                        googleMap!!.addMarker(
                            MarkerOptions().position(yourLocation)
                                .title("Ваши координаты")
                        )
                        builder.include(yourLocation)
                        googleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                builder.build(),
                                100
                            )
                        )

                    })

        }

    }



    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this@MyMapFragment.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MyMapFragment.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

}