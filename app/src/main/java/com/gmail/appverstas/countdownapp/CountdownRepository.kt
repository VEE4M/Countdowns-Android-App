package com.gmail.appverstas.countdownapp

import com.gmail.appverstas.countdownapp.data.CountdownDao
import com.gmail.appverstas.countdownapp.data.entities.CountdownItem
import javax.inject.Inject

/**
 *Veli-Matti Tikkanen, 12.8.2021
 */


class CountdownRepository @Inject constructor(private val countdownDao: CountdownDao) {

    fun getAllItems() = countdownDao.getAllItems()

    suspend fun insertCountdownItem(countdownItem: CountdownItem){
        countdownDao.insertCountdownItem(countdownItem)
    }

    suspend fun deleteCountdownItem(countdownItem: CountdownItem){
        countdownDao.deleteCountdownItem(countdownItem)
    }

    fun observeCountdownItemByID(countdownItemID: String) = countdownDao.observeCountdownItemByID(countdownItemID)

}