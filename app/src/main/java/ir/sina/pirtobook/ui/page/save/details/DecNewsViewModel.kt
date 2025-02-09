package ir.sina.pirtobook.ui.page.save.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sina.pirtobook.data.Article
import ir.sina.pirtobook.data.News
import ir.sina.pirtobook.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DecNewsViewModel : ViewModel(), KoinComponent {

    private val repository: BookRepository by inject()

    private val _newsTitle = MutableStateFlow<String?>(null)
    val newsTitle: StateFlow<String?> = _newsTitle.asStateFlow()

    val newsDetails: StateFlow<News?> = newsTitle
        .flatMapLatest { title ->
            if (title != null) {
                repository.getNewsDetails(title)
            } else {
                kotlinx.coroutines.flow.flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setNewsTitle(title: String) {
        _newsTitle.value = title
    }
}