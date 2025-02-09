package ir.sina.pirtobook.ui.page.main

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.Color.Companion.White
import ir.sina.pirtobook.ui.theme.PirtobookTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.burnoo.cokoin.navigation.getNavController

import dev.burnoo.cokoin.viewmodel.getViewModel
import ir.sina.pirtobook.R
import ir.sina.pirtobook.data.Book
import ir.sina.pirtobook.data.NavigationItem
import ir.sina.pirtobook.ui.page.description.DescriptionScreen
import ir.sina.pirtobook.ui.theme.Dark
import ir.sina.pirtobook.ui.theme.Dark_Mode
import ir.sina.pirtobook.ui.theme.Font_Text
import ir.sina.pirtobook.ui.theme.My_Font
import ir.sina.pirtobook.ui.theme.Shapess
import ir.sina.pirtobook.util.MyScreens


@Composable
fun MainScreen() {
    val viewModel = getViewModel<MainViewModel>()
    val books = viewModel.books.collectAsState(emptyList())
    val navigation = getNavController()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Dark_Mode)
    ) {
        AppBars()
        NavBar(searchQuery) { newQuery -> searchQuery = newQuery }
        Title1()
        BooksList(books.value, searchQuery) { title ->
            navigation.navigate(MyScreens.DescriptionScreen.route + "/$title") {
                launchSingleTop = true
            }
        }
        Spacer(modifier = Modifier.height(167.dp))
    }
}
// ----------------------------------------------------------------------------------------------------

@Composable
fun AppBars() {

    TopAppBar(
        backgroundColor = Dark_Mode,
        title = {
            androidx.compose.material.Text(
                text = "PirtoBook",
                fontFamily = My_Font,
                color = White,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 14.3.dp)
            )
        },
        elevation = 0.dp,
        modifier = Modifier
            .padding(top = 37.dp)
            .fillMaxWidth()
    )

}

// ----------------------------------------------------------------------------------------------------

@Composable
fun NavBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 35.dp)
            .height(56.dp),
        colors = CardDefaults.cardColors(Dark),
        shape = Shapess.large,
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = White,
                modifier = Modifier.padding(start = 18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = White, fontSize = 18.sp)

            )
        }
    }
}

// ----------------------------------------------------------------------------------------------------

@Composable
fun Title1() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 48.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(text = "All Books", color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold ,  fontFamily = Font_Text)
    }

}


// ----------------------------------------------------------------------------------------------------
@Composable
fun BooksList(books: List<Book>, searchQuery: String, onClickCard: (String) -> Unit) {
    val shuffledBooks = books.shuffled()
    val filteredBooks = shuffledBooks.filter { it.title.contains(searchQuery, ignoreCase = true) }
    if (filteredBooks.isNotEmpty()) {
        val chunkedBooks = filteredBooks.chunked(5)
        Column {
            chunkedBooks.forEach { rowBooks ->
                LazyRow(
                    modifier = Modifier.padding(top = 20.dp),
                    contentPadding = PaddingValues(end = 16.dp, bottom = 20.dp)
                ) {
                    items(rowBooks.size) { index ->
                        Books(rowBooks[index]) {
                            onClickCard(it)
                        }
                    }
                }
            }
        }
    } else {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_found_ani))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Dark_Mode),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .size(265.dp)
                    .padding(top = 0.dp, bottom = 0.dp),
                iterations = LottieConstants.IterateForever
            )
        }
    }
}


@Composable
fun Books(book: Book, onClickCard: (String) -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .width(225.5.dp)
            .clickable { onClickCard.invoke(book.title) },
        colors = CardDefaults.cardColors(Dark),
        shape = Shapess.small,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(228.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.2.dp)
            ) {
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    fontFamily = Font_Text,
                    modifier = Modifier.padding(top = 5.dp)
                )
                Text(
                    text = book.author,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = White,
                    fontFamily = Font_Text
                )

                Spacer(modifier = Modifier.padding(bottom = 10.dp))


            }


        }
    }
}


// ----------------------------------------------------------------------------------------------------



