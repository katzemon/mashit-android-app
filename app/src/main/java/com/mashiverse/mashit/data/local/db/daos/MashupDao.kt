package com.mashiverse.mashit.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mashiverse.mashit.data.local.db.entities.MashupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MashupDao {
    @Query("SELECT * FROM mashups WHERE wallet = :wallet LIMIT 1")
    suspend fun getMashupByWallet(wallet: String): MashupEntity?

    @Query("SELECT * FROM mashups WHERE wallet = :wallet LIMIT 1")
    fun getMashupByWalletFlow(wallet: String): Flow<MashupEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMashup(mashup: MashupEntity)
}