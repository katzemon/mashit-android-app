package com.mashiverse.mashit.data.repos.artists

import com.mashiverse.mashit.data.local.db.daos.FavoriteArtistDao
import com.mashiverse.mashit.data.local.db.entities.FavoriteArtistEntity
import javax.inject.Inject

class FavoriteArtistsRepo @Inject constructor(
    val favoriteArtistDao: FavoriteArtistDao
) {
    fun getFavoriteArtist() = favoriteArtistDao.getFavoriteArtists()

    suspend fun updateFavoriteArtist(favoriteArtist: FavoriteArtistEntity) {
        favoriteArtistDao.insertFavoriteArtist(favoriteArtist)
    }
}