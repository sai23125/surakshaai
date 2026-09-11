package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EmergencyContact
import com.example.model.Language
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueContainer
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SurakshaHeader(
    currentLanguage: Language,
    onLanguageClick: () -> Unit,
    onSosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "SurakshaAI Logo",
                        tint = CivicBlueContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "SURAKSHAAI",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            fontSize = 17.sp
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = "DISASTER SAFETY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontSize = 10.sp
                        ),
                        color = CivicBlue
                    )
                }
            }

            // Quick Actions: Translate, SOS, Profile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Switcher
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier
                        .height(38.dp)
                        .clickable { onLanguageClick() }
                        .testTag("lang_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Change Language",
                            tint = CivicBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = currentLanguage.code.uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                    }
                }

                // SOS Emergency Button
                Button(
                    onClick = onSosClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmergencyRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("sos_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency SOS",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "SOS",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Profile Avatar Indicator
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerLow)
                        .border(1.5.dp, CivicBlueContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Verified Profile",
                        tint = CivicBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LocationSubHeader(
    currentLocation: String,
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "refresh_anim")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Surface(
        color = SurfaceContainerLow.copy(alpha = 0.95f),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onLocationClick() }
                    .testTag("location_chip")
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Pin",
                    tint = CivicBlue,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = currentLocation,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "• 1m ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .clickable { onRefreshClick() }
                    .testTag("refresh_gps_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh GPS Telemetry",
                    tint = CivicBlue,
                    modifier = Modifier
                        .size(15.dp)
                        .then(if (isRefreshing) Modifier.rotate(rotation) else Modifier)
                )
                Text(
                    text = if (isRefreshing) "Syncing..." else "Refresh",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = CivicBlue
                )
            }
        }
    }
}

@Composable
fun EmergencySosDialog(
    contacts: List<EmergencyContact>,
    onDismiss: () -> Unit,
    currentLocation: String = "Bhavanipuram, Ward 14, Vijayawada North",
    latitude: Double = 16.5186,
    longitude: Double = 80.6195
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Emergency Alert",
                    tint = EmergencyRed,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "EMERGENCY SOS DISPATCH",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = EmergencyRed
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Tap any service below for immediate direct dial. Your GPS coordinates will be transmitted to first responder units.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Surface(
                    color = EmergencyRedContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = EmergencyRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Live GPS Beacon: $currentLocation (%.4f° N, %.4f° E)".format(latitude, longitude),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmergencyRed
                            )
                        )
                    }
                }

                contacts.take(4).forEach { contact ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceContainerLow,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.number.replace("-", "")}"))
                                context.startActivity(intent)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = contact.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = contact.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(EmergencyRed)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = contact.number,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    sendEmergencySosDistress(
                        context = context,
                        contacts = contacts.map { it.number },
                        location = currentLocation,
                        latitude = latitude,
                        longitude = longitude
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                modifier = Modifier.testTag("dialog_broadcast_sos_button")
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Broadcast SOS SMS")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    tint = CivicBlue
                )
                Text("Select App Language", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Choose your preferred language. All safety advisories, voice guidance, and maps will update instantly.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Language.values().forEach { lang ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (lang == currentLanguage) CivicBlueContainer.copy(alpha = 0.15f) else SurfaceContainerLow,
                        border = if (lang == currentLanguage) androidx.compose.foundation.BorderStroke(1.5.dp, CivicBlue) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(lang)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = lang.flag, fontSize = 20.sp)
                                Column {
                                    Text(
                                        text = lang.nativeName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = lang.displayName,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                            if (lang == currentLanguage) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = CivicBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun ManualLocationDialog(
    currentLocation: String,
    onLocationChosen: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val cities = listOf(
        "Vijayawada North, AP",
        "Krishna River Basin, Vijayawada",
        "Hyderabad, Telangana",
        "Visakhapatnam Coastal, AP",
        "Chennai, Tamil Nadu",
        "Bhubaneswar, Odisha"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = CivicBlue)
                Text("Select Hazard Area", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Select your municipality or river basin district to view localized hydrological telemetry and NDMA bulletins:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                cities.forEach { city ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (city == currentLocation) CivicBlue.copy(alpha = 0.12f) else SurfaceContainerLow,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLocationChosen(city)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = city,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (city == currentLocation) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = TextPrimary
                            )
                            if (city == currentLocation) {
                                Icon(
                                    Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = CivicBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun SurakshaBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    alertCount: Int = 1,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        tonalElevation = 6.dp,
        modifier = modifier.navigationBarsPadding()
    ) {
        // Home Feed
        NavigationBarItem(
            selected = currentRoute == "home-feed",
            onClick = { onNavigate("home-feed") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlue.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_home")
        )

        // Disaster Map
        NavigationBarItem(
            selected = currentRoute == "disaster-map",
            onClick = { onNavigate("disaster-map") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = "Map"
                )
            },
            label = { Text("Map", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlue.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_map")
        )

        // Report Hazard
        NavigationBarItem(
            selected = currentRoute == "incident-report",
            onClick = { onNavigate("incident-report") },
            icon = {
                Icon(
                    imageVector = Icons.Default.NotificationImportant,
                    contentDescription = "Report"
                )
            },
            label = { Text("Report", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlue.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_report")
        )

        // Active Alerts
        NavigationBarItem(
            selected = currentRoute == "active-alerts",
            onClick = { onNavigate("active-alerts") },
            icon = {
                Box {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Alerts"
                    )
                    if (alertCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(EmergencyRed)
                        )
                    }
                }
            },
            label = { Text("Alerts", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlue.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_alerts")
        )

        // Safe Places / Relief Shelters
        NavigationBarItem(
            selected = currentRoute == "safe-places",
            onClick = { onNavigate("safe-places") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Safe Places"
                )
            },
            label = { Text("Places", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlue.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_places")
        )
    }
}

@Composable
fun AskSurakshaFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = NavyContainer,
        contentColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.testTag("fab_ask_suraksha")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Ask SurakshaAI",
                tint = CivicBlueContainer,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Ask SurakshaAI",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}
