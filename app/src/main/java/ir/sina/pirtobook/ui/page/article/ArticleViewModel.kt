package ir.sina.pirtobook.ui.page.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sina.pirtobook.data.Article
import ir.sina.pirtobook.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArticleViewModel( private val repository: BookRepository): ViewModel() {

    val articles: StateFlow<List<Article>> = repository.getArticles()
        .stateIn( viewModelScope , SharingStarted.Eagerly , emptyList() )


    fun addArticle(article: Article, onComplete: (Boolean, String?) -> Unit) {
        repository.addArticle(article, onComplete)
    }




}