package com.liad.droptask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.liad.droptask.models.Contact
import com.liad.droptask.repositories.DropRepository
import javax.inject.Inject

class ContactFragViewModel @Inject constructor(private val repository: DropRepository) : ViewModel() {

    fun getContact(): LiveData<Contact> {
        return repository.getContactLiveData()
    }

}