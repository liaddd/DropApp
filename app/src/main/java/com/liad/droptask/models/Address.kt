package com.liad.droptask.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "address_table" , foreignKeys = [ForeignKey(
    entity = Contact::class,
    onUpdate = ForeignKey.CASCADE,
    parentColumns = ["id"],
    childColumns = ["contactId"]
)])
data class Address constructor(
    @SerializedName("streetAddress") val street: String,
    val city: String,
    val country: String
) {

    @PrimaryKey @Expose var contactId: Long = 0
    override fun toString(): String {
        return "\nstreet='$street'\ncity='$city'\ncountry='$country'"
    }

}