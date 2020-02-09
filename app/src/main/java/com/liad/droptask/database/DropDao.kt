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

    @Query("DELETE FROM contact_table WHERE id = :contactId")
    fun deleteContact(contactId: Long) : Int

    @Query("SELECT * FROM contact_table")
    fun getContacts(): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address): Long

    @Query("SELECT * FROM address_table WHERE contactId = :contactId")
    fun getAddress(contactId: Long): LiveData<Address?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBag(bags: List<Bag>)

    @Query("SELECT * FROM bags_table WHERE contactId = :contactId")
    fun getBags(contactId: Long): LiveData<List<Bag>>


}