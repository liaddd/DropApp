package com.liad.droptask.repositories

import androidx.lifecycle.LiveData
import co.climacell.statefulLiveData.core.StatefulLiveData
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import com.liad.droptask.models.DropReview

interface IDropRepository {

    val statefulLiveDataContactList: StatefulLiveData<List<Contact>>
    val statefulLiveDataAddress: StatefulLiveData<Address>
    val statefulLiveDataBags: StatefulLiveData<List<Bag>>

    val statefulLiveDataContact: StatefulLiveData<Contact>

    fun upsertContact(newContact: Contact)

    fun removeContact(contactId: Long)

    fun insertAddress(newAddress: Address)

    fun insertBags(newBags : List<Bag>)

    fun getDropReview(): LiveData<DropReview>

}