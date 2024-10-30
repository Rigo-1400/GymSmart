import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.composables.icons.lucide.Dumbbell
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = Color(0xFF1c1c1c),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Lucide.House, "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Lucide.Dumbbell, "Workouts") },
            label = { Text("Workouts") },
            selected = currentRoute == "workouts",
            onClick = { navController.navigate("workouts") }
        )
        NavigationBarItem(
            icon = { Icon(Lucide.Settings, "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == "settings",
            onClick = { navController.navigate("settings") }
        )
    }
}
