package com.gmail.appverstas.countdownapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gmail.appverstas.countdownapp.data.entities.CountdownItem

/**
 *Veli-Matti Tikkanen, 11.8.2021
 */

@Dao
interface CountdownDao {

    @Query("SELECT * FROM countdown_table ORDER BY date DESC")
    fun getAllItems(): LiveData<List<CountdownItem>>

    @Query("SELECT * FROM countdown_table WHERE id = :countdownItemID")
    fun observeCountdownItemByID(countdownItemID: String): LiveData<CountdownItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountdownItem(countdownItem: CountdownItem)

    @Delete
    suspend fun deleteCountdownItem(countdownItem: CountdownItem)

}