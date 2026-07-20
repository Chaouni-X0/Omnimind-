package com.example.omnimind.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.omnimind.presentation.screens.ApiCenterScreen
import com.example.omnimind.presentation.screens.ChatScreen
import com.example.omnimind.presentation.screens.CodeEditorScreen
import com.example.omnimind.presentation.screens.DashboardScreen
import com.example.omnimind.presentation.screens.GitHubScreen
import com.example.omnimind.presentation.screens.OnboardingScreen
import com.example.omnimind.presentation.screens.ProjectWorkspaceScreen
import com.example.omnimind.presentation.screens.TerminalScreen
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun OmniMindNavigation() {
    val viewModel: OmniMindViewModel = viewModel()
    val navController = rememberNavController()

    val onboardingComplete by viewModel.onboardingComplete.collectAsState()
    val startDestination = if (onboardingComplete) "dashboard" else "onboarding"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            val selectedTheme by viewModel.selectedTheme.collectAsState()
            OnboardingScreen(
                selectedTheme = selectedTheme,
                onThemeSelected = { viewModel.selectTheme(it) },
                onComplete = { apiKey, budget ->
                    viewModel.completeOnboarding(apiKey, budget)
                    navController.navigate("dashboard") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            val projects by viewModel.projects.collectAsState()
            DashboardScreen(
                projects = projects,
                onCreateProject = { name -> viewModel.createProject(name) },
                onOpenProject = { project ->
                    val encodedName = URLEncoder.encode(project.name, "UTF-8")
                    navController.navigate("workspace/${project.id}/$encodedName")
                },
                onOpenGitHub = { navController.navigate("github") },
                onOpenApiCenter = { navController.navigate("api-center") }
            )
        }

        composable(
            route = "workspace/{projectId}/{projectName}",
            arguments = listOf(
                navArgument("projectId") { type = NavType.StringType },
                navArgument("projectName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            val encodedName = backStackEntry.arguments?.getString("projectName") ?: ""
            val projectName = remember(encodedName) { URLDecoder.decode(encodedName, "UTF-8") }
            ProjectWorkspaceScreen(
                projectName = projectName,
                onOpenTerminal = { navController.navigate("terminal/$projectId") },
                onOpenEditor = { navController.navigate("editor/$projectId") },
                onOpenChat = { navController.navigate("chat/$projectId") }
            )
        }

        composable(
            route = "terminal/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            val terminalService = remember(projectId) { viewModel.terminalServiceFor(projectId) }
            TerminalScreen(terminalService = terminalService)
        }

        composable(
            route = "editor/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            val editorService = remember(projectId) { viewModel.codeEditorServiceFor(projectId) }
            CodeEditorScreen(editorService = editorService)
        }

        composable(
            route = "chat/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            val activeTaskId by viewModel.activeTaskId.collectAsState()
            val messagesFlow = remember(activeTaskId) {
                activeTaskId?.let { viewModel.messagesForTask(it) }
                    ?: kotlinx.coroutines.flow.MutableStateFlow(emptyList<com.example.omnimind.data.model.AgentMessage>())
            }
            val messages by messagesFlow.collectAsState()
            ChatScreen(
                messages = messages,
                onSendTask = { title, description ->
                    viewModel.startTask(projectId, title, description)
                }
            )
        }

        composable("github") {
            GitHubScreen(viewModel = viewModel)
        }

        composable("api-center") {
            ApiCenterScreen(viewModel = viewModel)
        }
    }
}
