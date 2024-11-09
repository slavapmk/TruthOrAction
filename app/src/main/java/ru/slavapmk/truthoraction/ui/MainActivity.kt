package ru.slavapmk.truthoraction.ui

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

    lateinit var aiGameInteractor: AiGameInteractor
    private lateinit var binding: ActivityMainBinding
    val players: MutableList<String> = mutableListOf(
        "Перви",
        "Второи",
        "Трети",
    )
    var currentPlayer: Int = 0


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

//        val model = GenerativeModel(
//            modelName = "gemini-1.5-flash-001",
//            apiKey = "AIzaSyAYvNUCYlf0EQn9ghRZr4vngp6WHy3K71k",
//            generationConfig = generationConfig {
//                temperature = 0.15f
//                topK = 32
//                topP = 1f
//                maxOutputTokens = 512
//            },
//            safetySettings = listOf(
//                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
//                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
//                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
//                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
//            )
//        )
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = when (LOG_REQUESTS) {
            true -> HttpLoggingInterceptor.Level.BODY
            false -> HttpLoggingInterceptor.Level.NONE
        }

        val retrofit: GeminiAPI = Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiAPI::class.java)

        aiGameInteractor = AiGameInteractor(
            GeminiTextGenerator("", retrofit, Gson())
        )
    }
}