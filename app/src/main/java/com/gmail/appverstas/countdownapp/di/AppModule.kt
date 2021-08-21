package com.gmail.appverstas.countdownapp.di

import android.content.Context
import androidx.room.Room
import com.gmail.appverstas.countdownapp.data.CountdownDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *Veli-Matti Tikkanen, 11.8.2021
 */

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideCountdownDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, CountdownDatabase::class.java , "countdown_db" ).build()

    @Singleton
    @Provides
    fun provideCountdownDao(db: CountdownDatabase) = db.countdownDao()

}