package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import com.example.data.LocationService
import com.example.model.UserCoordinates
import com.example.ui.DisasterViewModel
import com.example.ui.components.LocationPermissionCard
import com.example.ui.components.isLocationPermissionGranted
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LocationServiceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `isLocationPermissionGranted returns boolean for context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val service = LocationService(context)
        val hasPermission = service.hasLocationPermission()
        assertEquals(isLocationPermissionGranted(context), hasPermission)
    }

    @Test
    fun `UserCoordinates defaults represent valid coordinates for emergency routing`() {
        val coords = UserCoordinates()
        assertTrue("Latitude should be within India bounds", coords.latitude in 8.0..37.0)
        assertTrue("Longitude should be within India bounds", coords.longitude in 68.0..97.0)
        assertNotNull("Address should not be null", coords.address)
        assertTrue("Accuracy must be greater than 0", coords.accuracyMeters > 0f)
    }

    @Test
    fun `DisasterViewModel tracks coordinates and updates status`() {
        val viewModel = DisasterViewModel()
        val initialCoords = viewModel.currentCoordinates.value
        assertNotNull(initialCoords)
        assertEquals(16.5186, initialCoords.latitude, 0.0001)
        assertEquals(80.6195, initialCoords.longitude, 0.0001)

        viewModel.updateLocationPermission(true)
        assertTrue(viewModel.isLocationPermissionGranted.value)

        viewModel.updateLocationPermission(false)
        assertFalse(viewModel.isLocationPermissionGranted.value)
    }

    @Test
    fun `LocationPermissionCard displays grant button when permission is not granted`() {
        val viewModel = DisasterViewModel()
        viewModel.updateLocationPermission(false)

        composeTestRule.setContent {
            LocationPermissionCard(
                viewModel = viewModel,
                onManualLocationClick = {}
            )
        }

        composeTestRule.onNodeWithTag("location_permission_card").assertExists()
        composeTestRule.onNodeWithTag("grant_location_button").assertExists()
    }
}
