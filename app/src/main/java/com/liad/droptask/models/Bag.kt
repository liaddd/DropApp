package com.liad.droptask.models

import androidx.room.Entity

@Entity(tableName = "bags_table")
data class Bag(val tag: String = "DA12345"){

    var isAdded: Boolean = false
    override fun toString(): String {
        return "tag='$tag'"
    }


}