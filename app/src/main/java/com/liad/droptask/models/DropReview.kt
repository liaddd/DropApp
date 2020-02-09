package com.liad.droptask.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drops_table")
data class DropReview(
    @PrimaryKey
    val contact: Contact,
    val address: Address,
    val bags: List<Bag>
) {

    override fun toString(): String {
        return "contact=$contact,\n address=$address\n, bags=$bags"
    }
}