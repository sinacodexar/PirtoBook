package ir.sina.pirtobook.ui.page.save

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import ir.sina.pirtobook.data.News
import ir.sina.pirtobook.data.Treding
import ir.sina.pirtobook.ui.theme.Dark
import ir.sina.pirtobook.ui.theme.Dark3
import ir.sina.pirtobook.ui.theme.Dark_Mode
import ir.sina.pirtobook.ui.theme.Font_Text
import ir.sina.pirtobook.ui.theme.My_Font
import ir.sina.pirtobook.ui.theme.Shapess
import ir.sina.pirtobook.ui.theme.White
import ir.sina.pirtobook.util.MyScreens


@Composable
fun SaveScreen() {
    val viewModel: SaveViewModel = getViewModel()
    val news = viewModel.news.collectAsState(emptyList())
    val treding = viewModel.treding.collectAsState(emptyList())
    val navigation = getNavController()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val filteredArticles = news.value.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.title.contains(searchQuery, ignoreCase = true)
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(Dark_Mode)) {

        AppBar({ navigation.popBackStack() }) { navigation.navigate(MyScreens.ProfileScreen.route) }
        NavBar(searchQuery) { newQuery -> searchQuery = newQuery }
        Title1()
        OneNewsList(treding.value, searchQuery) { title ->
            navigation.navigate(MyScreens.TredingDetailsScreen.route + "/$title")
        }
        Title2()
        SecondNewsList(filteredArticles, searchQuery = searchQuery) { title ->
            navigation.navigate(MyScreens.NewsDec.route + "/$title")
        }

    }

}

@Composable
fun AppBar(onBackClick: () -> Unit, onProfileClick: () -> Unit) {

    TopAppBar(
        title = {
            Text(
                text = "News",
                fontFamily = My_Font,
                color = White,
                fontWeight = FontWeight.Medium,
                fontSize = 33.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.5.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp),
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = { onBackClick.invoke() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    tint = White,
                    modifier = Modifier.size(36.dp),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onProfileClick.invoke() }) {
                Icon(
                    Icons.Default.Person,
                    tint = White,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
        },
        backgroundColor = Dark_Mode
    )

}

@Composable
fun NavBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
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
                tint = Color.White,
                modifier = Modifier.padding(start = 18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                )

            )
        }
    }
}

@Composable
fun Title1() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 38.5.dp), verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.material3.Text(
            text = "Trending News",
            color = Companion.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Font_Text
        )
    }

}

@Composable
fun Title2() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.5.dp), verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.material3.Text(
            text = "All News",
            color = Companion.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Font_Text
        )
    }

}

@Composable
fun OneNewsList(data: List<Treding>, searchQuery: String, onClickCard: (String) -> Unit) {

    val filteredNews = data.filter { it.title.contains(searchQuery, ignoreCase = true) }.shuffled()

    if (filteredNews.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.padding(top = 20.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(filteredNews) { newsItem ->
                OneNews(newsItem) {
                    onClickCard.invoke(newsItem.title)
                }
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp, bottom = 20.dp)
                .background(Dark_Mode.copy(alpha = 0.8f))
        ) {
            CircularProgressIndicator(
                color = Companion.White,
                strokeWidth = 4.dp
            )
        }
    }

}

@Composable
fun OneNews(treding: Treding, onClickCard: (String) -> Unit) {

    Card(
        modifier = Modifier
            .width(360.dp)
            .height(100.dp)
            .padding(start = 23.5.dp, top = 1.dp, end = 16.dp)
            .clickable { onClickCard.invoke(treding.title) },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = Shapess.medium,
        colors = CardDefaults.cardColors(Dark_Mode)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AsyncImage(
                model = treding.imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .background(Dark3)
                    .align(Alignment.BottomCenter), // این خط باعث می‌شود که Column در پایین و وسط قرار بگیرد
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = treding.title,
                    color = White,
                    fontSize = 20.sp,
                    fontFamily = Font_Text,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }

        }


    }

}

@Composable
fun SecondNewsList(news: List<News>, searchQuery: String, onClickNews: (String) -> Unit) {
    val filteredNews = news.filter { it.title.contains(searchQuery, ignoreCase = true) }.shuffled()
    val context = LocalContext.current

    if (filteredNews.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(top = 20.dp),
            contentPadding = PaddingValues(bottom = 175.dp)
        ) {
            items(filteredNews) { newsItem ->
                SecondNews(newsItem , onClickShare = { news ->
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, news.title)
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "📖 ${news.title} - ${news.description}\n\n${news.imageUrl ?: ""}"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "مقاله را به اشتراک بگذارید"))
                } ) {
                    onClickNews.invoke(newsItem.title)
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
fun SecondNews(news: News, onClickShare: (News) -> Unit ,onClickNews: (String) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp)
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 20.dp)
            .clickable { onClickNews.invoke(news.title) },
        colors = CardDefaults.cardColors(Dark),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = Shapess.medium
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Dark),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AsyncImage(
                model = news.imageUrl,
                modifier = Modifier.size(132.4.dp, 250.dp),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Dark),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = news.title,
                    color = White,
                    fontSize = 16.sp,
                    fontFamily = Font_Text,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 15.dp, top = 20.dp , bottom = 20.dp)
                )

                IconButton(onClick = { onClickShare.invoke(news) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        tint = White,
                        modifier = Modifier.size(36.dp).padding( top = 0.dp , bottom = 16.dp ),
                        contentDescription = null
                    )
                }


            }

        }

    }

}

