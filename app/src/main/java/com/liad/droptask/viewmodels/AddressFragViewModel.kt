package com.liad.droptask.viewmodels

import androidx.lifecycle.ViewModel
import com.liad.droptask.models.Address
import com.liad.droptask.repositories.IDropRepository

class AddressFragViewModel(private val repository: IDropRepository) : ViewModel() {

    val statefulLiveDataAddress = repository.statefulLiveDataAddress

    fun insertAddress(address: Address) {
        repository.insertAddress(address)
    }
}