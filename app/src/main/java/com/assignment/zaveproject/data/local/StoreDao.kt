package com.assignment.zaveproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    @Query("SELECT * FROM stores WHERE query = :query")
    suspend fun getStores(query: String): List<StoreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<StoreEntity>)

    @Query("DELETE FROM stores WHERE query = :query")
    suspend fun clearQuery(query: String)

    @Query("UPDATE stores SET isSaved = 1 WHERE id = :storeId")
    suspend fun saveStore(storeId: String)

    @Query("SELECT * FROM stores WHERE isSaved = 1")
    fun getSavedStores(): Flow<List<StoreEntity>>

}