package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PreparednessTaskEntity
import com.example.ui.DisasterViewModel
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueLight
import com.example.ui.theme.EmeraldSafe
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Offline-accessible Disaster Preparedness Checklist Component.
 * Backed by local Room SQLite database with full reactive state flow.
 * Allows citizens to toggle critical safety tasks even during complete cellular blackout.
 */
@Composable
fun DisasterPreparednessChecklist(
    viewModel: DisasterViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initPreparednessDatabase(context)
    }

    val tasks by viewModel.preparednessTasks.collectAsState()
    val selectedCategory by viewModel.selectedPrepCategory.collectAsState()

    var isExpanded by remember { mutableStateOf(true) }
    var showTipsInfo by remember { mutableStateOf(false) }

    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "prep_progress")

    // Filter tasks based on selected category
    val displayedTasks = remember(tasks, selectedCategory) {
        when (selectedCategory) {
            "ALL" -> tasks
            "CRITICAL" -> tasks.filter { it.priority == "CRITICAL" }
            "PENDING" -> tasks.filter { !it.isCompleted }
            "DONE" -> tasks.filter { it.isCompleted }
            else -> tasks.filter { it.category == selectedCategory }
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("preparedness_checklist_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Title + Offline Badge
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
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CivicBlue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = CivicBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Disaster Preparedness Checklist",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = TextPrimary
                        )
                        Text(
                            text = "Standard Operating Safety Protocol (NDRF / SDMA)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                // Offline badge
                Surface(
                    color = EmeraldSafe.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, EmeraldSafe.copy(alpha = 0.25f)),
                    modifier = Modifier.testTag("offline_status_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = "Offline indicator",
                            tint = EmeraldSafe,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "100% Offline",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSafe
                        )
                    }
                }
            }

            // Progress Banner & Metric Card
            Surface(
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${(progressFraction * 100).toInt()}% Readiness Score",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                ),
                                color = if (progressFraction >= 0.8f) EmeraldSafe else if (progressFraction >= 0.5f) CivicBlue else AmberWarning
                            )
                            Text(
                                text = "$completedCount of $totalCount critical safety tasks verified",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        // Readiness Badge
                        val readinessLabel = when {
                            progressFraction >= 0.8f -> "Evacuation Ready"
                            progressFraction >= 0.5f -> "Moderate Prep"
                            else -> "Action Required"
                        }
                        val readinessColor = when {
                            progressFraction >= 0.8f -> EmeraldSafe
                            progressFraction >= 0.5f -> CivicBlue
                            else -> EmergencyRed
                        }

                        Surface(
                            color = readinessColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, readinessColor.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = readinessLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = readinessColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Linear Animated Progress Bar
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .testTag("preparedness_progress_bar"),
                        color = if (progressFraction >= 0.8f) EmeraldSafe else CivicBlue,
                        trackColor = SurfaceContainerHigh,
                        strokeCap = StrokeCap.Round,
                    )
                }
            }

            // Category Filter Chips
            val categories = listOf(
                "ALL" to "All (${tasks.size})",
                "CRITICAL" to "Critical (${tasks.count { it.priority == "CRITICAL" }})",
                "GO_BAG" to "Go-Bag",
                "UTILITIES" to "Utilities",
                "EVACUATION" to "Evacuation",
                "MEDICAL" to "Medical",
                "DOCUMENTS" to "Documents"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { (catKey, catName) ->
                    val isSelected = selectedCategory == catKey
                    Surface(
                        color = if (isSelected) CivicBlue else SurfaceContainerLow,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, if (isSelected) CivicBlue else OutlineVariant),
                        modifier = Modifier
                            .clickable { viewModel.selectPrepCategory(catKey) }
                            .testTag("prep_category_$catKey")
                    ) {
                        Text(
                            text = catName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Quick Batch Action Header (Toggle view & batch mark)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SAFETY TASKS (${displayedTasks.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    ),
                    color = TextSecondary,
                    fontSize = 10.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextButton(
                        onClick = { viewModel.resetAllPreparednessTasks() },
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("reset_checklist_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = CivicBlue,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Reset", fontSize = 11.sp, color = CivicBlue, fontWeight = FontWeight.Bold)
                    }

                    Surface(
                        color = SurfaceContainerHigh,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable { isExpanded = !isExpanded }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Interactive Task Items List
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    displayedTasks.forEach { task ->
                        PreparednessTaskRow(
                            task = task,
                            onToggle = { viewModel.togglePreparednessTask(task.id) }
                        )
                    }
                }
            }

            // Educational Tip Footer
            Surface(
                color = CivicBlueLight.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTipsInfo = !showTipsInfo }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CivicBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Preparedness checklist syncs locally to device storage without cellular data.",
                        style = MaterialTheme.typography.labelSmall,
                        color = CivicBlue,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Individual checklist task item with interactive check, priority indicator,
 * and accessible touch target.
 */
@Composable
fun PreparednessTaskRow(
    task: PreparednessTaskEntity,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (task.isCompleted) EmeraldSafe.copy(alpha = 0.4f) else OutlineVariant,
        label = "border_anim"
    )
    val containerColor by animateColorAsState(
        targetValue = if (task.isCompleted) SurfaceContainerLow else SurfaceContainerLowest,
        label = "container_anim"
    )

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag("preparedness_task_${task.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Interactive Checkbox / Circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (task.isCompleted) EmeraldSafe else Color.Transparent
                    )
                    .clickable { onToggle() }
                    .testTag("task_toggle_${task.id}"),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Not completed",
                        tint = OutlineVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Task Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = if (task.isCompleted) TextSecondary else TextPrimary,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Priority Tag
                    val (badgeBg, badgeText) = when (task.priority) {
                        "CRITICAL" -> EmergencyRed.copy(alpha = 0.12f) to EmergencyRed
                        "HIGH" -> AmberWarning.copy(alpha = 0.12f) to AmberWarning
                        else -> CivicBlue.copy(alpha = 0.10f) to CivicBlue
                    }
                    Surface(
                        color = badgeBg,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text(
                            text = task.priority,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = badgeText,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (task.isCompleted) OutlineVariant else TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Category indicator tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (task.isCompleted) EmeraldSafe else CivicBlue)
                    )
                    Text(
                        text = task.categoryLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
