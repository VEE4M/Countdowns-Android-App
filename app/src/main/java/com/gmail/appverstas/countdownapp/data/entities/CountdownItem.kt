package com.gmail.appverstas.countdownapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *Veli-Matti Tikkanen, 11.8.2021
 */
@Entity(tableName = "countdown_table")
data class CountdownItem(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val date : Long
)
