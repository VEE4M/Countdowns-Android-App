package com.gmail.appverstas.countdownapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.appverstas.countdownapp.data.entities.CountdownItem

/**
 *Veli-Matti Tikkanen, 11.8.2021
 */

@Database(entities = [CountdownItem::class], version = 1)
abstract class CountdownDatabase: RoomDatabase() {

    abstract fun countdownDao(): CountdownDao

}