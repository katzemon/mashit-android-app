package com.mashiverse.mashit.data.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mashiverse.mashit.data.models.mashup.MashupDetails

class MashupConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromMashupDetails(mashupDetails: MashupDetails?): String? {
        return gson.toJson(mashupDetails)
    }

    @TypeConverter
    fun toMashupDetails(json: String?): MashupDetails? {
        return gson.fromJson(json, MashupDetails::class.java)
    }
}