package ir.sina.pirtobook.util

import ir.sina.pirtobook.data.Book


sealed class MyScreens( val route: String ) {

    object MainScreen: MyScreens("mainScreen")
    object ProfileScreen: MyScreens("profileScreen")
    object SaveScreen: MyScreens("saveScreen")
    object DescriptionScreen: MyScreens("description_screen")
    object ArticleDec: MyScreens("articleDec")
    object NewsDec: MyScreens("newsDec")
    object TredingDetailsScreen: MyScreens("tredingDetailsScreen")
    object NewsScreen: MyScreens("newsScreen")

}

