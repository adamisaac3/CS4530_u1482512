package com.example.funfacts.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FunFactDao{
    @Query("SELECT * from fun_facts order by id desc")
    fun getAllFacts(): Flow<List<FunFactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: FunFactEntity)

    @Query("delete from fun_facts")
    suspend fun deleteAllFacts()

    @Query("delete from fun_facts where id = :factId")
    suspend fun deleteFact(factId: Long)

}