package ir.sina.pirtobook.ui.page.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sina.pirtobook.data.Book
import ir.sina.pirtobook.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(private val repository: BookRepository) : ViewModel() {

    val books: StateFlow<List<Book>> = repository.getBooks()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}