package cl.duoc.pichangapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.duoc.pichangapp.ui.screens.auth.LoginScreen
import cl.duoc.pichangapp.ui.screens.auth.RegisterScreen
import cl.duoc.pichangapp.ui.screens.home.HomeScreen
import cl.duoc.pichangapp.ui.screens.karma.KarmaScreen
import cl.duoc.pichangapp.ui.screens.notifications.NotificationsScreen
import cl.duoc.pichangapp.ui.screens.profile.ProfileScreen
import cl.duoc.pichangapp.ui.screens.splash.SplashScreen

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home", "Inicio", Icons.Filled.Home)
    object Karma : Screen("karma", "Karma", Icons.Filled.Star)
    object Notifications : Screen("notifications", "Notificaciones", Icons.Filled.Notifications)
    object Profile : Screen("profile", "Perfil", Icons.Filled.Person)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Karma,
    Screen.Notifications,
    Screen.Profile
)

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                            label = { screen.title?.let { Text(it) } },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Login.route + "?mensaje={mensaje}") { backStackEntry ->
                val mensaje = backStackEntry.arguments?.getString("mensaje")
                LoginScreen(
                    mensaje = mensaje,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = { correo ->
                        navController.navigate("verify-code/$correo") {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                )
            }
            composable("verify-code/{email}") { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                cl.duoc.pichangapp.ui.screens.auth.VerifyCodeScreen(
                    email = email,
                    onVerifySuccess = {
                        navController.navigate(Screen.Login.route + "?mensaje=Cuenta verificada, inicia sesión") {
                            popUpTo("verify-code/{email}") { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Karma.route) {
                KarmaScreen(navController = navController)
            }
            composable(Screen.Notifications.route) {
                NotificationsScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
