package com.example.funfacts.main

import android.app.Application
import androidx.room.Room
import com.example.funfacts.room.FunFactDatabase
import com.example.funfacts.room.FunFactRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FunFactsApplication : Application(){

    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            FunFactDatabase::class.java,
            "fun_facts_db"
        ).build()
    }

    val httpClient by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    val repository by lazy {
        FunFactRepository(
            dao = database.funFactDao(),
            httpClient = httpClient
        )
    }
}