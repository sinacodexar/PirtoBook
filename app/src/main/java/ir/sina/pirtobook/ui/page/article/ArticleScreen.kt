@file:Suppress("UNREACHABLE_CODE")

package ir.sina.pirtobook.ui.page.article

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import ir.sina.pirtobook.R
import ir.sina.pirtobook.data.Article
import ir.sina.pirtobook.ui.theme.Dark
import ir.sina.pirtobook.ui.theme.Dark_Mode
import ir.sina.pirtobook.ui.theme.Font_Text
import ir.sina.pirtobook.ui.theme.My_Font
import ir.sina.pirtobook.ui.theme.Shapess
import ir.sina.pirtobook.ui.theme.White
import ir.sina.pirtobook.util.MyScreens

@Composable
fun NewsScreen() {
    val viewModel: ArticleViewModel = getViewModel()
    val articles = viewModel.articles.collectAsState(emptyList())
    val navigation = getNavController()


    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    // فیلتر کردن مقالات بر اساس مقدار جستجو
    val filteredArticles = articles.value.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.author.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .background(Dark_Mode)
            .fillMaxSize()
    ) {
        AppBar ( onClickAddArticle = { showDialog = true } ) {

            navigation.popBackStack()

        }
        NavBar(searchQuery) { searchQuery = it }
        ArticleList(filteredArticles, onClickArticle = { title ->
            navigation.navigate(MyScreens.ArticleDec.route + "/$title") {
                launchSingleTop = true
            }
        } ,  onToggleLike = { })
    }

    if (showDialog) {
        AddArticleDialog(
            onDismiss = { showDialog = false },
            onArticleAdded = { article ->
                viewModel.addArticle(article) { success, errorMessage ->
                    if (success) {
                        showDialog = false
                        Toast.makeText(context, "مقاله با موفقیت اضافه شد!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, errorMessage ?: "خطا در افزودن مقاله", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}

@Composable
fun AppBar( onClickAddArticle: () -> Unit , onClickLike: (  ) -> Unit ) {

    TopAppBar(
        title = {
            Text(
                text = "Article",
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = My_Font,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.99.dp , end = 10.5.dp)
            )
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp), backgroundColor = Dark_Mode,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { onClickAddArticle.invoke() }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    modifier = Modifier.size(27.dp),
                    tint = White,
                    contentDescription = null
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { onClickLike.invoke() }) {
                Icon( imageVector = Icons.Default.KeyboardArrowLeft , tint = White , modifier = Modifier.size( 38.dp ) , contentDescription = null )
            }
        }
    )

}

@Composable
fun NavBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
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
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                )

            )
        }
    }
}

@Composable
fun ArticleList(data: List<Article>, onClickArticle: (String) -> Unit , onToggleLike: (Article) -> Unit) {
    val context = LocalContext.current
    if (data.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(top = 20.dp),
            contentPadding = PaddingValues(bottom = 195.dp)
        ) {
            items(data.size) { index ->
                Articles(
                    article = data[index],
                    onClickArticle = { onClickArticle.invoke(it) },
                    onClickShare = { article ->
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, article.title)
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "📖 ${article.title} - ${article.author}\n\n${article.imageUrl ?: ""}"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "مقاله را به اشتراک بگذارید"))
                    },

                ) {
                    onToggleLike.invoke( it )
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
fun Articles(
    article: Article,
    onClickArticle: (String) -> Unit,
    onClickShare: (Article) -> Unit,
    onToggleLike: (Article) -> Unit
) {
    var isLiked by remember { mutableStateOf(article.like) }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(458.dp)
            .padding(start = 16.dp, end = 16.dp, top = 25.dp)
            .clickable { onClickArticle.invoke(article.title) },
        elevation = 8.dp,
        shape = Shapess.medium,
        backgroundColor = Dark,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(288.dp)
            )

            Text(
                text = article.title,
                fontSize = 18.sp,
                color = White,
                fontWeight = FontWeight.Bold,
                fontFamily = Font_Text,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                text = article.author,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = White,
                fontFamily = Font_Text,
                modifier = Modifier.padding(top = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp , bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { onClickShare.invoke(article) }) {
                    Icon(
                        tint = White,
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(27.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.padding(bottom = 20.dp))
        }
    }
}


@Composable
fun AddArticleDialog(onDismiss: () -> Unit, onArticleAdded: (Article) -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Card(shape = Shapess.small, backgroundColor = Dark_Mode, elevation = 10.dp) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("")
            },
            text = {
                Column( verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally ) {
                    androidx.compose.material3.Button(
                        onClick = { imagePicker.launch("image/*") },
                           modifier = Modifier
                               .padding(top = 16.dp, bottom = 16.dp)
                               .clip(shape = Shapess.medium),
                        colors = ButtonDefaults.buttonColors( Dark )
                    ) { Text("اضافه کردن عکس" , color = White , fontFamily = Font_Text) }
                    imageUri?.let {
                        AsyncImage(
                            model = it,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(235.dp, 185.dp)
                                .clip(Shapess.medium)
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 15.dp),
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("موضوع" , color = White , fontFamily = Font_Text) })
                    OutlinedTextField(
                        value = author,
                        modifier = Modifier.padding(top = 5.dp),
                        onValueChange = { author = it },
                        label = { Text("نویسنده" , color = White , fontFamily = Font_Text) })
                    OutlinedTextField(
                        value = description,
                        modifier = Modifier.padding(top = 5.dp),
                        maxLines = 7,
                        onValueChange = { description = it },
                        label = { Text("توضیحات کامل" , color = White , fontFamily = Font_Text) })


                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {

                    if ( title.isNotEmpty() && author.isNotEmpty() ) {
                        val newArticle =
                            Article(title, author, description, false, imageUri?.toString() ?: "")
                        onArticleAdded(newArticle)
                    }else {
                        Toast.makeText(context, " لطفا مقادیر را پر کنید ", Toast.LENGTH_SHORT).show()
                    }


                }) {
                    Text("انتشار", color = White, fontFamily = Font_Text)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = onDismiss, modifier = Modifier.padding(bottom = 10.dp )) {
                    Text("لغو", color = White, fontFamily = Font_Text)
                }
            },
            backgroundColor = Dark_Mode,
            shape = Shapess.medium
        )
    }


}
