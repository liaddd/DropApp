package com.liad.droptask.viewmodels

import androidx.lifecycle.ViewModel
import com.liad.droptask.models.Contact
import com.liad.droptask.repositories.IDropRepository

class ContactFragViewModel (private val repository: IDropRepository) : ViewModel() {

    val statefulLiveDataContact = repository.statefulLiveDataContact

    val statefulLiveDataContactList = repository.statefulLiveDataContactList


    fun insertContact(contact: Contact){
        repository.insertContact(contact)
    }

}