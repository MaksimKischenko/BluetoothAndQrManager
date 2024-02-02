import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bt_dev.bottom_nav.BottomItem


@Composable
fun BottomNavigationContainer(
    navController: NavController
) {
    val items = listOf(
        BottomItem.BluetoothScreenItem,
        BottomItem.QRCamScreenItem,
    )
    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = "icon"
                    )
                },
                label = {
                    Text(text = item.title, fontSize = 9.sp)
                },
            )
        }
    }
}