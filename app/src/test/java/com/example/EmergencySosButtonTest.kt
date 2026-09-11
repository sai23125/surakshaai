package com.example

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.ui.components.EmergencySosButton
import com.example.ui.components.SosButtonVariant
import com.example.ui.components.buildEmergencyDistressMessage
import com.example.ui.components.sendEmergencySosDistress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class EmergencySosButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `buildEmergencyDistressMessage contains location coordinates and maps link`() {
        val location = "Bhavanipuram, Ward 14, Vijayawada North"
        val lat = 16.5186
        val lon = 80.6195
        val message = buildEmergencyDistressMessage(location, lat, lon, "Flood water rising fast")

        assertTrue("Message must contain SOS header", message.contains("EMERGENCY SOS DISTRESS ALERT"))
        assertTrue("Message must contain user location", message.contains(location))
        assertTrue("Message must contain latitude", message.contains("16.5186"))
        assertTrue("Message must contain longitude", message.contains("80.6195"))
        assertTrue("Message must contain Google maps link", message.contains("https://maps.google.com/?q=16.5186,80.6195"))
        assertTrue("Message must contain custom ground note", message.contains("Flood water rising fast"))
    }

    @Test
    fun `sendEmergencySosDistress triggers intent with distress message to contacts`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val app = context as Application
        val contacts = listOf("112", "1070", "9711077372")
        val location = "Bhavanipuram, Ward 14"

        sendEmergencySosDistress(
            context = context,
            contacts = contacts,
            location = location,
            latitude = 16.5186,
            longitude = 80.6195
        )

        val nextIntent = shadowOf(app).nextStartedActivity
        assertNotNull("An intent must be started when sending emergency SOS", nextIntent)
        assertTrue("Intent action should be ACTION_SENDTO or ACTION_SEND or ACTION_CHOOSER",
            nextIntent.action == Intent.ACTION_SENDTO ||
            nextIntent.action == Intent.ACTION_SEND ||
            nextIntent.action == Intent.ACTION_CHOOSER
        )

        val body = nextIntent.getStringExtra("sms_body") ?: nextIntent.getStringExtra(Intent.EXTRA_TEXT)
        assertNotNull("Intent must carry distress message body", body)
        assertTrue("Intent body must mention location", body!!.contains(location))
    }

    @Test
    fun `EmergencySosButton renders and invokes intent on click`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val app = context as Application
        var clicked = false

        composeTestRule.setContent {
            EmergencySosButton(
                currentLocation = "Vijayawada North",
                contacts = listOf("112"),
                variant = SosButtonVariant.PROMINENT,
                onPressed = { clicked = true }
            )
        }

        // Verify button node exists and perform click
        composeTestRule.onNodeWithTag("emergency_sos_button").assertExists()
        composeTestRule.onNodeWithTag("emergency_sos_button").performClick()

        assertTrue("onPressed callback was executed", clicked)
        val startedIntent = shadowOf(app).nextStartedActivity
        assertNotNull("Clicking SOS button launches emergency dispatch intent", startedIntent)
    }
}
