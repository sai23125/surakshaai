package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tsunami
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SeverityLevel
import com.example.ui.components.DisasterPreparednessChecklist
import com.example.ui.components.EmergencySosButton
import com.example.ui.components.LocationPermissionCard
import com.example.ui.components.SosButtonVariant
import com.example.ui.DisasterViewModel
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.AmberWarningContainer
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueContainer
import com.example.ui.theme.CivicBlueLight
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldSafe
import com.example.ui.theme.EmeraldSafeContainer
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CitizenHomeScreen(
    viewModel: DisasterViewModel,
    onNavigateToMap: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToPlaces: () -> Unit,
    onNavigateToAsk: () -> Unit,
    modifier: Modifier = Modifier
) {
    val risk by viewModel.hyperlocalRisk.collectAsState()
    val currentCoordinates by viewModel.currentCoordinates.collectAsState()
    val officialAlerts by viewModel.officialAlerts.collectAsState()
    val civicActions by viewModel.civicActions.collectAsState()
    val reports by viewModel.incidentReports.collectAsState()
    var showTransparencyDetails by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Welcome Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Civic Early Warning System",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        ),
                        color = CivicBlue
                    )
                    Text(
                        text = "Good morning, Ramesh",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = TextPrimary
                    )
                }

                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, CivicBlue.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = null,
                            tint = CivicBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Ward 14 Active",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Live Play Services Location Permission & Beacon
        item {
            LocationPermissionCard(
                viewModel = viewModel,
                onManualLocationClick = { viewModel.toggleLocationDialog(true) }
            )
        }

        // Dedicated Emergency SOS Dispatch Action
        item {
            EmergencySosButton(
                currentLocation = currentCoordinates.address ?: risk.locationName,
                contacts = listOf("112", "1070", "9711077372"),
                latitude = currentCoordinates.latitude,
                longitude = currentCoordinates.longitude,
                variant = SosButtonVariant.PROMINENT
            )
        }

        // Official Alert Card (NDMA / IMD SACHET)
        item {
            val alert = officialAlerts.firstOrNull()
            if (alert != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = AmberWarningContainer.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, AmberWarning),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("official_alert_card")
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
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = AmberWarning,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "OFFICIAL ALERT • NDMA / IMD SACHET",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = AmberWarning
                                )
                            }
                            Surface(
                                color = AmberWarning,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "MODERATE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = alert.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

                        Text(
                            text = "Target Area: ${alert.targetArea}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Surface(
                            color = SurfaceContainerLowest,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, AmberWarning.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CrisisAlert,
                                    contentDescription = null,
                                    tint = EmergencyRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Civic Directive:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = EmergencyRed
                                    )
                                    Text(
                                        text = alert.directive,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Issued: ${alert.issuedTime} • Valid: ${alert.validTill}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "Details →",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = CivicBlue,
                                modifier = Modifier.clickable { onNavigateToAlerts() }
                            )
                        }
                    }
                }
            }
        }

        // Hyperlocal Risk Assessment Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AmberWarning.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(
                                    text = "Your Hyperlocal Risk",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Ward 14 • Budameru Corridor",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            color = AmberWarning.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, AmberWarning)
                        ) {
                            Text(
                                text = "🟡 MODERATE RISK",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = AmberWarning,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    // Dual Sub-Risk Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Flood Risk
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Tsunami, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                                    Text("Flood Risk", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                }
                                Text("MODERATE", fontWeight = FontWeight.Black, fontSize = 15.sp, color = AmberWarning)
                                Text("Canal Surge: +18mm rain in 3h", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        // Heat Risk
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Thermostat, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(16.dp))
                                    Text("Heat Risk", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                }
                                Text("LOW", fontWeight = FontWeight.Black, fontSize = 15.sp, color = EmeraldSafe)
                                Text("Normal: 29°C, 74% humidity", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                            }
                        }
                    }

                    // Algorithm Transparency Toggle
                    Surface(
                        color = SurfaceContainerHigh,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTransparencyDetails = !showTransparencyDetails }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(15.dp))
                                Text(
                                    text = "Why is my risk like this? → Algorithm Transparency",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = CivicBlue
                                )
                            }
                            Text(
                                text = if (showTransparencyDetails) "▲" else "▼",
                                fontSize = 11.sp,
                                color = CivicBlue
                            )
                        }
                    }

                    AnimatedVisibility(visible = showTransparencyDetails) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Elevation Gradient: Device is at ${risk.elevationAmsl}m AMSL, situated ${risk.corridorDistanceMeters}m from the Budameru diversion corridor.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Telemetry Feed: Discharge recorded at Prakasam Barrage reached 42,000 cusecs at 08:00 AM.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Environmental Telemetry (Open-Meteo & IMD Radars)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
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
                            Icon(Icons.Default.Public, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                            Text(
                                text = "What's Happening Now? (Live Radar)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "Updated 2m ago",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    // 4-cell weather telemetry grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Temp
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Icon(Icons.Default.Cloud, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(18.dp))
                                Text("${risk.temperatureCelsius}°C", fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextPrimary)
                                Text("Overcast", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        // Rain
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(18.dp))
                                Text("${risk.rainfallLast3hMm} mm", fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextPrimary)
                                Text("Rain (3h)", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        // Humidity
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Icon(Icons.Default.Thermostat, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(18.dp))
                                Text("${risk.humidityPercent}%", fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextPrimary)
                                Text("Humidity", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        // Wind
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Icon(Icons.Default.Air, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(18.dp))
                                Text("${risk.windKmh} km/h", fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextPrimary)
                                Text("Wind", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Offline-Accessible Disaster Preparedness Checklist Component
        item {
            DisasterPreparednessChecklist(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Verified Community Hazards Snippet
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
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
                        Column {
                            Text(
                                text = "Crowdsourced Field Hazards",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "Validated reports from citizens & volunteers",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = onNavigateToReport,
                            colors = ButtonDefaults.buttonColors(containerColor = CivicBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+ Report", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    reports.take(2).forEach { r ->
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = r.incidentType,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = CivicBlue
                                        )
                                        Text("• ${r.timestamp}", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                                    }
                                    Text(
                                        text = r.locationName,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = r.groundObservations,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }

                                Surface(
                                    color = SurfaceContainerLowest,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable { viewModel.upvoteReport(r.id) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.ThumbUp, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(14.dp))
                                        Text("${r.upvotes}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CivicBlue)
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "View all on Interactive Map →",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = CivicBlue,
                        modifier = Modifier.clickable { onNavigateToMap() }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
