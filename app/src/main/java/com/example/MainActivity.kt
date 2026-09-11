package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.DisasterViewModel
import com.example.ui.components.AskSurakshaFab
import com.example.ui.components.EmergencySosDialog
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.LocationSubHeader
import com.example.ui.components.ManualLocationDialog
import com.example.ui.components.SurakshaBottomNav
import com.example.ui.components.SurakshaHeader
import com.example.ui.screens.ActiveAlertsScreen
import com.example.ui.screens.AskSurakshaScreen
import com.example.ui.screens.CitizenHomeScreen
import com.example.ui.screens.DisasterMapScreen
import com.example.ui.screens.IncidentReportScreen
import com.example.ui.screens.OnboardingRoleScreen
import com.example.ui.screens.OrgDashboardScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SurakshaApp()
            }
        }
    }
}

@Composable
fun SurakshaApp(
    viewModel: DisasterViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "onboarding"

    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val currentCoordinates by viewModel.currentCoordinates.collectAsState()
    val isGpsRefreshing by viewModel.isGpsRefreshing.collectAsState()
    val showSosDialog by viewModel.showSosDialog.collectAsState()
    val showLocationDialog by viewModel.showLocationDialog.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initPreparednessDatabase(context)
    }

    val isChatRoute = currentRoute == "ask-suraksha"
    val isOrgRoute = currentRoute == "org-dashboard"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!isChatRoute && !isOrgRoute) {
                androidx.compose.foundation.layout.Column {
                    SurakshaHeader(
                        currentLanguage = currentLanguage,
                        onLanguageClick = { showLanguageDialog = true },
                        onSosClick = { viewModel.toggleSosDialog(true) }
                    )
                    val context = androidx.compose.ui.platform.LocalContext.current
                    LocationSubHeader(
                        currentLocation = currentLocation,
                        isRefreshing = isGpsRefreshing,
                        onRefreshClick = { viewModel.fetchLiveCoordinates(context) },
                        onLocationClick = { viewModel.toggleLocationDialog(true) }
                    )
                }
            }
        },
        bottomBar = {
            if (!isChatRoute && !isOrgRoute) {
                SurakshaBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (!isChatRoute && !isOrgRoute) {
                AskSurakshaFab(
                    onClick = { navController.navigate("ask-suraksha") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Screen 1: Welcome & Role Selection (Node: 3a131fd360ba4a30bbea7578baad7ae4)
            composable("onboarding") {
                OnboardingRoleScreen(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        navController.navigate("home-feed") {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToOrg = {
                        navController.navigate("org-dashboard") {
                            launchSingleTop = true
                        }
                    },
                    onOpenLocationPicker = { viewModel.toggleLocationDialog(true) }
                )
            }

            // Screen 2: Citizen Home Feed
            composable("home-feed") {
                CitizenHomeScreen(
                    viewModel = viewModel,
                    onNavigateToMap = { navController.navigate("disaster-map") },
                    onNavigateToReport = { navController.navigate("incident-report") },
                    onNavigateToAlerts = { navController.navigate("active-alerts") },
                    onNavigateToPlaces = { navController.navigate("safe-places") },
                    onNavigateToAsk = { navController.navigate("ask-suraksha") }
                )
            }

            // Screen 3: Interactive Geospatial Hazard Map
            composable("disaster-map") {
                DisasterMapScreen(
                    viewModel = viewModel,
                    onNavigateToReport = { navController.navigate("incident-report") }
                )
            }

            // Screen 4: Safe Places & Relief Shelters
            composable("safe-places") {
                DisasterMapScreen(
                    viewModel = viewModel,
                    onNavigateToReport = { navController.navigate("incident-report") }
                )
            }

            // Screen 5: Hazard Reporting
            composable("incident-report") {
                IncidentReportScreen(
                    viewModel = viewModel,
                    onNavigateBackToHome = { navController.navigate("home-feed") }
                )
            }

            // Screen 6: Active Verified Bulletins & Global Feeds
            composable("active-alerts") {
                ActiveAlertsScreen(
                    viewModel = viewModel
                )
            }

            // Screen 7: Ask Suraksha AI Assistant
            composable("ask-suraksha") {
                AskSurakshaScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Screen 8: Organization & Civic Responder Dashboard
            composable("org-dashboard") {
                OrgDashboardScreen(
                    viewModel = viewModel,
                    onSwitchToCitizen = {
                        navController.navigate("home-feed") {
                            popUpTo("onboarding") { inclusive = false }
                        }
                    }
                )
            }
        }
    }

    // Emergency SOS Modal Dialog
    if (showSosDialog) {
        EmergencySosDialog(
            contacts = viewModel.emergencyContacts,
            onDismiss = { viewModel.toggleSosDialog(false) },
            currentLocation = currentCoordinates.address ?: currentLocation,
            latitude = currentCoordinates.latitude,
            longitude = currentCoordinates.longitude
        )
    }

    // Language Selection Modal Dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { viewModel.selectLanguage(it) },
            onDismiss = { showLanguageDialog = false }
        )
    }

    // Location Selection Dialog
    if (showLocationDialog) {
        ManualLocationDialog(
            currentLocation = currentLocation,
            onLocationChosen = { viewModel.setLocationManually(it) },
            onDismiss = { viewModel.toggleLocationDialog(false) }
        )
    }
}
