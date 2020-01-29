package com.liad.droptask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.liad.droptask.models.Contact
import com.liad.droptask.repositories.DropRepository
import javax.inject.Inject

class BagsFragViewModel @Inject constructor(private val  repository: DropRepository) : ViewModel(){

    // todo unnecessary DELETE IN PRODUCTION

    fun getContact(): LiveData<Contact> {
        return repository.getContactLiveData()
    }

}