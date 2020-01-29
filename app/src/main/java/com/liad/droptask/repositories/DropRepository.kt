package com.liad.droptask.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import com.liad.droptask.models.Phone
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
open class DropRepository @Inject constructor(/*private val dropDatabase: DropDatabase*/) {

    // todo FIX - Get contact from the one created and saved in Database OR fetch from Api
    // todo REMOVE - HARD CODED initialization
    private val contact: Contact =
        Contact(
            "Liad Ho", Phone(972, "547320099"),
            listOf(Address("wow", "nice", "cool")),
            listOf(
                Bag("DA${Random.nextInt(1, 99999)}"),
                Bag("DA${Random.nextInt(1, 99999)}"),
                Bag("DA${Random.nextInt(1, 99999)}")
            )
        )
    private val contactLiveData: MutableLiveData<Contact> = MutableLiveData(contact)
    private val addressLiveData: MutableLiveData<Address> = MutableLiveData()
    private val bagsLiveData: MutableLiveData<List<Bag>> = MutableLiveData()

    init {
        Log.d("Liad", "repository initialized hashcode: $this")
    }

    fun insertContact(contact: Contact) {
        TODO("not implemented")
    }

    fun getContactLiveData(): LiveData<Contact> {
        return contactLiveData
    }

    fun insertAddress(address: Address) {
        TODO("not implemented")
    }

    fun getAddressLiveData(): LiveData<Address> {
        return addressLiveData
    }

    fun insertBag(bag: Bag) {
        TODO("not implemented")
    }

    fun getBags(): LiveData<List<Bag>> {
        return bagsLiveData
    }
}