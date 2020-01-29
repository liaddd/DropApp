package com.liad.droptask.models

import androidx.room.Entity

@Entity(tableName = "drops_table")
data class DropReview (private val contact : Contact)