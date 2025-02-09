package ir.sina.pirtobook

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dev.burnoo.cokoin.navigation.KoinNavHost
import ir.sina.pirtobook.data.NavigationItem
import ir.sina.pirtobook.di.myModules
import ir.sina.pirtobook.ui.page.description.DescriptionScreen
import ir.sina.pirtobook.ui.page.main.MainScreen
import ir.sina.pirtobook.ui.page.article.NewsScreen
import ir.sina.pirtobook.ui.page.article.details.ArticleDetailsScreen
import ir.sina.pirtobook.ui.page.profile.ProfileScreen
import ir.sina.pirtobook.ui.page.save.SaveScreen
import ir.sina.pirtobook.ui.page.save.details.NewsDetailsScreen
import ir.sina.pirtobook.ui.page.save.treding.TredingDetailsScreen
import ir.sina.pirtobook.ui.theme.Dark2
import ir.sina.pirtobook.ui.theme.PirtobookTheme
import ir.sina.pirtobook.ui.theme.Shapess
import ir.sina.pirtobook.util.MyScreens
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        createNotificationChannel(this)
        requestPermissions()
        Firebase.initialize(this)
        enableEdgeToEdge()
        startKoin {
            androidContext(this@MainActivity)
            modules(myModules)
            setContent {
                PirtobookTheme {
                    UiScreen()
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissions.toTypedArray())
        }
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if (!it.value) {
                    // Handle permission denial
                }
            }
        }


}

@Composable
fun UiScreen() {
    val navController = rememberNavController()

    Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {

        KoinNavHost(
            startDestination = MyScreens.MainScreen.route,
            navController = navController
        ) {
            composable(MyScreens.MainScreen.route) {
                MainScreen()
            }
            composable(MyScreens.SaveScreen.route) {
                SaveScreen()
            }
            composable(MyScreens.ProfileScreen.route) {
                ProfileScreen()
            }
            composable(
                route = MyScreens.DescriptionScreen.route + "/{bookTitle}",
                arguments = listOf(
                    navArgument("bookTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val bookTitle = remember { backStackEntry.arguments?.getString("bookTitle") }
                DescriptionScreen(bookTitle = bookTitle)
            }
            composable(MyScreens.NewsScreen.route) {
                NewsScreen()
            }

            composable(
                route = MyScreens.ArticleDec.route + "/{articleTitle}",
                arguments = listOf(
                    navArgument("articleTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val articleTitle = remember { backStackEntry.arguments?.getString("articleTitle") }
                ArticleDetailsScreen(articleTitle = articleTitle)
            }


            composable(
                route = MyScreens.NewsDec.route + "/{newsTitle}",
                arguments = listOf(
                    navArgument("newsTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val newsTitle = remember { backStackEntry.arguments?.getString("newsTitle") }
                NewsDetailsScreen(newsTitle = newsTitle)
            }

            composable(
                route = MyScreens.TredingDetailsScreen.route + "/{tredingTitle}",
                arguments = listOf(
                    navArgument("tredingTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val tredingTitle = remember { backStackEntry.arguments?.getString("tredingTitle") }
                TredingDetailsScreen(tredingTitle = tredingTitle)
            }

        }

        Card(
            modifier = androidx.compose.ui.Modifier
                .padding(
                    bottom = 60.dp,
                    start = 30.dp,
                    end = 30.dp
                ).height(67.dp)
                .align(Alignment.BottomCenter),
            shape = Shapess.large,
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Dark2)
        ) {
            Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize().background(Dark2)
            ) {
                BottomNavigationBar(navController = navController)
            }
        }
    }
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem("Home", Icons.Default.Home, MyScreens.MainScreen.route),
        NavigationItem("News", Icons.Default.Create, MyScreens.NewsScreen.route),
        NavigationItem("Saved", Icons.Default.DateRange, MyScreens.SaveScreen.route),
        NavigationItem("Profile", Icons.Default.Person, MyScreens.ProfileScreen.route)

    )

    BottomNavigation(
        backgroundColor = Dark2,
        contentColor = Color.White,
        elevation = 0.dp,
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    ) {
        val currentRoute = navController.currentBackStackEntryFlow
            .collectAsState(initial = navController.currentBackStackEntry).value?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name
                    )
                },
                label = null,
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = false
            )
        }
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "download_channel",
            "دانلود فایل",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "نمایش پیشرفت دانلود فایل"
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}



