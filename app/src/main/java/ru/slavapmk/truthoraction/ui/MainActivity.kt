package ru.slavapmk.truthoraction.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.slavapmk.truthoraction.R
import ru.slavapmk.truthoraction.databinding.ActivityMainBinding
import ru.slavapmk.truthoraction.dto.GeminiAPI
import ru.slavapmk.truthoraction.io.AiGameInteractor
import ru.slavapmk.truthoraction.io.GeminiTextGenerator

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var fmanager: FragmentManager
        private const val LOG_REQUESTS = true
    }

    private lateinit var binding: ActivityMainBinding
    val shared: SharedPreferences by lazy {
        getSharedPreferences("truthlocalstorage", Context.MODE_PRIVATE)
    }
    val players: MutableList<String> = mutableListOf(
        "Перви",
        "Второи",
        "Трети",
    )
    var currentPlayer: Int = 0
    private val retrofit: GeminiAPI by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = when (LOG_REQUESTS) {
            true -> HttpLoggingInterceptor.Level.BODY
            false -> HttpLoggingInterceptor.Level.NONE
        }
        Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiAPI::class.java)
    }
    val aiGameInteractor: AiGameInteractor by lazy {
        AiGameInteractor(
            GeminiTextGenerator(
                shared.getString("aiToken", "")!!,
                retrofit, Gson()
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        fmanager = supportFragmentManager
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViewById<BottomNavigationView>(R.id.bottom_bar).setupWithNavController(
            (supportFragmentManager.findFragmentById(
                R.id.fragment_container
            ) as NavHostFragment).navController
        )
        window.navigationBarColor = ContextCompat.getColor(this, R.color.panel)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
    }
}