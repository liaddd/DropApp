package com.liad.droptask.viewmodels

import androidx.lifecycle.ViewModel
import com.liad.droptask.models.Bag
import com.liad.droptask.repositories.IDropRepository

class BagsFragViewModel constructor(private val repository: IDropRepository) : ViewModel() {

    val bagsStatefulLiveData = repository.statefulLiveDataBags

    fun addBags(newBags: List<Bag>) {
        repository.insertBags(newBags)
    }
}