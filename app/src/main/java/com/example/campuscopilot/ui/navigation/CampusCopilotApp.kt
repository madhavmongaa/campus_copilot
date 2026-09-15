package com.example.campuscopilot.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campuscopilot.data.repositories.LocalQuizRepository
import com.example.campuscopilot.data.repositories.LocalSavedRepository
import com.example.campuscopilot.data.repositories.LocalStudyPlanRepository
import com.example.campuscopilot.data.services.MockAIService
import com.example.campuscopilot.data.services.MockCampusRetriever
import com.example.campuscopilot.ui.screens.ask.AskScreen
import com.example.campuscopilot.ui.screens.home.HomeScreen
import com.example.campuscopilot.ui.screens.profile.ProfileScreen
import com.example.campuscopilot.ui.screens.saved.SavedScreen
import com.example.campuscopilot.ui.screens.study.StudyScreen

enum class NavDestination(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    ASK("ask", "Ask", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    STUDY("study", "Study", Icons.Filled.Book, Icons.Outlined.Book),
    SAVED("saved", "Saved", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
    PROFILE("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun CampusCopilotApp() {
    // Single shared instances of services & repositories
    val aiService = remember { MockAIService() }
    val campusRetriever = remember { MockCampusRetriever() }
    val savedRepository = remember { LocalSavedRepository() }
    val studyPlanRepository = remember { LocalStudyPlanRepository() }
    val quizRepository = remember { LocalQuizRepository() }

    var currentDestination by remember { mutableStateOf(NavDestination.HOME) }

    // Dynamic state parameters passed when navigating between screens
    var askInitialPrompt by remember { mutableStateOf<String?>(null) }
    var askCampusModeActive by remember { mutableStateOf(false) }
    var studyInitialTab by remember { mutableStateOf("PLAN") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavDestination.values().forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (destination != NavDestination.ASK) askInitialPrompt = null
                            currentDestination = destination
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = destination.title
                            )
                        },
                        label = {
                            Text(
                                text = destination.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                NavDestination.HOME -> HomeScreen(
                    onNavigateToAsk = { prompt ->
                        askInitialPrompt = prompt
                        askCampusModeActive = false
                        currentDestination = NavDestination.ASK
                    },
                    onNavigateToAskCampus = {
                        askInitialPrompt = null
                        askCampusModeActive = true
                        currentDestination = NavDestination.ASK
                    },
                    onNavigateToStudyTab = { tab ->
                        studyInitialTab = tab
                        currentDestination = NavDestination.STUDY
                    }
                )

                NavDestination.ASK -> AskScreen(
                    aiService = aiService,
                    campusRetriever = campusRetriever,
                    savedRepository = savedRepository,
                    initialPrompt = askInitialPrompt,
                    initialCampusMode = askCampusModeActive
                )

                NavDestination.STUDY -> StudyScreen(
                    aiService = aiService,
                    studyPlanRepository = studyPlanRepository,
                    savedRepository = savedRepository,
                    quizRepository = quizRepository,
                    initialTab = studyInitialTab
                )

                NavDestination.SAVED -> SavedScreen(
                    savedRepository = savedRepository
                )

                NavDestination.PROFILE -> ProfileScreen(
                    savedRepository = savedRepository
                )
            }
        }
    }
}
