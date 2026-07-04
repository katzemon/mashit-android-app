package com.mashiverse.mashit.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mashiverse.mashit.data.local.db.converters.ImageTypeConverters
import com.mashiverse.mashit.data.local.db.converters.MashupConverters
import com.mashiverse.mashit.data.local.db.converters.NftConverters
import com.mashiverse.mashit.data.local.db.daos.FavoriteArtistDao
import com.mashiverse.mashit.data.local.db.daos.MashupDao
import com.mashiverse.mashit.data.local.db.daos.NftDao
import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.FavoriteArtistEntity
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.local.db.entities.MashupEntity
import com.mashiverse.mashit.data.local.db.entities.NftEntity

@Database(
    entities = [NftEntity::class, ImageTypeEntity::class, MashupEntity::class, FavoriteArtistEntity::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(NftConverters::class, ImageTypeConverters::class, MashupConverters::class)
abstract class RoomDb : RoomDatabase() {
    abstract fun getNftDao(): NftDao
    abstract fun getImageTypeDao(): TraitTypeDao
    abstract fun getMashupDao(): MashupDao
    abstract fun getFavoriteArtistDao(): FavoriteArtistDao
}