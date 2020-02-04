package com.liad.droptask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact

@Dao
interface DropDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact): Long

    @Query("SELECT * FROM contact_table")
    fun getContact(): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address): Long

    @Query("SELECT * FROM address_table WHERE address_table.contactId = :contactId")
    fun getAddress(contactId : String): LiveData<Address?>

    /*@Query("SELECT * FROM contact_table , address_table WHERE contact_table.id = address_table.contactId")
    fun getContactWithAddress(): LiveData<List<ContactWithAddress>>*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBag(bags: List<Bag>)

    @Query("SELECT * FROM bags_table")
    fun getBag(): LiveData<List<Bag>?>

}