package com.liad.droptask.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact constructor(
    var fullName: String,
    var phoneNumber: Phone
) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0
    constructor() : this("", Phone(0, ""))


    override fun toString(): String {
        return /*"id='$id',\n" +*/"fullName='$fullName',\n" +
                "phoneNumber='$phoneNumber'"
    }
}
