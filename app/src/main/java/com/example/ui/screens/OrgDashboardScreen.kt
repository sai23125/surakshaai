package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppRole
import com.example.ui.DisasterViewModel
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueContainer
import com.example.ui.theme.CivicBlueLight
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.EmeraldSafe
import com.example.ui.theme.EmeraldSafeContainer
import com.example.ui.theme.NavyContainer
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OrgDashboardScreen(
    viewModel: DisasterViewModel,
    onSwitchToCitizen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reports by viewModel.incidentReports.collectAsState()
    var isTriageVerified by remember { mutableStateOf(false) }
    var isFieldDispatched by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Command Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onSwitchToCitizen) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Return", tint = TextPrimary)
                    }
                    Column {
                        Text("SURAKSHAAI CONTROL ROOM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CivicBlue)
                        Text("DDMA COMMAND", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    }
                }

                Surface(
                    color = NavyContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                        viewModel.selectRole(AppRole.CITIZEN)
                        onSwitchToCitizen()
                    }
                ) {
                    Text(
                        text = "Switch to Citizen",
                        color = CivicBlueLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Active Zone Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Radar, contentDescription = null, tint = CivicBlueContainer, modifier = Modifier.size(16.dp))
                            Text("Vijayawada Urban Zone • 18 Wards", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Surface(color = EmeraldSafe, shape = RoundedCornerShape(4.dp)) {
                            Text("LIVE SYNC", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                    }
                    Text(
                        text = "Krishna District DDMA Command • Auto-refresh 30s • 4 Field Teams Deployed",
                        color = Color(0xFFDCE9FF),
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 4 Situation Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Metric 1: Alerts
                Surface(
                    color = SurfaceContainerLowest,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(16.dp))
                        Text("1 Active", fontWeight = FontWeight.Black, fontSize = 14.sp, color = TextPrimary)
                        Text("Official Alerts", fontSize = 9.sp, color = TextSecondary)
                    }
                }
                // Metric 2: Citizen Reports
                Surface(
                    color = SurfaceContainerLowest,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                        Text("${reports.size} Total", fontWeight = FontWeight.Black, fontSize = 14.sp, color = TextPrimary)
                        Text("Citizen Reports", fontSize = 9.sp, color = TextSecondary)
                    }
                }
                // Metric 3: High-Risk Wards
                Surface(
                    color = SurfaceContainerLowest,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.Emergency, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(16.dp))
                        Text("2 Wards", fontWeight = FontWeight.Black, fontSize = 14.sp, color = EmergencyRed)
                        Text("Ward 14 & 19", fontSize = 9.sp, color = TextSecondary)
                    }
                }
                // Metric 4: Field Ops
                Surface(
                    color = SurfaceContainerLowest,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.TaskAlt, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(16.dp))
                        Text("3 Open", fontWeight = FontWeight.Black, fontSize = 14.sp, color = TextPrimary)
                        Text("Dispatch Ops", fontSize = 9.sp, color = TextSecondary)
                    }
                }
            }
        }

        // AI Triage & Priority Queue
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, EmergencyRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_triage_card")
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(color = EmergencyRed, shape = RoundedCornerShape(4.dp)) {
                            Text(
                                text = "PRIORITY #1 • CRITICAL ACTION",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text("Score: 98.4 / 100", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = EmergencyRed)
                    }

                    Text(
                        text = "Urban Hydrology Breach: Severe Waterlogging (45cm) & Drainage Backflow",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Text(
                        text = "Location: Bhavanipuram Primary Health Centre Approach Road, Ward 14",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    // Evidence attached
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.PermMedia, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(14.dp))
                        Text("Telemetry Evidence: 4 citizen reports + 1 GPS photo confirmed", fontSize = 11.sp, color = CivicBlue)
                    }

                    // Why Prioritized AI Explanation
                    Surface(
                        color = SurfaceContainerLow,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(15.dp))
                                Text("Why Prioritized (SurakshaAI Intelligence)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CivicBlue)
                            }
                            Text(
                                text = "Blocks primary ambulance access to PHC; low-lying terrain (12m elevation) combined with 18mm rain and proximity to Krishna Western Main Canal.",
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                    }

                    // Recommended Protocol
                    Surface(
                        color = SurfaceContainerHigh,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.BuildCircle, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                            Text("Recommended: Deploy Mobile De-watering Pump Unit #3 & barricade road.", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        }
                    }

                    // Verification & Dispatch Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { isTriageVerified = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTriageVerified) EmeraldSafe else CivicBlue
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isTriageVerified) "Incident Verified" else "Verify Incident", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { isFieldDispatched = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFieldDispatched) EmeraldSafe else EmergencyRed
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isFieldDispatched) "Unit #3 En Route" else "Assign Field Unit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Active Incident Roster
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Active Incident Roster", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)

                    reports.forEach { report ->
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${report.id} • ${report.timestamp}", fontSize = 10.sp, color = TextSecondary)
                                    Text(report.incidentType, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                                    Text(report.locationName, fontSize = 11.sp, color = TextSecondary)
                                }
                                Surface(
                                    color = SurfaceContainerHigh,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(report.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CivicBlue, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
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
