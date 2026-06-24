package com.mashiverse.mashit.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashiverse.mashit.data.models.mashup.MashupDetails

@Entity(tableName = "mashups")
data class MashupEntity(
    @PrimaryKey
    val wallet: String,
    val mashup: MashupDetails
)