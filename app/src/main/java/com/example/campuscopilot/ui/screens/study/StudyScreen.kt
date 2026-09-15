package com.example.campuscopilot.ui.screens.study

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
import androidx.compose.ui.unit.sp
import com.example.campuscopilot.data.models.*
import com.example.campuscopilot.domain.repositories.QuizRepository
import com.example.campuscopilot.domain.repositories.SavedRepository
import com.example.campuscopilot.domain.repositories.StudyPlanRepository
import com.example.campuscopilot.domain.services.AIService
import com.example.campuscopilot.ui.components.*
import com.example.campuscopilot.theme.AccentGreen
import com.example.campuscopilot.theme.PrimaryAccent
import kotlinx.coroutines.launch

@Composable
fun StudyScreen(
    aiService: AIService,
    studyPlanRepository: StudyPlanRepository,
    savedRepository: SavedRepository,
    quizRepository: QuizRepository,
    initialTab: String = "PLAN"
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(initialTab) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP HEADER & TAB BAR
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Study",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Three large top cards / tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StudyTabCard(
                        title = "Study Plan",
                        icon = Icons.Outlined.EventNote,
                        isSelected = selectedTab == "PLAN",
                        onClick = { selectedTab = "PLAN" },
                        modifier = Modifier.weight(1f)
                    )
                    StudyTabCard(
                        title = "Explain",
                        icon = Icons.Outlined.Lightbulb,
                        isSelected = selectedTab == "EXPLAIN",
                        onClick = { selectedTab = "EXPLAIN" },
                        modifier = Modifier.weight(1f)
                    )
                    StudyTabCard(
                        title = "Quiz Me",
                        icon = Icons.Outlined.Quiz,
                        isSelected = selectedTab == "QUIZ",
                        onClick = { selectedTab = "QUIZ" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // TAB CONTENT
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedTab) {
                "PLAN" -> StudyPlanView(
                    aiService = aiService,
                    studyPlanRepository = studyPlanRepository,
                    savedRepository = savedRepository
                )
                "EXPLAIN" -> ExplainView(
                    aiService = aiService,
                    savedRepository = savedRepository
                )
                "QUIZ" -> QuizMeView(
                    aiService = aiService,
                    quizRepository = quizRepository,
                    savedRepository = savedRepository
                )
            }
        }
    }
}

@Composable
private fun StudyTabCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val fg = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = fg
        )
    }
}

// ----------------------------------------------------
// 1. STUDY PLAN VIEW
// ----------------------------------------------------
@Composable
private fun StudyPlanView(
    aiService: AIService,
    studyPlanRepository: StudyPlanRepository,
    savedRepository: SavedRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var subject by remember { mutableStateOf("Python") }
    var topic by remember { mutableStateOf("Loops & Lists") }
    var availableTime by remember { mutableStateOf("2 hours") }
    var deadline by remember { mutableStateOf("Tomorrow") }
    var difficulty by remember { mutableStateOf("Beginner") }

    var isLoading by remember { mutableStateOf(false) }
    var generatedPlan by remember { mutableStateOf<StudyPlan?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            SectionHeader(
                title = "Create Study Plan",
                subtitle = "Generate an optimized step-by-step learning roadmap."
            )
        }

        // FORM INPUTS
        item {
            AppleCard(cornerRadius = 18.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Subject") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = topic,
                            onValueChange = { topic = it },
                            label = { Text("Topic") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = availableTime,
                            onValueChange = { availableTime = it },
                            label = { Text("Available Time") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = deadline,
                            onValueChange = { deadline = it },
                            label = { Text("Deadline") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Text(
                        text = "Difficulty Level",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach { diff ->
                            TagPill(
                                text = diff,
                                isSelected = difficulty == diff,
                                onClick = { difficulty = diff }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    ApplePrimaryButton(
                        text = "Create Plan",
                        icon = Icons.Default.AutoAwesome,
                        isLoading = isLoading,
                        onClick = {
                            isLoading = true
                            coroutineScope.launch {
                                val plan = aiService.generateStudyPlan(
                                    subject = subject,
                                    topic = topic,
                                    availableTime = availableTime,
                                    deadline = deadline,
                                    difficulty = difficulty
                                )
                                generatedPlan = plan
                                studyPlanRepository.savePlan(plan)
                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // GENERATED PLAN RESULT
        if (generatedPlan != null) {
            val plan = generatedPlan!!
            item {
                AppleCard(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.primary,
                    cornerRadius = 20.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = plan.title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${plan.subject} • ${plan.availableTime} • ${plan.deadline}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TagPill(text = plan.priority, isSelected = true)
                        }

                        Divider(color = MaterialTheme.colorScheme.outline)

                        Text(
                            text = "TIME BLOCKS SCHEDULE",
                            style = MaterialTheme.typography.labelSmall
                        )

                        // Checkable Study Blocks
                        plan.blocks.forEachIndexed { index, block ->
                            var isChecked by remember { mutableStateOf(block.isCompleted) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        isChecked = !isChecked
                                        coroutineScope.launch {
                                            studyPlanRepository.toggleBlockCompletion(plan.id, block.id)
                                        }
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        isChecked = checked
                                        coroutineScope.launch {
                                            studyPlanRepository.toggleBlockCompletion(plan.id, block.id)
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = block.timeRange,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryAccent
                                    )
                                    Text(
                                        text = block.activity,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        savedRepository.saveItem(
                                            SavedItem(
                                                title = plan.title,
                                                subtitle = "Study Plan • ${plan.availableTime}",
                                                category = SavedCategory.PLANS,
                                                content = plan.blocks.joinToString("\n") { "${it.timeRange} — ${it.activity}" }
                                            )
                                        )
                                        Toast.makeText(context, "Saved plan to Saved items!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Outlined.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save Plan")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 2. EXPLAIN VIEW
// ----------------------------------------------------
@Composable
private fun ExplainView(
    aiService: AIService,
    savedRepository: SavedRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var conceptText by remember { mutableStateOf("Recursion & StackOverflow") }
    var selectedDifficulty by remember { mutableStateOf("Beginner") }
    var isLoading by remember { mutableStateOf(false) }
    var explanationResult by remember { mutableStateOf<ConceptExplanation?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            SectionHeader(
                title = "Explain Concept",
                subtitle = "Break down complex topics into intuitive breakdowns."
            )
        }

        item {
            AppleCard(cornerRadius = 18.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = conceptText,
                        onValueChange = { conceptText = it },
                        label = { Text("What do you want explained?") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text(text = "Target Level", style = MaterialTheme.typography.labelSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach { diff ->
                            TagPill(
                                text = diff,
                                isSelected = selectedDifficulty == diff,
                                onClick = { selectedDifficulty = diff }
                            )
                        }
                    }

                    ApplePrimaryButton(
                        text = "Explain Concept",
                        icon = Icons.Default.Lightbulb,
                        isLoading = isLoading,
                        onClick = {
                            isLoading = true
                            coroutineScope.launch {
                                explanationResult = aiService.explainConcept(conceptText, selectedDifficulty)
                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (explanationResult != null) {
            val exp = explanationResult!!
            item {
                AppleCard(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.primary,
                    cornerRadius = 20.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exp.concept,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            TagPill(text = exp.difficulty, isSelected = true)
                        }

                        Divider(color = MaterialTheme.colorScheme.outline)

                        ExplainSection(title = "SIMPLE EXPLANATION", body = exp.simpleExplanation)
                        ExplainSection(title = "EXAMPLE / CODE", body = exp.example, isCode = true)
                        ExplainSection(title = "COMMON MISTAKES TO AVOID", body = exp.commonMistakes)
                        ExplainSection(title = "QUICK SELF-TEST QUESTION", body = exp.quickTestQuestion)

                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    savedRepository.saveItem(
                                        SavedItem(
                                            title = "Concept: ${exp.concept}",
                                            subtitle = "Explain • ${exp.difficulty}",
                                            category = SavedCategory.ANSWERS,
                                            content = exp.simpleExplanation
                                        )
                                    )
                                    Toast.makeText(context, "Explanation saved!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Outlined.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Explanation")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExplainSection(
    title: String,
    body: String,
    isCode: Boolean = false
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(4.dp))
        if (isCode) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp)
            ) {
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        } else {
            Text(text = body, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// ----------------------------------------------------
// 3. QUIZ ME VIEW (Interactive Quiz Runner)
// ----------------------------------------------------
@Composable
private fun QuizMeView(
    aiService: AIService,
    quizRepository: QuizRepository,
    savedRepository: SavedRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var topic by remember { mutableStateOf("Loops & Lists") }
    var numQuestions by remember { mutableStateOf(4) }
    var difficulty by remember { mutableStateOf("Beginner") }

    var isLoading by remember { mutableStateOf(false) }
    var activeQuiz by remember { mutableStateOf<Quiz?>(null) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var userAnswers by remember { mutableStateOf(mutableListOf<UserAnswer>()) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            SectionHeader(
                title = "Quiz Me",
                subtitle = "Generate custom quizzes and test your recall."
            )
        }

        if (activeQuiz == null && !isQuizCompleted) {
            // SETUP FORM
            item {
                AppleCard(cornerRadius = 18.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        OutlinedTextField(
                            value = topic,
                            onValueChange = { topic = it },
                            label = { Text("Quiz Topic") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Text(text = "Difficulty", style = MaterialTheme.typography.labelSmall)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Beginner", "Intermediate", "Advanced").forEach { diff ->
                                TagPill(
                                    text = diff,
                                    isSelected = difficulty == diff,
                                    onClick = { difficulty = diff }
                                )
                            }
                        }

                        ApplePrimaryButton(
                            text = "Start Quiz",
                            icon = Icons.Default.PlayArrow,
                            isLoading = isLoading,
                            onClick = {
                                isLoading = true
                                coroutineScope.launch {
                                    val quiz = aiService.generateQuiz(topic, numQuestions, difficulty)
                                    activeQuiz = quiz
                                    currentQuestionIndex = 0
                                    selectedOption = null
                                    userAnswers = mutableListOf()
                                    isQuizCompleted = false
                                    isLoading = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else if (activeQuiz != null && !isQuizCompleted) {
            // ACTIVE QUESTION RUNNER (One question at a time)
            val quiz = activeQuiz!!
            val q = quiz.questions[currentQuestionIndex]

            item {
                AppleCard(cornerRadius = 20.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question ${currentQuestionIndex + 1} of ${quiz.questions.size}",
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryAccent,
                                fontWeight = FontWeight.Bold
                            )
                            TagPill(text = quiz.topic, isSelected = false)
                        }

                        Text(
                            text = q.question,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // OPTIONS LIST
                        q.options.forEachIndexed { optionIndex, optionText ->
                            val isSelected = selectedOption == optionIndex
                            val bg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            val border = if (isSelected) PrimaryAccent else MaterialTheme.colorScheme.outline

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bg)
                                    .clickable { selectedOption = optionIndex }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedOption = optionIndex }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        ApplePrimaryButton(
                            text = if (currentQuestionIndex == quiz.questions.size - 1) "Finish Quiz" else "Next Question",
                            enabled = selectedOption != null,
                            onClick = {
                                val sel = selectedOption ?: 0
                                userAnswers.add(
                                    UserAnswer(
                                        questionId = q.id,
                                        selectedOptionIndex = sel,
                                        isCorrect = sel == q.correctOptionIndex
                                    )
                                )

                                if (currentQuestionIndex < quiz.questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedOption = null
                                } else {
                                    // Complete quiz
                                    val correctCount = userAnswers.count { it.isCorrect }
                                    val result = QuizResult(
                                        topic = quiz.topic,
                                        difficulty = quiz.difficulty,
                                        score = correctCount,
                                        totalQuestions = quiz.questions.size,
                                        userAnswers = userAnswers
                                    )
                                    coroutineScope.launch {
                                        quizRepository.saveQuizResult(result)
                                    }
                                    isQuizCompleted = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else if (isQuizCompleted && activeQuiz != null) {
            // VISUALLY SATISFYING QUIZ RESULTS SCREEN
            val quiz = activeQuiz!!
            val correctCount = userAnswers.count { it.isCorrect }
            val percentage = (correctCount * 100) / quiz.questions.size

            item {
                AppleCard(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderColor = AccentGreen,
                    cornerRadius = 24.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(AccentGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = AccentGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = "Quiz Complete!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "$percentage% Score",
                            style = MaterialTheme.typography.displayLarge,
                            color = AccentGreen,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "$correctCount of ${quiz.questions.size} questions answered correctly",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Divider(color = MaterialTheme.colorScheme.outline)

                        // QUESTION BREAKDOWN
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "QUESTION BREAKDOWN",
                                style = MaterialTheme.typography.labelSmall
                            )

                            quiz.questions.forEachIndexed { idx, question ->
                                val ans = userAnswers.getOrNull(idx)
                                val isRight = ans?.isCorrect == true

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = if (isRight) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if (isRight) AccentGreen else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = question.question,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = question.explanation,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        savedRepository.saveItem(
                                            SavedItem(
                                                title = "${quiz.topic} Quiz Result",
                                                subtitle = "Quiz • Score: $percentage%",
                                                category = SavedCategory.QUIZZES,
                                                content = "Score: $correctCount/${quiz.questions.size} ($percentage%)\nTopic: ${quiz.topic}"
                                            )
                                        )
                                        Toast.makeText(context, "Quiz result saved!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save Result")
                            }

                            Button(
                                onClick = {
                                    activeQuiz = null
                                    isQuizCompleted = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Take Another")
                            }
                        }
                    }
                }
            }
        }
    }
}
