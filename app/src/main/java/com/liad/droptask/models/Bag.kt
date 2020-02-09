package com.liad.droptask.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import kotlin.random.Random

@Entity(
    tableName = "bags_table", foreignKeys = [ForeignKey(
        entity = Contact::class,
        onUpdate = ForeignKey.CASCADE,
        parentColumns = ["id"],
        childColumns = ["contactId"]
    )]
)
data class Bag(val tag: String = "DA${Random.nextInt(10000, 99999)}") {


    @PrimaryKey
    @Expose
    var contactId: Long = 0

    @Ignore
    var isAdded: Boolean = false

    override fun toString(): String {
        return tag
    }


}