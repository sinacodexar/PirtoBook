package ir.sina.pirtobook.ui.page.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import ir.sina.pirtobook.R
import ir.sina.pirtobook.data.Book
import ir.sina.pirtobook.ui.page.main.Books
import ir.sina.pirtobook.ui.page.main.BooksList
import ir.sina.pirtobook.ui.page.main.MainViewModel
import ir.sina.pirtobook.ui.theme.Dark
import ir.sina.pirtobook.ui.theme.Dark_Mode
import ir.sina.pirtobook.ui.theme.Font_Text
import ir.sina.pirtobook.ui.theme.My_Font
import ir.sina.pirtobook.ui.theme.Shapess
import ir.sina.pirtobook.ui.theme.White
import ir.sina.pirtobook.util.MyScreens
import org.xmlpull.v1.sax2.Driver


@Composable
fun ProfileScreen() {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Dark_Mode)
    ) {
        val viewModel = getViewModel<MainViewModel>()
        val books = viewModel.books.collectAsState(emptyList())
        val navigation = getNavController()
        var searchQuery by remember { mutableStateOf("") }

        AppBar(onClickBack = { navigation.popBackStack() }) {
            navigation.navigate( MyScreens.NewsScreen.route )
        }

        Profile_Text {
            navigation.navigate( MyScreens.MainScreen.route )
        }

        NavBar(searchQuery) { newQuery -> searchQuery = newQuery }

        ListBooks(books.value, searchQuery) { title ->
            navigation.navigate(MyScreens.DescriptionScreen.route + "/$title") {
                launchSingleTop = true
            }
        }
        Spacer(modifier = Modifier.height(167.dp))

    }

}

@Composable
fun AppBar(onClickBack: () -> Unit, onClickArticle: () -> Unit) {

    TopAppBar(
        title = {
            Text(
                text = "Your Profile",
                color = White,
                fontWeight = FontWeight.Medium,
                fontFamily = My_Font,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { onClickBack.invoke() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    tint = White,
                    modifier = Modifier.size(36.dp),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickArticle.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Create,
                    tint = White,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.padding(top = 24.dp),
        elevation = 0.dp,
        backgroundColor = Dark_Mode

    )

}

@Composable
fun NavBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp, top = 16.dp, bottom = 19.dp)
            .height(50.dp),
        colors = CardDefaults.cardColors(Dark),
        shape = Shapess.large,
        elevation = CardDefaults.cardElevation(5.dp)
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
                tint = Companion.White,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Companion.White,
                    fontSize = 18.sp
                )

            )
        }
    }
}

@Composable
fun Profile_Text(onClickBook: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(395.dp)
            .background(Dark_Mode),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile_ani))
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .size(235.dp)
                .padding(top = 0.dp, bottom = 0.dp),
            iterations = LottieConstants.IterateForever
        )

        Text(
            text = "Hello My Friend 😉",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp),
            color = White,
            fontSize = 27.sp,
            fontFamily = Font_Text
        )

        Button(
            onClick = { onClickBook.invoke() },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(Dark),
            shape = Shapess.large,
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Text(text = "Books", color = White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 20.dp, bottom = 5.dp, start = 16.dp, end = 16.dp)
        )

    }

}

@Composable
fun ListBooks(data: List<Book>, searchQuery: String, onClickCard: (String) -> Unit) {
    val shuffledBooks = data.shuffled()
    val filteredBooks = shuffledBooks.filter { it.title.contains(searchQuery, ignoreCase = true) }
    if (filteredBooks.isNotEmpty()) {
        val chunkedBooks = filteredBooks.chunked(2)
        Column {
            chunkedBooks.forEach { rowBooks ->
                LazyRow(
                    modifier = Modifier.padding(top = 20.dp),
                    contentPadding = PaddingValues(end = 16.dp, bottom = 20.dp)
                ) {
                    items(rowBooks.size) { index ->
                        BookCard(rowBooks[index]) {
                            onClickCard(it)
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Dark_Mode),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_found_ani))
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
fun BookCard(book: Book, onClickCard: (String) -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .width(180.5.dp)
            .clickable { onClickCard.invoke(book.title) },
        colors = CardDefaults.cardColors(Dark_Mode),
        shape = Shapess.small,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(200.5.dp)
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
                androidx.compose.material3.Text(
                    text = book.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Companion.White,
                    fontFamily = Font_Text,
                    modifier = Modifier.padding(top = 5.dp)
                )
                androidx.compose.material3.Text(
                    text = book.author,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Companion.White,
                    fontFamily = Font_Text
                )

                Spacer(modifier = Modifier.padding(bottom = 10.dp))


            }


        }
    }
}