package com.liad.droptask.repositories

import androidx.lifecycle.LiveData
import co.climacell.statefulLiveData.core.StatefulLiveData
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import com.liad.droptask.models.DropReview

interface IDropRepository {

    val statefulLiveDataContact: StatefulLiveData<Contact>
    val statefulLiveDataContactList: StatefulLiveData<List<Contact>>
    val statefulLiveDataAddress: StatefulLiveData<Address>
    val statefulLiveDataBags: StatefulLiveData<List<Bag>>

    fun insertContact(newContact: Contact)

    fun insertAddress(newAddress: Address)

    fun insertBags(newBags : List<Bag>)

    fun getDropReview() : LiveData<DropReview>
}