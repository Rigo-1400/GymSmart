import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.composables.icons.lucide.Contact
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
        containerColor = Color(0xFFD0BCFF),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Lucide.House, "Home", tint = Color.Black) },
            label = { Text("Home", color = Color.Black) },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Lucide.Dumbbell, "Workouts", tint = Color.Black) },
            label = { Text("Workouts", color = Color.Black) },
            selected = currentRoute == "workouts",
            onClick = { navController.navigate("workouts") }
        )
        NavigationBarItem(
            icon = { Icon(Lucide.Contact, "Settings", tint = Color.Black) },
            label = { Text("Profile", color = Color.Black) },
            selected = currentRoute == "settings",
            onClick = { navController.navigate("settings") }
        )
    }
}
