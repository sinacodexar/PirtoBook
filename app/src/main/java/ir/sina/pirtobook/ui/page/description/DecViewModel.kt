package ir.sina.pirtobook.ui.page.description


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class DecViewModel : ViewModel(), KoinComponent {

    private val repository: BookRepository by inject()

    private val _bookTitle = MutableStateFlow<String?>(null)
    val bookTitle: StateFlow<String?> = _bookTitle.asStateFlow()

    val bookDetails: StateFlow<Book?> = bookTitle
        .flatMapLatest { title ->
            if (title != null) {
                repository.getBookDetails(title)
            } else {
                kotlinx.coroutines.flow.flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setBookTitle(title: String) {
        _bookTitle.value = title
    }
}