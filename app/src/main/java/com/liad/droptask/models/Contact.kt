package com.liad.droptask.models

import androidx.room.Entity
import javax.inject.Inject

@Entity(tableName = "contact_table")
data class Contact @Inject constructor(
    val fullName: String,
    val phoneNumber: Phone,
    val address: List<Address>,
    val bags: List<Bag>
) {

    override fun toString(): String {
        return "fullName='$fullName',\n" +
                "phoneNumber='$phoneNumber',\n" +
                "address=$address,\n" +
                "bags=$bags"
    }


}

data class Phone(val countryCode: Int, val number: String)

