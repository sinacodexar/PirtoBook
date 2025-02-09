package ir.sina.pirtobook.ui.page.article.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sina.pirtobook.data.Article
import ir.sina.pirtobook.data.Book
import ir.sina.pirtobook.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DecArticleViewModel : ViewModel(), KoinComponent {

    private val repository: BookRepository by inject()

    private val _articleTitle = MutableStateFlow<String?>(null)
    val articleTitle: StateFlow<String?> = _articleTitle.asStateFlow()

    val articleDetails: StateFlow<Article?> = articleTitle
        .flatMapLatest { title ->
            if (title != null) {
                repository.getArticleDetails(title)
            } else {
                kotlinx.coroutines.flow.flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setArticleTitle(title: String) {
        _articleTitle.value = title
    }
}