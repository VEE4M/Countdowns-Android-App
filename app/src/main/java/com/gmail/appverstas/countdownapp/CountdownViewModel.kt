package com.gmail.appverstas.countdownapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.appverstas.countdownapp.data.entities.CountdownItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Veli-Matti Tikkanen, 12.8.2021
 */


@HiltViewModel
class CountdownViewModel @Inject constructor(private val countdownRepository: CountdownRepository) : ViewModel(){


    fun getAllItems() = countdownRepository.getAllItems()

    fun observeCountdownItemByID(countdownItemID: String) = countdownRepository.observeCountdownItemByID(countdownItemID)

    fun insertCountdownItem(countdownItem: CountdownItem) = viewModelScope.launch {
        countdownRepository.insertCountdownItem(countdownItem)
    }

    fun deleteCountdownItem(countdownItem: CountdownItem) = viewModelScope.launch {
        countdownRepository.deleteCountdownItem(countdownItem)
    }

}