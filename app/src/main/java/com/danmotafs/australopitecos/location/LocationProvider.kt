package com.danmotafs.australopitecos.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices

object LocationProvider {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onLocationReceived: (String) -> Unit
    ) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude

                    val mapsLink =
                        "https://maps.google.com/?q=$latitude,$longitude"

                    onLocationReceived(mapsLink)

                } else {

                    onLocationReceived("Localização indisponível")

                }
            }
            .addOnFailureListener {

                onLocationReceived("Erro ao obter localização")

            }
    }
}

