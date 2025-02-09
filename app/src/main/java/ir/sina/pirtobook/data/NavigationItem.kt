package ir.sina.pirtobook.data

import androidx.compose.ui.graphics.vector.ImageVector
import okhttp3.Route

data class NavigationItem(
    val name: String,
    val icon: ImageVector,
    val route: String
)
