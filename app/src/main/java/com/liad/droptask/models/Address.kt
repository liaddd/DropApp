package com.liad.droptask.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

@Entity(tableName = "address_table")
data class Address @Inject constructor(
    @SerializedName("streetAddress") val street: String,
    val city: String,
    val country: String
) {

    override fun toString(): String {
        return "street='$street',\n" +
                "city='$city',\n" +
                "country='$country')"
    }
}