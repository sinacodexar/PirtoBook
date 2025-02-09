package ir.sina.pirtobook.ui.page.article.details

import ir.sina.pirtobook.ui.page.description.DecViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import ir.sina.pirtobook.data.Article
import ir.sina.pirtobook.data.Book
import ir.sina.pirtobook.ui.theme.Dark_Mode
import ir.sina.pirtobook.ui.theme.Font_Text
import ir.sina.pirtobook.ui.theme.My_Font
import ir.sina.pirtobook.ui.theme.Shapess
import ir.sina.pirtobook.ui.theme.White


@Composable
fun ArticleDetailsScreen(articleTitle: String?) {
    val viewModel: DecArticleViewModel = getViewModel()
    val articleDetails by viewModel.articleDetails.collectAsState()
    val context = LocalContext.current
    val navigation = getNavController()



    LaunchedEffect(key1 = true) {
        if (articleTitle != null) {
            viewModel.setArticleTitle(articleTitle)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Dark_Mode)
    ) {
        AppBars() {
            navigation.popBackStack()
        }

        when (articleDetails) {
            null -> {
                // Loading or error state
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .align(Alignment.CenterHorizontally)
                )
                // You can add an error message here if needed
            }
            else -> {
                ImageAndText(data = articleDetails!!)
            }
        }

        Spacer(modifier = Modifier.height( 180.dp ))

    }



}


@Composable
fun AppBars( onBackClick: () -> Unit ) {

    TopAppBar(
        navigationIcon = {
            IconButton( onClick = { onBackClick.invoke() } ) {
                Icon( imageVector = Icons.Default.KeyboardArrowLeft , modifier = Modifier.size( 36.dp ) , contentDescription = null , tint = White )
            }
        },
        backgroundColor = Dark_Mode,
        title = {
            Text(
                text = "Description",
                color = Color.White,
                fontFamily = My_Font,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 57.6.dp)
            )
        },
        elevation = 0.dp,
        modifier = Modifier
            .padding(top = 37.dp)
            .fillMaxWidth()
    )

}


@Composable
fun ImageAndText(data: Article) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = data.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 20.dp)
                .clip(shape = Shapess.medium)
                .size(400.dp, 286.dp)
        )

        Text(
            text = data.title,
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            textAlign = TextAlign.End,
            fontFamily = Font_Text
        )
        Text(
            text = data.author,
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp),
            textAlign = TextAlign.End,
            fontFamily = Font_Text
        )
        Text(
            text = data.description,
            color = White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp),
            textAlign = TextAlign.End,
            fontFamily = Font_Text
        )

    }



}





