package com.liad.droptask.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import co.climacell.statefulLiveData.core.*
import com.liad.droptask.DropApplication
import com.liad.droptask.database.DropDao
import com.liad.droptask.database.DropDatabase
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import com.liad.droptask.models.DropReview
import com.liad.droptask.server_connection.RequestApi
import com.liad.droptask.utils.extensions.observeOnceNullable
import com.liad.droptask.utils.extensions.toast
import retrofit2.Retrofit
import java.util.concurrent.Executors


class DropRepository(dropDatabase: DropDatabase, retrofit: Retrofit) : IDropRepository {

    override val statefulLiveDataContact: StatefulLiveData<Contact> = MutableStatefulLiveData()
    override val statefulLiveDataContactList: StatefulLiveData<List<Contact>>
    override val statefulLiveDataAddress: StatefulLiveData<Address>
    override val statefulLiveDataBags: StatefulLiveData<List<Bag>>

    private val dropReviewMutableLiveData: MutableLiveData<DropReview> = MutableLiveData()

    private val executor = Executors.newSingleThreadExecutor()
    private var dao: DropDao
    private var requestApi: RequestApi

    init {
        Log.d("Liad", "repository initialized: $this")
        dao = dropDatabase.dao()
        requestApi = retrofit.create(RequestApi::class.java)

        statefulLiveDataContactList = getContactListStatefulLiveData()

        statefulLiveDataAddress = statefulLiveDataContact.switchMap {
            getAddressStatefulLiveData(if (it.id != 0L) it.id else 1)
        }

        statefulLiveDataBags = statefulLiveDataContact.switchMap {
            getBagsStatefulLiveData(it.id)
        }
    }

    /** Contact functions */

    // insert Contact to DB
    override fun upsertContact(newContact: Contact) {
        Log.d("Liad", "upsert Contact()")

        val mutableContacts = (statefulLiveDataContactList as? MutableStatefulLiveData<List<Contact>>)
        val possibleContact = (statefulLiveDataContact.value as? StatefulData.Success<Contact>)?.data

        val contactsList = (mutableContacts?.value as? StatefulData.Success<List<Contact>>)?.data ?: emptyList()
        if (possibleContact != newContact && !contactsList.contains(newContact)) {
            postContactToApi(newContact).observeOnce(Observer {
                when (it) {
                    is StatefulData.Success -> {
                        saveContactInDatabase(newContact)
                        val newList = contactsList.toMutableList()
                        newList.add(newContact)
                        mutableContacts?.putData(newList)
                    }
                    is StatefulData.Loading -> {
                    }
                    is StatefulData.Error -> {
                    }
                }
            })
        } else if (possibleContact != newContact && contactsList.contains(newContact)) {
            val currentContactIndex = contactsList.indexOf(newContact)
            val currentContact = contactsList[currentContactIndex]
            val tempContactList = contactsList.toMutableList()
            tempContactList.removeAt(currentContactIndex)
            tempContactList.add(currentContact)
            (statefulLiveDataContact as? MutableStatefulLiveData<Contact>)?.putData(currentContact)
            (statefulLiveDataContactList as? MutableStatefulLiveData<List<Contact>>)?.putData(tempContactList)
        }
    }

    // remove Contact from DB
    override fun removeContact(contactId: Long) {
        executor.submit {
            val test = dao.deleteContact(contactId)
            toast(DropApplication.instance, "Contact deleted: $test")
        }
    }

    // get Contact from DB
    private fun getContactListLiveData(): LiveData<List<Contact>> = dao.getContacts()

    // get Contact from DB or API
    private fun getContactListStatefulLiveData(): StatefulLiveData<List<Contact>> {
        val statefulResult = MutableStatefulLiveData<List<Contact>>()
        statefulResult.putLoading()

        // fetching from DB
        getContactListLiveData().observeOnceNullable(Observer { dbContacts ->
            Log.d("Liad", "dbContacts: $dbContacts")
            if (dbContacts.isNullOrEmpty()) {
                // fetching from Api
                getContactFromApi().observeOnce(Observer { apiContact ->
                    //statefulResult.putValue(apiContact)
                    if (apiContact is StatefulData.Success) {
                        statefulResult.putData(listOf(apiContact.data))
                        saveContactInDatabase(apiContact.data)
                    }
                })
            } else {
                statefulResult.putData(dbContacts)
            }
        })
        return statefulResult
    }

    // Api request - POST CONTACT
    private fun postContactToApi(contact: Contact): StatefulLiveData<Unit> {
        Log.d("Liad", "Posting Contact to API $contact")
        return requestApi.postContact(contact)/*.map {
            saveContactInDatabase(contactId)
        }*/
    }

    private fun saveContactInDatabase(contact: Contact) {
        Log.d("Liad", "saveContactInDatabase: $contact")
        executor.submit {
            val value = dao.insertContact(contact)
            Log.d("Liad", "insertion value: $value")
            contact.id = value
            (statefulLiveDataContact as? MutableStatefulLiveData<Contact>)?.putData(contact)
        }
    }

    // Api request - GET CONTACT
    private fun getContactFromApi() = requestApi.getContact()

    /** End of Contact functions */


    /** Address functions */

    // insert Address to DB
    override fun insertAddress(newAddress: Address) {
        Log.d("Liad", "insertAddress()")
        val possibleAddress = (statefulLiveDataAddress.value as? StatefulData.Success)?.data
        val currentContactId = (statefulLiveDataContact.value as? StatefulData.Success)?.data?.id
        newAddress.contactId = currentContactId ?: 0
        if (possibleAddress != newAddress /*&& isChanged*/) {
            postAddressToApi(newAddress).observeOnce(Observer {
                when (it) {
                    is StatefulData.Success -> {
                        (statefulLiveDataAddress as? MutableStatefulLiveData<Address>)?.putData(newAddress)
                        saveAddressInDatabase(newAddress)
                    }
                    is StatefulData.Loading -> (statefulLiveDataAddress as? MutableStatefulLiveData<Address>)?.putLoading(
                        it.loadingData
                    )
                    is StatefulData.Error -> (statefulLiveDataAddress as? MutableStatefulLiveData<Address>)?.putError(
                        it.throwable
                    )
                }
            })
        }
    }

    // get Address from DB
    private fun getAddressLiveData(contactId: Long): LiveData<Address?> = dao.getAddress(contactId)

    private fun getAddressStatefulLiveData(contactId: Long): StatefulLiveData<Address> {
        Log.d("Liad", "getAddressStatefulLiveData()")
        val statefulResult = MutableStatefulLiveData<Address>()
        statefulResult.putLoading()
        getAddressLiveData(contactId).observeOnceNullable(Observer { dbAddress ->
            if (dbAddress == null) {
                getAddressFromApi().observeOnce(Observer { apiAddress ->
                    statefulResult.putValue(apiAddress)
                    if (apiAddress is StatefulData.Success) {
                        apiAddress.data.contactId =
                            (statefulLiveDataContact.value as? StatefulData.Success<Contact>)?.data?.id ?: 0
                        saveAddressInDatabase(apiAddress.data)
                    }
                })
            } else {
                statefulResult.putData(dbAddress)
            }
        })

        return statefulResult
    }

    // Api request - POST ADDRESS
    private fun postAddressToApi(newAddress: Address): StatefulLiveData<Unit> {
        Log.d("Liad", "posting $newAddress to API")
        return requestApi.postAddress(newAddress)
    }

    // Api request - GET ADDRESS
    private fun getAddressFromApi() = requestApi.getAddress()

    private fun saveAddressInDatabase(newAddress: Address) {
        Log.d("Liad", "saveAddressInDatabase - $newAddress")
        executor.submit {
            val value = dao.insertAddress(newAddress)
            Log.d("Liad", "insertion value: $value")
        }
    }

    /** End of Address functions */


    /** Bags functions */

    // insert Bag to DB
    override fun insertBags(newBags: List<Bag>) {
        Log.d("Liad", "insertBags()")
        val possibleSuccessData = (statefulLiveDataBags.value as? StatefulData.Success)?.data

        if (possibleSuccessData != newBags) {
            postBagsToApi(newBags).observeOnce(Observer {
                when (it) {
                    is StatefulData.Success -> {
                        newBags.map { bag ->
                            bag.contactId =
                                (statefulLiveDataContact.value as? StatefulData.Success<Contact>)?.data?.id ?: 0
                        }
                        (statefulLiveDataBags as? MutableStatefulLiveData<List<Bag>>)?.putData(newBags)
                        saveBagsInDatabase(newBags)
                    }
                    is StatefulData.Loading -> (statefulLiveDataBags as? MutableStatefulLiveData<List<Bag>>)?.putLoading(
                        it.loadingData
                    )
                    is StatefulData.Error -> (statefulLiveDataBags as? MutableStatefulLiveData<List<Bag>>)?.putError(
                        it.throwable
                    )
                }
            })
        }
    }

    private fun postBagsToApi(newBags: List<Bag>): StatefulLiveData<Unit> = requestApi.postBags(newBags)

    private fun saveBagsInDatabase(newBags: List<Bag>) {
        executor.submit {
            Log.d("Liad", "saveBagsInDatabase - $newBags")
            dao.insertBag(newBags)
        }
    }

    private fun getBagsStatefulLiveData(contactId: Long): StatefulLiveData<List<Bag>> {

        val statefulResult = MutableStatefulLiveData<List<Bag>>()
        statefulResult.putLoading()

        getBagsLiveData(contactId).observeOnce(Observer { dbBags ->
            if (dbBags.isNullOrEmpty()) {
                statefulResult.putData(listOf(Bag(), Bag(), Bag()))
            } else {
                statefulResult.putData(dbBags)
            }
        })
        statefulResult.putData(emptyList())
        return statefulResult
    }

    // get Bag from DB
    private fun getBagsLiveData(contactId: Long): LiveData<List<Bag>> = dao.getBags(contactId)

    /** End of Bags functions */


    // get Drop review from Class members
    override fun getDropReview(): LiveData<DropReview> = dropReviewMutableLiveData

}