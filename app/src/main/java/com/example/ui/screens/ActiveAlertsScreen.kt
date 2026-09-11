package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Tsunami
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SeverityLevel
import com.example.ui.DisasterViewModel
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.AmberWarningContainer
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueContainer
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.EmeraldSafe
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
fun ActiveAlertsScreen(
    viewModel: DisasterViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val alerts by viewModel.officialAlerts.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Early warning banner
            Surface(
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Public, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                            Text("Global & National Early Warning Feed", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                        }
                        Surface(
                            color = EmergencyRed,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "4 RED ALERTS",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Harmonized telemetry via NDMA, GDACS, USGS & IMD satellite clusters. Updated 2m ago.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val cats = listOf("All", "Floods & Tsunami (9)", "Cyclones (5)", "Earthquakes (6)", "Heatwaves (4)")
                cats.forEach { cat ->
                    val isSel = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSel) CivicBlue else SurfaceContainerLow,
                        border = if (isSel) null else BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable { selectedCategory = cat }
                            .testTag("filter_$cat")
                    ) {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) Color.White else TextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // MEA Diaspora Safety Protocol Card
        item {
            Surface(
                color = NavyDeep,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.FlightTakeoff, contentDescription = null, tint = CivicBlueContainer, modifier = Modifier.size(16.dp))
                            Text("Indian Diaspora Crisis Protocol", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Surface(color = NavyContainer, shape = RoundedCornerShape(4.dp)) {
                            Text("MEA Active", color = EmeraldSafe, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                    }
                    Text(
                        text = "Consular safety protocols active for Indian nationals & students in coastal cyclone and seismic zones.",
                        color = Color(0xFFDCE9FF),
                        fontSize = 11.sp
                    )
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+911123012113"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CivicBlueContainer),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("MEA 24/7 Helpline (+91-11-23012113)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Bulletins list
        items(alerts.size) { index ->
            val alert = alerts[index]
            val isCritical = alert.severity == SeverityLevel.CRITICAL

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isCritical) EmergencyRedContainer.copy(alpha = 0.35f) else SurfaceContainerLowest
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    1.5.dp,
                    if (isCritical) EmergencyRed else if (alert.severity == SeverityLevel.HIGH) AmberWarning else CivicBlue.copy(alpha = 0.4f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                        Surface(
                            color = if (isCritical) EmergencyRed else AmberWarning,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = alert.severity.label.uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "Valid: ${alert.validTill}",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Text(
                        text = "Impact Corridor: ${alert.targetArea}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    if (alert.sustainedWind != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = SurfaceContainerLow,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("Wind Velocity", fontSize = 9.sp, color = TextSecondary)
                                    Text(alert.sustainedWind, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                                }
                            }
                            Surface(
                                color = SurfaceContainerLow,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("Storm Surge", fontSize = 9.sp, color = TextSecondary)
                                    Text(alert.stormSurge ?: "N/A", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmergencyRed)
                                }
                            }
                        }
                    }

                    Surface(
                        color = SurfaceContainerLowest,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.CrisisAlert, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(16.dp))
                            Text(alert.directive, fontSize = 11.sp, color = TextPrimary)
                        }
                    }

                    Text(
                        text = "Source: ${alert.sourceAgency}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
