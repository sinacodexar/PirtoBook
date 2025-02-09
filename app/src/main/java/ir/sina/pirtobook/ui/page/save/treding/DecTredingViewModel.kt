package ir.sina.pirtobook.ui.page.save.treding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sina.pirtobook.data.News
import ir.sina.pirtobook.data.Treding
import ir.sina.pirtobook.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DecTredingViewModel : ViewModel(), KoinComponent {

    private val repository: BookRepository by inject()

    private val _tredingTitle = MutableStateFlow<String?>(null)
    val tredingTitle: StateFlow<String?> = _tredingTitle.asStateFlow()

    val tredingDetails: StateFlow<Treding?> = tredingTitle
        .flatMapLatest { title ->
            if (title != null) {
                repository.getTredingDetails(title)
            } else {
                kotlinx.coroutines.flow.flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setTredingTitle(title: String) {
        _tredingTitle.value = title
    }
}