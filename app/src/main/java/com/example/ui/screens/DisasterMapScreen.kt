package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DisasterViewModel
import com.example.ui.theme.*

@Composable
fun DisasterMapScreen(
    viewModel: DisasterViewModel,
    onNavigateToReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shelters by viewModel.reliefShelters.collectAsState()
    val hazards by viewModel.roadHazards.collectAsState()
    var selectedLayer by remember { mutableStateOf("All") }
    var selectedShelterId by remember { mutableStateOf("shelter_1") }
    var showLegend by remember { mutableStateOf(false) }

    val activeShelter = shelters.find { it.id == selectedShelterId } ?: shelters.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Layer Filter Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val layers = listOf("All", "Official Alerts (1)", "Safe Places (3)", "Road Hazards (3)", "Hydro Risk")
                layers.forEach { layer ->
                    val isSelected = selectedLayer == layer
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) CivicBlue else SurfaceContainerLow,
                        border = if (isSelected) null else BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable { selectedLayer = layer }
                            .testTag("layer_pill_$layer")
                    ) {
                        Text(
                            text = layer,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) Color.White else TextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Interactive Geospatial Hazard Canvas
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Draw Krishna River / Canal corridor blue path
                        val riverPath = Path().apply {
                            moveTo(0f, h * 0.7f)
                            cubicTo(w * 0.3f, h * 0.65f, w * 0.6f, h * 0.8f, w, h * 0.75f)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(riverPath, color = Color(0xFF1E3A8A).copy(alpha = 0.45f))

                        // Draw Flood Buffer Contour (Hydro DEM)
                        drawCircle(
                            color = Color(0xFFEF4444).copy(alpha = 0.18f),
                            radius = 110f,
                            center = Offset(w * 0.72f, h * 0.55f)
                        )

                        // Grid Lines (Radar mesh)
                        for (i in 1..4) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.08f),
                                start = Offset(0f, h * i / 5f),
                                end = Offset(w, h * i / 5f),
                                strokeWidth = 1f
                            )
                            drawLine(
                                color = Color.White.copy(alpha = 0.08f),
                                start = Offset(w * i / 5f, 0f),
                                end = Offset(w * i / 5f, h),
                                strokeWidth = 1f
                            )
                        }

                        // Safe Elevation Zone Marker
                        drawCircle(
                            color = Color(0xFF10B981).copy(alpha = 0.25f),
                            radius = 70f,
                            center = Offset(w * 0.28f, h * 0.35f)
                        )
                    }

                    // On-Canvas UI Overlays:
                    // 1. You are here beacon
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(end = 40.dp, top = 20.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CivicBlueContainer,
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.MyLocation, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Text("You: Ward 14 (14m Elev)", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // 2. Safe Shelter Marker
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 24.dp, top = 28.dp)
                            .clickable { selectedShelterId = "shelter_1" }
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldSafe,
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Text("Shelter (1.2km • 19m)", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // 3. Hazard Marker
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 24.dp, bottom = 38.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmergencyRed,
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Text("35cm Waterlogged Underpass", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Top Bar Info: GPS Active & Elevation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = NavyContainer.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "LIVE HYDRO RADAR • Vijayawada North",
                                color = CivicBlueLight,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        IconButton(
                            onClick = { showLegend = !showLegend },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Layers, contentDescription = "Layers Legend", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Legend details
        item {
            AnimatedVisibility(visible = showLegend) {
                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Data Credibility & Layer Legend", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("🛡 Official: NDMA/SDMA", fontSize = 11.sp, color = TextSecondary)
                            Text("📡 Sensors: CWC Telemetry", fontSize = 11.sp, color = TextSecondary)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("👥 Community: 3+ Validated", fontSize = 11.sp, color = TextSecondary)
                            Text("🧠 AI Model: Hydro DEM", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // Verified Safe Place Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, EmeraldSafe),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("safe_place_card")
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
                        Surface(
                            color = EmeraldSafeContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(13.dp))
                                Text(
                                    text = "VERIFIED SAFE PLACE • OPEN NOW",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = EmeraldDark
                                )
                            }
                        }

                        Text(
                            text = "Elev: ${activeShelter.elevationMsl}m MSL",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldSafe
                        )
                    }

                    Column {
                        Text(
                            text = activeShelter.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(14.dp))
                                Text("${activeShelter.distanceKm} km (${activeShelter.driveTimeMins}m drive)", fontSize = 11.sp, color = TextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Text("${activeShelter.walkTimeMins}m walk", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    // Shelter Capacity & Auxiliary Generator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Occupancy", fontSize = 10.sp, color = TextSecondary)
                                Text("${activeShelter.currentOccupancy} / ${activeShelter.maxCapacity}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                            }
                        }
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Generator Backup", fontSize = 10.sp, color = TextSecondary)
                                Text("${activeShelter.fuelReserveHours}h Fuel Reserve", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EmeraldSafe)
                            }
                        }
                    }

                    // Provisions checklist
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Confirmed Emergency Provisions:", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = TextPrimary)
                        activeShelter.provisions.forEach { prov ->
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(14.dp))
                                Text(prov, fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    Text(
                        text = "Audited by ${activeShelter.auditedBy} • ${activeShelter.auditTime}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )

                    // Action Buttons: Safe Directions, Call, Share
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:16.5200,80.6150?q=${Uri.encode(activeShelter.name)}"))
                                context.startActivity(mapIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CivicBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Safe Directions", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${activeShelter.phoneContact}"))
                                context.startActivity(callIntent)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(15.dp))
                        }

                        OutlinedButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Safe Shelter: ${activeShelter.name} is open with drinking water and medical staff. Located 1.2km from Ward 14.")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Safe Shelter"))
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(15.dp))
                        }
                    }
                }
            }
        }

        // Hyperlocal Road Hazards Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(14.dp),
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
                        Text(
                            text = "Hyperlocal Road Hazards",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

                        Button(
                            onClick = onNavigateToReport,
                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+ Report Hazard", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    hazards.forEach { hazard ->
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(EmergencyRedContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (hazard.hazardType == "Waterlogging") Icons.Default.Water else Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = EmergencyRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Column {
                                        Text(hazard.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                                        Text(hazard.location, fontSize = 11.sp, color = TextSecondary)
                                        Text("${hazard.timeAgo} • ${hazard.verificationCount} verified", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }

                                Surface(
                                    color = if (hazard.isPassable) EmeraldSafe.copy(alpha = 0.15f) else EmergencyRed.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (hazard.isPassable) "Passable" else "Impassable",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = if (hazard.isPassable) EmeraldSafe else EmergencyRed,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
