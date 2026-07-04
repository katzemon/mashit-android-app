package com.mashiverse.mashit.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_artists")
data class FavoriteArtistEntity(
    @PrimaryKey
    val alias: String,
    val isSubscribed: Boolean
)
