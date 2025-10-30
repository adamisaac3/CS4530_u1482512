package com.example.funfacts.room

import com.example.funfacts.api.FunFactResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow

class FunFactRepository (
    private val dao: FunFactDao,
    private val httpClient: HttpClient
){
    val allFacts: Flow<List<FunFactEntity>> = dao.getAllFacts()

    suspend fun fetchAndStoreNewFact(): Result<Unit>{
        return try{
            val response: FunFactResponse = httpClient.get(
                "https://uselessfacts.jsph.pl/random.json?language=en"
            ).body()

            val entity = FunFactEntity(
                text = response.text,
                source = response.source,
                sourceUrl = response.sourceUrl,
                language = response.language,
                permalink = response.permalink
            )

            dao.insertFact(entity)
            Result.success(Unit)
        }
        catch(e: Exception){
            Result.failure(e)
        }
    }

    suspend fun deleteFact(factId: Long){
        dao.deleteFact(factId)
    }

}