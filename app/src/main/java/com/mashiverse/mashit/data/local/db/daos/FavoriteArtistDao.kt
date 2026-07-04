package com.mashiverse.mashit.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mashiverse.mashit.data.local.db.entities.FavoriteArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteArtistDao {
    @Query("SELECT * FROM favorite_artists WHERE isSubscribed = 1")
    fun getFavoriteArtists(): Flow<List<FavoriteArtistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteArtist(artist: FavoriteArtistEntity)
}