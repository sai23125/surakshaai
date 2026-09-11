package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.OnEmergencyRed
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Visual variants for the Emergency SOS Button.
 */
enum class SosButtonVariant {
    COMPACT,      // Suitable for toolbars, headers, and floating action areas
    PROMINENT,    // High-impact button with animated emergency pulse
    EXTENDED_BANNER // Full-width card with distress preview, GPS beacon, and instant 1-tap dispatch
}

/**
 * Builds the standardized pre-defined emergency distress message with live location & GPS coordinates.
 */
fun buildEmergencyDistressMessage(
    location: String,
    latitude: Double = 16.5186,
    longitude: Double = 80.6195,
    customNote: String? = null
): String {
    val currentTime = SimpleDateFormat("dd MMM yyyy, hh:mm a 'IST'", Locale.getDefault()).format(Date())
    return buildString {
        append("🚨 EMERGENCY SOS DISTRESS ALERT 🚨\n")
        append("URGENT: I need emergency assistance and medical/rescue help immediately!\n\n")
        append("📍 My Current Location: $location\n")
        append("🌐 GPS Coordinates: %.4f° N, %.4f° E\n".format(latitude, longitude))
        append("🗺️ Live Google Maps Navigation: https://maps.google.com/?q=$latitude,$longitude\n")
        append("⏰ Timestamp: $currentTime\n")
        if (!customNote.isNullOrBlank()) {
            append("📝 Ground Situation: $customNote\n")
        }
        append("\nSent via SurakshaAI Civic Emergency Response Network.")
    }
}

/**
 * Triggers an Android Intent to send the user's current location and pre-defined distress message to emergency contacts.
 */
fun sendEmergencySosDistress(
    context: Context,
    contacts: List<String> = listOf("112", "1070", "9711077372"),
    location: String = "Bhavanipuram, Ward 14, Vijayawada North",
    latitude: Double = 16.5186,
    longitude: Double = 80.6195,
    customNote: String? = null
) {
    val distressMessage = buildEmergencyDistressMessage(location, latitude, longitude, customNote)
    val recipientString = contacts.filter { it.isNotBlank() }.joinToString(";")

    // Attempt direct SMS dispatch intent (smsto:)
    val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:$recipientString")
        putExtra("sms_body", distressMessage)
        putExtra(Intent.EXTRA_TEXT, distressMessage)
        putExtra("address", recipientString)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        context.startActivity(smsIntent)
        Toast.makeText(context, "Dispatching SOS Alert to Emergency Contacts...", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        // Fallback: General share/messaging chooser intent
        val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "EMERGENCY SOS DISTRESS ALERT")
            putExtra(Intent.EXTRA_TEXT, distressMessage)
            putExtra("address", recipientString)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(fallbackIntent, "Send Emergency SOS Distress Alert via:")
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(chooser)
        } catch (innerEx: Exception) {
            Toast.makeText(context, "Unable to launch messaging app. Calling 112 directly...", Toast.LENGTH_LONG).show()
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(callIntent)
        }
    }
}

/**
 * Emergency SOS Button Component.
 *
 * When pressed, this component triggers an Android Intent to send the user's current location
 * and a pre-defined distress message to emergency contacts.
 */
@Composable
fun EmergencySosButton(
    currentLocation: String,
    modifier: Modifier = Modifier,
    contacts: List<String> = listOf("112", "1070", "9711077372"),
    latitude: Double = 16.5186,
    longitude: Double = 80.6195,
    customNote: String? = null,
    variant: SosButtonVariant = SosButtonVariant.PROMINENT,
    onPressed: (() -> Unit)? = null
) {
    val context = LocalContext.current

    // Pulsing animation for urgent visual hierarchy
    val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_alpha"
    )

    fun handleSosClick() {
        onPressed?.invoke()
        sendEmergencySosDistress(
            context = context,
            contacts = contacts,
            location = currentLocation,
            latitude = latitude,
            longitude = longitude,
            customNote = customNote
        )
    }

    when (variant) {
        SosButtonVariant.COMPACT -> {
            Button(
                onClick = { handleSosClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmergencyRed,
                    contentColor = OnEmergencyRed
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = modifier
                    .heightIn(min = 48.dp)
                    .testTag("emergency_sos_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Trigger Emergency SOS Distress Alert",
                        tint = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .scale(pulseScale)
                    )
                    Text(
                        text = "SOS",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }

        SosButtonVariant.PROMINENT -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer pulsating halo ring
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .scale(pulseScale)
                        .clip(RoundedCornerShape(16.dp))
                        .background(EmergencyRed.copy(alpha = ringAlpha))
                )

                Button(
                    onClick = { handleSosClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmergencyRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .testTag("emergency_sos_button")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = "Emergency SOS",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(22.dp)
                                    .scale(pulseScale)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(horizontalAlignment = Alignment.Start) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "EMERGENCY SOS",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    letterSpacing = 1.sp,
                                    color = Color.White
                                )
                                Surface(
                                    color = Color.White.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "1-TAP INTENT",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Sends live GPS coordinates & distress message to 112 & contacts",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        SosButtonVariant.EXTENDED_BANNER -> {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, EmergencyRed),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .testTag("emergency_sos_card")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = null,
                                tint = EmergencyRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Distress SOS Broadcast",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                                color = TextPrimary
                            )
                        }

                        Surface(
                            color = EmergencyRedContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(EmergencyRed)
                                )
                                Text(
                                    text = "LIVE BEACON",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = EmergencyRed
                                )
                            }
                        }
                    }

                    // Location transmission preview
                    Surface(
                        color = SurfaceContainerLow,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GpsFixed,
                                contentDescription = null,
                                tint = EmergencyRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "Coordinates ready: $currentLocation",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Lat: %.4f° N, Long: %.4f° E • Google Maps Link embedded".format(latitude, longitude),
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    // The Big Action Trigger Button
                    Button(
                        onClick = { handleSosClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp)
                            .testTag("emergency_sos_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "SEND DISTRESS MESSAGE & LOCATION",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
