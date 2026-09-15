package com.example.campuscopilot.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuscopilot.ui.components.AppleCard
import com.example.campuscopilot.ui.components.SectionHeader
import com.example.campuscopilot.theme.PrimaryAccent

@Composable
fun HomeScreen(
    onNavigateToAsk: (prompt: String?) -> Unit,
    onNavigateToAskCampus: () -> Unit,
    onNavigateToStudyTab: (studyTab: String) -> Unit,
    userName: String = "Madhav"
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // TOP GREETING
        item {
            Column {
                Text(
                    text = "Good morning, $userName",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ready to get something done?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // LARGE AI INPUT CARD
        item {
            AppleCard(
                onClick = { onNavigateToAsk(null) },
                backgroundColor = MaterialTheme.colorScheme.surface,
                cornerRadius = 20.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "What do you need help with?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Ask AI to plan, explain, quiz, or find campus info",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // QUICK ACTIONS (4 compact cards)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "QUICK ACTIONS",
                    style = MaterialTheme.typography.labelSmall
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionTile(
                        title = "Study Plan",
                        icon = Icons.Outlined.EventNote,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToStudyTab("PLAN") }
                    )
                    QuickActionTile(
                        title = "Explain",
                        icon = Icons.Outlined.Lightbulb,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToStudyTab("EXPLAIN") }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionTile(
                        title = "Quiz Me",
                        icon = Icons.Outlined.Quiz,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToStudyTab("QUIZ") }
                    )
                    QuickActionTile(
                        title = "Ask Campus",
                        icon = Icons.Outlined.AccountBalance,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToAskCampus() }
                    )
                }
            }
        }

        // CONTINUE WHERE YOU LEFT OFF
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionHeader(title = "Continue where you left off")

                ActivityCard(
                    title = "Python — Loops & Lists",
                    subtitle = "2-Hour Study Plan • 40% Completed",
                    tag = "Study Plan",
                    icon = Icons.Outlined.Code,
                    onClick = { onNavigateToStudyTab("PLAN") }
                )
                ActivityCard(
                    title = "Physics — Current Electricity",
                    subtitle = "Concept Breakdown • Quick Review",
                    tag = "Explain",
                    icon = Icons.Outlined.ElectricBolt,
                    onClick = { onNavigateToStudyTab("EXPLAIN") }
                )
                ActivityCard(
                    title = "Campus clubs",
                    subtitle = "Coding Club & Robotics Lab info",
                    tag = "Ask Campus",
                    icon = Icons.Outlined.Groups,
                    onClick = { onNavigateToAskCampus() }
                )
            }
        }

        // TODAY'S FOCUS CARD
        item {
            AppleCard(
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                borderColor = MaterialTheme.colorScheme.outline
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrimaryAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Today's Focus",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Complete 2 remaining blocks for Python loops before tomorrow's test.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionTile(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AppleCard(
        modifier = modifier,
        cornerRadius = 16.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ActivityCard(
    title: String,
    subtitle: String,
    tag: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    AppleCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall,
                color = PrimaryAccent,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
