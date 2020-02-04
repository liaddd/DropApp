package com.liad.droptask.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import co.climacell.statefulLiveData.core.*
import com.liad.droptask.database.DropDao
import com.liad.droptask.database.DropDatabase
import com.liad.droptask.extensions.observeOnceNullable
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import com.liad.droptask.models.DropReview
import com.liad.droptask.server_connection.RequestApi
import retrofit2.Retrofit
import java.util.concurrent.Executors


class DropRepository(dropDatabase: DropDatabase, retrofit: Retrofit) : IDropRepository {

    override val statefulLiveDataContact: StatefulLiveData<Contact>
    override val statefulLiveDataContactList: MutableStatefulLiveData<List<Contact>> = MutableStatefulLiveData()
    override val statefulLiveDataAddress: StatefulLiveData<Address>
    override val statefulLiveDataBags: StatefulLiveData<List<Bag>> = MutableStatefulLiveData()

    private val contacts: MutableList<Contact>
    private val executor = Executors.newSingleThreadExecutor()
    private val dropReviewMutableLiveData: MutableLiveData<DropReview> = MutableLiveData()
    private var dao: DropDao
    private var apiRequest: RequestApi

    init {
        Log.d("Liad", "repository initialized: $this")
        dao = dropDatabase.dao()
        apiRequest = retrofit.create(RequestApi::class.java)

        statefulLiveDataContact = getContactStatefulLiveData()
        statefulLiveDataAddress = getAddressStatefulLiveData()

        contacts = mutableListOf()
    }

    /** Contact functions */

    // insert Contact to DB
    override fun insertContact(newContact: Contact) {
        Log.d("Liad", "insertContact()")
        val possibleSuccessData = (statefulLiveDataContact.value as? StatefulData.Success)?.data

        val mutableContactStatefulLiveData = statefulLiveDataContact as? MutableStatefulLiveData<Contact>
        // check if current contact != new contact added && new contact added doesn't exist in contacts yet
        if (possibleSuccessData != newContact && !contacts.contains(newContact)) {
            postContactToApi(newContact).observeOnce(Observer {
                when (it) {
                    is StatefulData.Success -> {
                        mutableContactStatefulLiveData?.putData(newContact)
                        saveContactInDatabase(newContact)
                        updateListContactStatefulLiveData(newContact)
                    }
                    is StatefulData.Loading -> mutableContactStatefulLiveData?.putLoading(
                        it.loadingData
                    )
                    is StatefulData.Error -> mutableContactStatefulLiveData?.putError(it.throwable)
                }
            })
        } else if (possibleSuccessData != newContact && contacts.contains(newContact)) {
            mutableContactStatefulLiveData?.putData(newContact)
        }
    }

    // get Contact from DB
    private fun getContactLiveData(): LiveData<List<Contact>> = dao.getContact()

    // get Contact from DB or API
    private fun getContactStatefulLiveData(): StatefulLiveData<Contact> {
        val statefulResult = MutableStatefulLiveData<Contact>()
        statefulResult.putLoading()

        // fetching from DB
        getContactLiveData().observeOnceNullable(Observer { dbContact ->
            Log.d("Liad", "dbContact: $dbContact")
            if (dbContact.isNullOrEmpty()) {
                // fetching from Api
                getContactFromApi().observeOnce(Observer { apiContact ->
                    statefulResult.putValue(apiContact)
                    if (apiContact is StatefulData.Success) {
                        updateListContactStatefulLiveData(apiContact.data)
                        saveContactInDatabase(apiContact.data)
                    }
                })
            } else {
                for (contact in dbContact) {
                    contacts.add(contact)
                }
                statefulLiveDataContactList.putData(contacts)
                statefulResult.putData(contacts[contacts.size - 1])
            }
        })

        return statefulResult
    }

    private fun updateListContactStatefulLiveData(contact: Contact) {
        contacts.add(contact)
        statefulLiveDataContactList.putData(contacts)
    }

    // Api request - POST CONTACT
    private fun postContactToApi(contact: Contact): StatefulLiveData<Unit> {
        Log.d("Liad", "Posting Contact to API $contact")
        return apiRequest.postContact(contact)/*.map {
            saveContactInDatabase(contactId)
        }*/
    }

    private fun saveContactInDatabase(contact: Contact) {
        executor.submit {
            Log.d("Liad", "saveContactInDatabase: $contact")
            val value = dao.insertContact(contact)
            Log.d("Liad", "insertion value: $value")
        }
    }

    // Api request - GET CONTACT
    private fun getContactFromApi() = apiRequest.getContact()

    /** End of Contact functions */


    /** Address functions */

    // insert Address to DB
    override fun insertAddress(newAddress: Address) {
        Log.d("Liad", "insertAddress()")
        val possibleSuccessData = (statefulLiveDataAddress.value as? StatefulData.Success)?.data
        val currentContactId = (statefulLiveDataContact.value as? StatefulData.Success)?.data!!.id
        newAddress.contactId = currentContactId
        Log.d("Liad", "newAddress.contactId ${newAddress.contactId} currentContactId $currentContactId")
        if (possibleSuccessData != newAddress /*&& isChanged*/) {
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
    private fun getAddressLiveData(): LiveData<Address?> = dao.getAddress((statefulLiveDataContact.value as? StatefulData.Success<Contact>)?.data?.id.toString())

    private fun getAddressStatefulLiveData(): StatefulLiveData<Address> {
        val statefulResult = MutableStatefulLiveData<Address>()
        statefulResult.putLoading()

        getAddressLiveData().observeOnceNullable(Observer { dbAddress ->
            if (dbAddress == null) {
                getAddressFromApi().observeOnce(Observer { apiAddress ->
                    statefulResult.putValue(apiAddress)
                    if (apiAddress is StatefulData.Success) {
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
        return apiRequest.postAddress(newAddress).map {
            saveAddressInDatabase(newAddress)
        }
    }

    // Api request - GET ADDRESS
    private fun getAddressFromApi() = apiRequest.getAddress()

    private fun saveAddressInDatabase(newAddress: Address) {
        executor.submit {
            newAddress.contactId = (statefulLiveDataContact.value as? StatefulData.Success<Contact>)?.data?.id ?: 0
            Log.d("Liad", "saveAddressInDatabase - $newAddress")
            val value = dao.insertAddress(newAddress)
            Log.d("Liad", "insertion value: $value")
        }
    }

    /** End of Address functions */


    /** Bags functions */

    // insert Bag to DB
    override fun insertBags(newBags: List<Bag>) {

        Log.d("Liad", "insertContact()")
        val possibleSuccessData = (statefulLiveDataBags.value as? StatefulData.Success)?.data

        if (possibleSuccessData != newBags) {
            postBagsToApi(newBags).observeOnce(Observer {
                when (it) {
                    is StatefulData.Success -> {
                        (statefulLiveDataBags as? MutableStatefulLiveData<List<Bag>>)?.putData(newBags)
                        saveBagsInDatabase(newBags)
                    }
                    is StatefulData.Loading -> (statefulLiveDataBags as? MutableStatefulLiveData<List<Bag>>)?.putLoading(
                        it.loadingData
                    )
                    is StatefulData.Error -> (statefulLiveDataBags as? MutableStatefulLiveData<List<Bag>>)?.putError(it.throwable)
                }
            })
        }
    }

    private fun postBagsToApi(newBags: List<Bag>): StatefulLiveData<Unit> {
        Log.d("Liad", "posting statefulLiveDataBags to API")
        return apiRequest.postBags(newBags).map {
            saveBagsInDatabase(newBags)
        }
    }

    private fun saveBagsInDatabase(newBags: List<Bag>) {
        executor.submit {
            Log.d("Liad", "saveBagsInDatabase - $newBags")
            dao.insertBag(newBags)
        }
    }

    // get Bag from DB
    fun getBags(): LiveData<List<Bag>?> = dao.getBag()

    /** End of Bags functions */


    // get Drop review from Class members
    override fun getDropReview(): LiveData<DropReview> = dropReviewMutableLiveData

}