package com.example.campuscopilot.ui.screens.profile

import android.widget.Toast
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuscopilot.data.models.User
import com.example.campuscopilot.domain.repositories.SavedRepository
import com.example.campuscopilot.ui.components.AppleCard
import com.example.campuscopilot.ui.components.TagPill
import com.example.campuscopilot.theme.PrimaryAccent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    savedRepository: SavedRepository,
    user: User = User()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isDarkTheme by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.displayLarge
            )
        }

        // USER HEADER CARD
        item {
            AppleCard(
                backgroundColor = MaterialTheme.colorScheme.surface,
                cornerRadius = 20.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.take(1),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = user.role,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TagPill(text = "AI Launchpad", isSelected = true)
                    }
                }
            }
        }

        // STATS ROW
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    number = "${user.studyPlansCount}",
                    label = "Study Plans",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    number = "${user.quizzesCount}",
                    label = "Quizzes",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    number = "${user.questionsAskedCount}",
                    label = "Questions",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // SETTINGS LIST
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "SETTINGS",
                    style = MaterialTheme.typography.labelSmall
                )

                AppleCard(cornerRadius = 18.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        SettingRow(
                            icon = Icons.Outlined.DarkMode,
                            title = "Appearance",
                            subtitle = "Toggle dark mode theme",
                            trailing = {
                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = { isDarkTheme = it }
                                )
                            }
                        )

                        Divider(color = MaterialTheme.colorScheme.outline)

                        SettingRow(
                            icon = Icons.Outlined.DeleteSweep,
                            title = "Clear Saved Content",
                            subtitle = "Remove all bookmarked plans & quizzes",
                            onClick = {
                                coroutineScope.launch {
                                    savedRepository.clearAll()
                                    Toast.makeText(context, "Saved content cleared", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        Divider(color = MaterialTheme.colorScheme.outline)

                        SettingRow(
                            icon = Icons.Outlined.CleaningServices,
                            title = "Clear Chat History",
                            subtitle = "Reset active chat messages",
                            onClick = {
                                Toast.makeText(context, "Chat history cleared", Toast.LENGTH_SHORT).show()
                            }
                        )

                        Divider(color = MaterialTheme.colorScheme.outline)

                        SettingRow(
                            icon = Icons.Outlined.Info,
                            title = "About Campus Copilot",
                            subtitle = "Version 1.0.0 • AI Hackathon Edition",
                            onClick = {
                                Toast.makeText(context, "Campus Copilot v1.0.0", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {
    AppleCard(
        modifier = modifier,
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryAccent
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (trailing != null) {
            trailing()
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
