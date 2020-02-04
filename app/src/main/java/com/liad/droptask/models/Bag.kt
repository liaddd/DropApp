package com.liad.droptask.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "bags_table")
data class Bag(@PrimaryKey val tag: String = "DA${Random.nextInt(10000 , 99999)}"){

    @Ignore
    var isAdded: Boolean = false

    override fun toString(): String {
        return "$tag"
    }


}