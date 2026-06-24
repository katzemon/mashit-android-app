package com.mashiverse.mashit.data.di.data

import android.content.Context
import androidx.room.Room
import com.mashiverse.mashit.data.local.db.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext ctx: Context
    ) = Room.databaseBuilder(
        context = ctx,
        klass = RoomDb::class.java,
        name = "mashit_db",
    ).fallbackToDestructiveMigration(true)
        .build()

    @Provides
    fun provideNftDao(
        db: RoomDb
    ) = db.getNftDao()

    @Provides
    fun provideImageTypeDao(
        db: RoomDb
    ) = db.getImageTypeDao()

    @Provides
    fun provideMashupDao(
        db: RoomDb
    ) = db.getMashupDao()
}