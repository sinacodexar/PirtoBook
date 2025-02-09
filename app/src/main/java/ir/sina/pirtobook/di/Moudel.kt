package ir.sina.pirtobook.di



import ir.sina.pirtobook.repository.BookRepository
import ir.sina.pirtobook.ui.page.article.ArticleViewModel
import ir.sina.pirtobook.ui.page.article.details.DecArticleViewModel
import ir.sina.pirtobook.ui.page.description.DecViewModel
import ir.sina.pirtobook.ui.page.main.MainViewModel
import ir.sina.pirtobook.ui.page.save.SaveViewModel
import ir.sina.pirtobook.ui.page.save.details.DecNewsViewModel
import ir.sina.pirtobook.ui.page.save.treding.DecTredingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    single< BookRepository > { BookRepository() }
    viewModel { MainViewModel(get()) }
    viewModel { DecViewModel() }
    viewModel { ArticleViewModel( get() ) }
    viewModel { DecArticleViewModel() }
    viewModel { SaveViewModel( get() ) }
    viewModel { DecNewsViewModel() }
    viewModel { DecTredingViewModel() }

}