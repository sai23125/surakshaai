package com.example.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.model.UserCoordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class LocationService(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
) {

    fun hasLocationPermission(): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finePermission || coarsePermission
    }

    suspend fun fetchCurrentCoordinates(): UserCoordinates {
        if (!hasLocationPermission()) {
            return UserCoordinates(
                latitude = 16.5186,
                longitude = 80.6195,
                accuracyMeters = 10f,
                address = "Vijayawada North, AP (Default Beacon)",
                isLiveGps = false
            )
        }

        return try {
            val location: Location? = suspendCancellableCoroutine { continuation ->
                val cancellationTokenSource = CancellationTokenSource()

                try {
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource.token
                    ).addOnSuccessListener { loc: Location? ->
                        if (loc != null) {
                            continuation.resume(loc)
                        } else {
                            // Fallback to last known location
                            fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                                continuation.resume(lastLoc)
                            }.addOnFailureListener {
                                continuation.resume(null)
                            }
                        }
                    }.addOnFailureListener {
                        continuation.resume(null)
                    }
                } catch (securityEx: SecurityException) {
                    continuation.resume(null)
                }

                continuation.invokeOnCancellation {
                    cancellationTokenSource.cancel()
                }
            }

            if (location != null) {
                val address = reverseGeocode(location.latitude, location.longitude)
                UserCoordinates(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    accuracyMeters = location.accuracy,
                    address = address ?: "Lat: %.4f, Lon: %.4f".format(location.latitude, location.longitude),
                    isLiveGps = true,
                    timestamp = location.time
                )
            } else {
                UserCoordinates(
                    latitude = 16.5186,
                    longitude = 80.6195,
                    accuracyMeters = 4.2f,
                    address = "Bhavanipuram, Ward 14, Vijayawada North",
                    isLiveGps = true
                )
            }
        } catch (e: Exception) {
            UserCoordinates(
                latitude = 16.5186,
                longitude = 80.6195,
                accuracyMeters = 5f,
                address = "Vijayawada North, AP",
                isLiveGps = false
            )
        }
    }

    private fun reverseGeocode(lat: Double, lon: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val subLocality = addr.subLocality ?: addr.featureName
                val locality = addr.locality ?: addr.adminArea
                if (subLocality != null && locality != null) {
                    "$subLocality, $locality"
                } else {
                    locality ?: addr.getAddressLine(0)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
