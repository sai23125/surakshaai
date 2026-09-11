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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.DisasterViewModel
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueContainer
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.EmeraldSafe
import com.example.ui.theme.EmeraldSafeContainer
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun IncidentReportScreen(
    viewModel: DisasterViewModel,
    onNavigateBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reportType by viewModel.reportType.collectAsState()
    val severity by viewModel.reportSeverity.collectAsState()
    val waterDepth by viewModel.reportWaterDepth.collectAsState()
    val notes by viewModel.reportNotes.collectAsState()
    val isSubmitted by viewModel.reportSubmitted.collectAsState()
    val location by viewModel.currentLocation.collectAsState()
    var photoAttached by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Header
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Civic Response Dispatch",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = CivicBlue
                )
                Text(
                    text = "Report a Civic Hazard",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = TextPrimary
                )
                Text(
                    text = "Help your neighborhood and disaster teams stay informed. Reports are verified before municipal triage.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Privacy Protected Banner
        item {
            Surface(
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, CivicBlue.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                    Column {
                        Text(
                            text = "Privacy Protected & Verified",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = CivicBlue
                        )
                        Text(
                            text = "Your name and exact home coordinates will not be shown publicly. Reports are displayed with general ward/street-level accuracy.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        if (isSubmitted) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = EmeraldSafeContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, EmeraldSafe),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(44.dp))
                        Text(
                            text = "Hazard Report Dispatched!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Your report for $location has been recorded. It is currently being triaged by GMC / NDRF field responders.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Button(
                            onClick = {
                                viewModel.resetReportForm()
                                onNavigateBackToHome()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CivicBlue)
                        ) {
                            Text("Return to Safety Dashboard", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Step 1: Incident Type
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("1. Select Incident Type", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)

                        val types = listOf(
                            Pair("Waterlogging", "🌊 Urban pooling"),
                            Pair("Severe Flooding", "🌧 River surge"),
                            Pair("Blocked Road", "🚧 Debris & silt"),
                            Pair("Fallen Tree / Pole", "🌳 Live wire hazard"),
                            Pair("Heat Emergency", "☀️ Water kiosk need"),
                            Pair("Other Hazard", "⚠️ Civic disruption")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            types.chunked(2).forEach { rowPair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    rowPair.forEach { (t, desc) ->
                                        val isSelected = reportType == t
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) CivicBlueContainer.copy(alpha = 0.15f) else SurfaceContainerLow,
                                            border = if (isSelected) BorderStroke(1.5.dp, CivicBlue) else null,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { viewModel.updateReportType(t) }
                                                .testTag("type_pill_$t")
                                        ) {
                                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                                                Text(t, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                                                Text(desc, fontSize = 10.sp, color = TextSecondary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Location
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("General Incident Location", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Surface(
                            color = SurfaceContainerLow,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(18.dp))
                                Column {
                                    Text("$location (Ward 14)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                                    Text("High-Accuracy GPS Beacon Active (±4m)", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // Step 2: Severity
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("2. Hazard Severity", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        val severities = listOf(
                            Pair(SeverityLevel.ADVISORY, "Mild Severity (Passable with caution)"),
                            Pair(SeverityLevel.MODERATE, "Moderate (2-wheelers obstructed)"),
                            Pair(SeverityLevel.CRITICAL, "Critical Emergency (Life / property risk)")
                        )
                        severities.forEach { (sev, desc) ->
                            val isSelected = severity == sev
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) SurfaceContainerHigh else SurfaceContainerLow,
                                border = if (isSelected) BorderStroke(1.5.dp, CivicBlue) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.updateReportSeverity(sev) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when (sev) {
                                                    SeverityLevel.ADVISORY -> EmeraldSafe
                                                    SeverityLevel.MODERATE -> AmberWarning
                                                    else -> EmergencyRed
                                                }
                                            )
                                    )
                                    Text(desc, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = TextPrimary)
                                }
                            }
                        }
                    }
                }
            }

            // Step 3: Estimated Water Depth
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("3. Estimated Water Depth (Optional)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val depths = listOf("Ankle (~10cm)", "Knee (30-50cm)", "Waist (>75cm)")
                            depths.forEach { d ->
                                val isSelected = waterDepth.startsWith(d.take(4))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) CivicBlue else SurfaceContainerLow,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.updateReportWaterDepth(d) }
                                ) {
                                    Text(
                                        text = d,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else TextPrimary,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step 4: Ground Observations
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("4. Ground Observations", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { viewModel.updateReportNotes(it) },
                            placeholder = { Text("e.g., Drainage overflowed near school gate. 2-wheelers cannot pass.", fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .testTag("report_notes_input")
                        )
                    }
                }
            }

            // Step 5: Photo Evidence & Submit
            item {
                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { photoAttached = !photoAttached }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = CivicBlue)
                            Column {
                                Text(
                                    text = if (photoAttached) "Photo Evidence Attached (1 image)" else "Attach Live Photo / Hazard Evidence",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                                Text("GPS & timestamp will be watermarked automatically", fontSize = 10.sp, color = TextSecondary)
                            }
                        }
                        if (photoAttached) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        if (notes.isBlank()) {
                            viewModel.updateReportNotes("Waterlogging observed obstructing municipal access road.")
                        }
                        viewModel.submitIncidentReport()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CivicBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_report")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Verified Report to Responders", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
