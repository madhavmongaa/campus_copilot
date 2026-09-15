package com.example.campuscopilot.ui.screens.saved

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.campuscopilot.data.models.SavedCategory
import com.example.campuscopilot.data.models.SavedItem
import com.example.campuscopilot.domain.repositories.SavedRepository
import com.example.campuscopilot.ui.components.AppleCard
import com.example.campuscopilot.ui.components.SectionHeader
import com.example.campuscopilot.ui.components.TagPill
import com.example.campuscopilot.theme.PrimaryAccent
import kotlinx.coroutines.launch

@Composable
fun SavedScreen(
    savedRepository: SavedRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val savedItemsState by savedRepository.savedItems.collectAsState()
    var selectedCategory by remember { mutableStateOf(SavedCategory.ALL) }
    var selectedItemForDetail by remember { mutableStateOf<SavedItem?>(null) }

    val filteredItems = remember(savedItemsState, selectedCategory) {
        if (selectedCategory == SavedCategory.ALL) {
            savedItemsState
        } else {
            savedItemsState.filter { it.category == selectedCategory }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // HEADER & FILTER CHIPS
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saved",
                        style = MaterialTheme.typography.displayLarge
                    )
                    if (savedItemsState.isNotEmpty()) {
                        TextButton(onClick = {
                            coroutineScope.launch {
                                savedRepository.clearAll()
                                Toast.makeText(context, "Cleared saved items", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Clear all", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Filter Tabs: All, Plans, Answers, Quizzes
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SavedCategory.values().forEach { cat ->
                        val text = when (cat) {
                            SavedCategory.ALL -> "All (${savedItemsState.size})"
                            SavedCategory.PLANS -> "Plans"
                            SavedCategory.ANSWERS -> "Answers"
                            SavedCategory.QUIZZES -> "Quizzes"
                        }
                        TagPill(
                            text = text,
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }
            }
        }

        // SAVED ITEMS LIST
        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No saved items found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Save study plans, AI answers, or quiz results to view them here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    SavedItemCard(
                        item = item,
                        onClick = { selectedItemForDetail = item },
                        onDelete = {
                            coroutineScope.launch {
                                savedRepository.deleteItem(item.id)
                                Toast.makeText(context, "Deleted item", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    // DETAIL MODAL DIALOG
    if (selectedItemForDetail != null) {
        val item = selectedItemForDetail!!
        Dialog(onDismissRequest = { selectedItemForDetail = null }) {
            AppleCard(
                backgroundColor = MaterialTheme.colorScheme.surface,
                cornerRadius = 24.dp
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TagPill(text = item.category.name, isSelected = true)
                        IconButton(onClick = { selectedItemForDetail = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Divider(color = MaterialTheme.colorScheme.outline)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = item.content,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Button(
                        onClick = { selectedItemForDetail = null },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedItemCard(
    item: SavedItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val icon = when (item.category) {
        SavedCategory.PLANS -> Icons.Outlined.EventNote
        SavedCategory.ANSWERS -> Icons.Outlined.Lightbulb
        SavedCategory.QUIZZES -> Icons.Outlined.Quiz
        SavedCategory.ALL -> Icons.Outlined.Bookmark
    }

    AppleCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
