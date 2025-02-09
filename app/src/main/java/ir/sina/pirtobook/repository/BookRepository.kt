package ir.sina.pirtobook.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ir.sina.pirtobook.data.Article
import ir.sina.pirtobook.data.Book
import ir.sina.pirtobook.data.News
import ir.sina.pirtobook.data.Treding
import ir.sina.pirtobook.proxy.ShecanDns
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okio.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.CancellationException

class BookRepository {

    val client = OkHttpClient.Builder()
        .dns(ShecanDns()) // کلاینت DNS شکن
        .build()

    private val mainDatabase = FirebaseDatabase.getInstance("https://pirtobook-default-rtdb.firebaseio.com/")
        .apply { setPersistenceEnabled(true) }

    private val secondDatabase: FirebaseDatabase by lazy {
        val existingApp = FirebaseApp.getApps(mainDatabase.app.applicationContext).find { it.name == "secondFirebase" }

        val secondApp = existingApp ?: FirebaseApp.initializeApp(
            mainDatabase.app.applicationContext,
            FirebaseOptions.Builder()
                .setApplicationId("1:201469780413:android:b81aaa42916d336fc172d4")
                .setApiKey("AIzaSyC1v1VA1kRfRTuzegSUcKqtorOuLxUdM8Q")
                .setDatabaseUrl("https://pirtobook2-default-rtdb.firebaseio.com/")
                .build(),
            "secondFirebase"
        )

        FirebaseDatabase.getInstance(secondApp).apply { setPersistenceEnabled(true) }
    }

    private val secondDatabase2: FirebaseDatabase by lazy {
        val existingApp = FirebaseApp.getApps(mainDatabase.app.applicationContext).find { it.name == "secondFirebase2" }

        val secondApp2 = existingApp ?: FirebaseApp.initializeApp(
            mainDatabase.app.applicationContext,
            FirebaseOptions.Builder()
                .setApplicationId("1:678842599926:android:5ec4123e2d6a662f2fdfd7")
                .setApiKey("AIzaSyCTsUGsh_CoQHcpIInm5uPHQg9tjrid_N4")
                .setDatabaseUrl("https://pirbook3-default-rtdb.firebaseio.com/")
                .build(),
            "secondFirebase2"
        )

        FirebaseDatabase.getInstance(secondApp2).apply { setPersistenceEnabled(true) }
    }

    private val secondDatabase3: FirebaseDatabase by lazy {
        val existingApp = FirebaseApp.getApps(mainDatabase.app.applicationContext).find { it.name == "secondFirebase3" }

        val secondApp3 = existingApp ?: FirebaseApp.initializeApp(
            mainDatabase.app.applicationContext,
            FirebaseOptions.Builder()
                .setApplicationId("1:927705992152:android:7c9d48d9c808982f5080ad")
                .setApiKey("AIzaSyA7cWMKj8vDtM9Fm4HxblxMIGjujuwRT2g")
                .setDatabaseUrl("https://pirtobook4-default-rtdb.firebaseio.com/")
                .build(),
            "secondFirebase3"
        )

        FirebaseDatabase.getInstance(secondApp3).apply { setPersistenceEnabled(true) }
    }



    private val booksRef = mainDatabase.getReference("books")
    private val secondBooksRef = secondDatabase.getReference("article")
    private val secondNewsRef = secondDatabase2.getReference("news")
    private val secondTredingRef = secondDatabase3.getReference("treding")



    fun getBooks(): Flow<List<Book>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val books = snapshot.children.mapNotNull { it.getValue(Book::class.java) }
                trySend(books)
            }

            override fun onCancelled(error: DatabaseError) {
                close(CancellationException(error.message))
                Log.v( "Not_Books" , error.message )
            }
        }
        booksRef.addValueEventListener(listener)
        awaitClose { booksRef.removeEventListener(listener) }
    }

    fun getBookDetails(title: String): Flow<Book?> = flow {
        try {
            val query = booksRef.orderByChild("title").equalTo(title)
            val snapshot = query.get().await()

            val book = snapshot.children.firstOrNull()?.getValue(Book::class.java)
            emit(book)

        } catch (e: Exception) {
            Log.e("BookRepository", "Error fetching book details", e)
            emit(null)
        }
    }.catch { exception ->
        Log.e("BookRepository", "Caught an exception", exception)
        emit(null)
    }

    fun getArticles(): Flow<List<Article>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val articles = snapshot.children.mapNotNull { it.getValue(Article::class.java) }
                trySend(articles)
            }

            override fun onCancelled(error: DatabaseError) {
                close(CancellationException(error.message))
            }
        }
        secondBooksRef.addValueEventListener(listener)
        awaitClose { secondBooksRef.removeEventListener(listener) }
    }

    fun addArticle(article: Article, onComplete: (Boolean, String?) -> Unit) {
        val key = secondBooksRef.push().key ?: return
        secondBooksRef.child(key).setValue(article)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    fun getArticleDetails(title: String): Flow<Article?> = flow {
        try {
            val query = secondBooksRef.orderByChild("title").equalTo(title)
            val snapshot = query.get().await()

            val article = snapshot.children.firstOrNull()?.getValue(Article::class.java)
            emit(article)

        } catch (e: Exception) {
            Log.e("BookRepository", "Error fetching book details", e)
            emit(null)
        }
    }.catch { exception ->
        Log.e("BookRepository", "Caught an exception", exception)
        emit(null)
    }


    fun getNews(): Flow<List<News>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val news = snapshot.children.mapNotNull { it.getValue(News::class.java) }
                trySend(news)
            }

            override fun onCancelled(error: DatabaseError) {
                close(CancellationException(error.message))
            }
        }
        secondNewsRef.addValueEventListener(listener)
        awaitClose { secondNewsRef.removeEventListener(listener) }
    }

    fun getNewsDetails(title: String): Flow<News?> = flow {
        try {
            val query = secondNewsRef.orderByChild("title").equalTo(title)
            val snapshot = query.get().await()

            val news = snapshot.children.firstOrNull()?.getValue(News::class.java)
            emit(news)

        } catch (e: Exception) {
            Log.e("BookRepository", "Error fetching book details", e)
            emit(null)
        }
    }.catch { exception ->
        Log.e("BookRepository", "Caught an exception", exception)
        emit(null)
    }

    fun getTreding(): Flow<List<Treding>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tred = snapshot.children.mapNotNull { it.getValue(Treding::class.java) }
                trySend(tred)
            }

            override fun onCancelled(error: DatabaseError) {
                close(CancellationException(error.message))
            }
        }
        secondTredingRef.addValueEventListener(listener)
        awaitClose { secondTredingRef.removeEventListener(listener) }
    }

    fun getTredingDetails(title: String): Flow<Treding?> = flow {
        try {
            val query = secondTredingRef.orderByChild("title").equalTo(title)
            val snapshot = query.get().await()

            val tred = snapshot.children.firstOrNull()?.getValue(Treding::class.java)
            emit(tred)

        } catch (e: Exception) {
            Log.e("BookRepository", "Error fetching book details", e)
            emit(null)
        }
    }.catch { exception ->
        Log.e("BookRepository", "Caught an exception", exception)
        emit(null)
    }








}