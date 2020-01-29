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
    fun insertContact(contact: Contact)

    @Query("SELECT * FROM contact_table")
    fun getContact(): LiveData<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address)

    @Query("SELECT * FROM address_table")
    fun getAddress(): LiveData<Address>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBag(bag: Bag)

    @Query("SELECT * FROM bags_table")
    fun getBag(): LiveData<Bag>
}