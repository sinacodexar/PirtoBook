package ir.sina.pirtobook.ui.page.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sina.pirtobook.data.Article
import ir.sina.pirtobook.data.News
import ir.sina.pirtobook.data.Treding
import ir.sina.pirtobook.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SaveViewModel(private val repository: BookRepository): ViewModel() {

    val news: StateFlow<List<News>> = repository.getNews()
        .stateIn( viewModelScope , SharingStarted.Eagerly , emptyList() )

    val treding: StateFlow<List<Treding>> = repository.getTreding()
        .stateIn( viewModelScope , SharingStarted.Eagerly , emptyList() )

}
