package com.example.campuscopilot.ui.screens.ask

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuscopilot.data.models.ChatMessage
import com.example.campuscopilot.data.models.SavedCategory
import com.example.campuscopilot.data.models.SavedItem
import com.example.campuscopilot.domain.repositories.SavedRepository
import com.example.campuscopilot.domain.services.AIService
import com.example.campuscopilot.domain.services.CampusRetriever
import com.example.campuscopilot.ui.components.AppleCard
import com.example.campuscopilot.ui.components.SourceChip
import com.example.campuscopilot.ui.components.TagPill
import com.example.campuscopilot.theme.PrimaryAccent
import kotlinx.coroutines.launch

@Composable
fun AskScreen(
    aiService: AIService,
    campusRetriever: CampusRetriever,
    savedRepository: SavedRepository,
    initialPrompt: String? = null,
    initialCampusMode: Boolean = false
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var isCampusMode by remember { mutableStateOf(initialCampusMode) }
    var inputText by remember { mutableStateOf(initialPrompt ?: "") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember { mutableStateListOf<ChatMessage>() }

    // Auto-send initial prompt if passed in
    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank() && messages.isEmpty()) {
            sendMessage(
                promptText = initialPrompt,
                isCampus = isCampusMode,
                aiService = aiService,
                retriever = campusRetriever,
                messages = messages,
                onLoadingState = { isLoading = it }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP HEADER & MODE TOGGLE
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isCampusMode) "Ask Campus" else "Ask AI",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = if (isCampusMode) "Answers grounded in your campus information." else "Your personal AI study assistant.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (messages.isNotEmpty()) {
                        IconButton(onClick = { messages.clear() }) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteSweep,
                                contentDescription = "Clear chat",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Selector Segmented Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (!isCampusMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { isCampusMode = false }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = if (!isCampusMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "General AI",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (!isCampusMode) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (!isCampusMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (isCampusMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { isCampusMode = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = if (isCampusMode) PrimaryAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ask Campus (RAG)",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isCampusMode) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isCampusMode) PrimaryAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // CHAT MESSAGE LIST OR EMPTY STATE
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty() && !isLoading) {
                // EMPTY STATE
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCampusMode) Icons.Default.AccountBalance else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isCampusMode) "Ask anything about Campus" else "How can I help?",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isCampusMode)
                                "Get answers verified by official campus documents."
                            else
                                "Ask for study plans, concepts, or practice problems.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(28.dp))

                        // SUGGESTED PROMPTS
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "SUGGESTED PROMPTS",
                                style = MaterialTheme.typography.labelSmall
                            )
                            val prompts = if (isCampusMode) listOf(
                                "Where is the gym?",
                                "What clubs can I join if I'm interested in technology?",
                                "What sports facilities are available?",
                                "What are the library timings?",
                                "What events are happening on campus?"
                            ) else listOf(
                                "Make me a study plan for Python",
                                "Explain recursion simply",
                                "Quiz me on loops & lists",
                                "How do I balance studying and exams?"
                            )

                            prompts.forEach { prompt ->
                                AppleCard(
                                    cornerRadius = 14.dp,
                                    onClick = {
                                        inputText = prompt
                                        sendMessage(
                                            promptText = prompt,
                                            isCampus = isCampusMode,
                                            aiService = aiService,
                                            retriever = campusRetriever,
                                            messages = messages,
                                            onLoadingState = { isLoading = it }
                                        )
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = prompt,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.NorthEast,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // MESSAGES LIST
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(messages) { message ->
                        ChatMessageBubble(
                            message = message,
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Campus Copilot", message.text)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onSave = {
                                coroutineScope.launch {
                                    savedRepository.saveItem(
                                        SavedItem(
                                            title = if (message.isCampusQuery) "Campus Answer" else "AI Answer",
                                            subtitle = if (message.isCampusQuery) "Grounded Campus Info" else "General AI Chat",
                                            category = SavedCategory.ANSWERS,
                                            content = message.text
                                        )
                                    )
                                    Toast.makeText(context, "Saved to Saved items", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onRegenerate = {
                                val lastUserMsg = messages.lastOrNull { it.isUser }
                                if (lastUserMsg != null) {
                                    sendMessage(
                                        promptText = lastUserMsg.text,
                                        isCampus = isCampusMode,
                                        aiService = aiService,
                                        retriever = campusRetriever,
                                        messages = messages,
                                        onLoadingState = { isLoading = it }
                                    )
                                }
                            }
                        )
                    }

                    if (isLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = PrimaryAccent
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (isCampusMode) "Searching campus knowledge base..." else "Thinking...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // INPUT FIELD BOTTOM BAR
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = if (isCampusMode) "Ask campus question..." else "Ask anything...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (inputText.isNotBlank()) {
                            val promptToSend = inputText
                            inputText = ""
                            keyboardController?.hide()
                            sendMessage(
                                promptText = promptToSend,
                                isCampus = isCampusMode,
                                aiService = aiService,
                                retriever = campusRetriever,
                                messages = messages,
                                onLoadingState = { isLoading = it }
                            )
                        }
                    })
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val promptToSend = inputText
                            inputText = ""
                            keyboardController?.hide()
                            sendMessage(
                                promptText = promptToSend,
                                isCampus = isCampusMode,
                                aiService = aiService,
                                retriever = campusRetriever,
                                messages = messages,
                                onLoadingState = { isLoading = it }
                            )
                        }
                    },
                    enabled = inputText.isNotBlank() && !isLoading,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank() && !isLoading)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank() && !isLoading)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun sendMessage(
    promptText: String,
    isCampus: Boolean,
    aiService: AIService,
    retriever: CampusRetriever,
    messages: MutableList<ChatMessage>,
    onLoadingState: (Boolean) -> Unit
) {
    val userMsg = ChatMessage(text = promptText, isUser = true)
    messages.add(userMsg)
    onLoadingState(true)

    kotlinx.coroutines.GlobalScope.launch {
        try {
            val responseMsg = if (isCampus) {
                val docs = retriever.retrieveRelevantDocuments(promptText)
                aiService.answerCampusQuestion(promptText, docs)
            } else {
                aiService.chat(promptText, messages)
            }
            messages.add(responseMsg)
        } catch (e: Exception) {
            messages.add(
                ChatMessage(
                    text = "Sorry, an error occurred while processing your request.",
                    isUser = false
                )
            )
        } finally {
            onLoadingState(false)
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    onCopy: () -> Unit,
    onSave: () -> Unit,
    onRegenerate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        if (message.isUser) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (message.isCampusQuery) PrimaryAccent else MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (message.isCampusQuery) Icons.Default.AccountBalance else Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (message.isCampusQuery) "Campus Copilot (Grounded)" else "Campus Copilot",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // SOURCE CHIPS (RAG)
                if (message.sources.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "SOURCES",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            message.sources.forEach { sourceTitle ->
                                SourceChip(title = sourceTitle)
                            }
                        }
                    }
                }

                // CHAT ACTIONS ROW
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copy",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onSave, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkAdd,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onRegenerate, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Regenerate",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
