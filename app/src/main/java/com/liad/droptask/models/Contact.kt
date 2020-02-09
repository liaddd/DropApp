package com.liad.droptask.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "contact_table")
data class Contact constructor(
    var fullName: String,
    var phoneNumber: Phone
) {


    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun toString(): String {
        return /*"id='$id',\n" +*/"fullName='$fullName',\n" +
                "phoneNumber='$phoneNumber'"
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contact) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(id)

}
