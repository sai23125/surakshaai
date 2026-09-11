package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.DomainVerification
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.ui.DisasterViewModel
import com.example.ui.theme.*

@Composable
fun AskSurakshaScreen(
    viewModel: DisasterViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isChatLoading.collectAsState()
    val location by viewModel.currentLocation.collectAsState()
    val risk by viewModel.hyperlocalRisk.collectAsState()
    var inputQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = CivicBlueContainer, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text("Ask SurakshaAI", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Text("Hyperlocal Emergency Decision Support", fontSize = 10.sp, color = CivicBlue)
                    }
                }

                Surface(
                    color = EmeraldSafeContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "LIVE TELEMETRY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Live Context Ribbon
        Surface(
            color = SurfaceContainerLow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📍 $location • Rain: ${risk.rainfallLast3hMm}mm (3h)",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "1 Official Advisory Active",
                    fontSize = 11.sp,
                    color = AmberWarning,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            items(messages) { msg ->
                if (msg.isUser) {
                    // User Message
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            color = CivicBlue,
                            shape = RoundedCornerShape(14.dp, 14.dp, 2.dp, 14.dp),
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(msg.text, color = Color.White, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${msg.timestamp} • Sent",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 9.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                } else {
                    // Assistant Message
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            color = SurfaceContainerLowest,
                            shape = RoundedCornerShape(14.dp, 14.dp, 14.dp, 2.dp),
                            border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.widthIn(max = 320.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.SupportAgent, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                                    Text("SurakshaAI Decision Engine", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CivicBlue)
                                    if (msg.confidenceScore != null) {
                                        Surface(color = SurfaceContainerHigh, shape = RoundedCornerShape(4.dp)) {
                                            Text(
                                                text = "${msg.confidenceScore}% Grounded",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextSecondary,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }

                                Text(msg.text, fontSize = 12.sp, color = TextPrimary)

                                if (msg.actionDirective != null) {
                                    Surface(
                                        color = AmberWarningContainer.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(Icons.Default.CrisisAlert, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(15.dp))
                                            Text(msg.actionDirective, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                    }
                                }

                                if (msg.verifiedShelterName != null) {
                                    Surface(
                                        color = EmeraldSafeContainer.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(Icons.Default.DomainVerification, contentDescription = null, tint = EmeraldSafe, modifier = Modifier.size(15.dp))
                                            Text("Nearest High Ground: ${msg.verifiedShelterName}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                    }
                                }

                                Text(msg.timestamp, fontSize = 9.sp, color = TextSecondary, modifier = Modifier.align(Alignment.End))
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = CivicBlue, strokeWidth = 2.dp)
                        Text("SurakshaAI is analyzing radar telemetry...", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }
        }

        // Quick Suggestion Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val suggestions = listOf(
                "Is Bhavanipuram underpass safe?",
                "Where is nearest relief shelter?",
                "Emergency Go-Bag checklist",
                "How to contact NDRF?"
            )
            suggestions.forEach { sug ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceContainerLow,
                    border = BorderStroke(1.dp, CivicBlue.copy(alpha = 0.2f)),
                    modifier = Modifier.clickable { viewModel.sendChatMessage(sug) }
                ) {
                    Text(
                        text = sug,
                        fontSize = 11.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Bottom Input Row
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Voice Query Simulation Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NavyContainer)
                        .clickable {
                            viewModel.sendChatMessage("Voice Query: Is the Bhavanipuram underpass safe to cross right now?")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = CivicBlueLight, modifier = Modifier.size(20.dp))
                }

                OutlinedTextField(
                    value = inputQuery,
                    onValueChange = { inputQuery = it },
                    placeholder = { Text("Ask safety question in English, Telugu, or Hindi...", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp)
                )

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CivicBlue)
                        .clickable {
                            if (inputQuery.isNotBlank()) {
                                viewModel.sendChatMessage(inputQuery)
                                inputQuery = ""
                            }
                        }
                        .testTag("chat_send_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
