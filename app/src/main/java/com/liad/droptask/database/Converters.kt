package com.liad.droptask.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import com.liad.droptask.models.Phone

class Converters {


     // statefulLiveDataContact converter
     @TypeConverter
     fun stringToContact(data: String?): Contact = Gson().fromJson(data, Contact::class.java)

     @TypeConverter
     fun contactToString(contact: Contact?): String = Gson().toJson(contact)

     // phone converter
     @TypeConverter
     fun stringToPhone(data: String?): Phone = Gson().fromJson(data, Phone::class.java)

     @TypeConverter
     fun phoneToString(phone: Phone?): String = Gson().toJson(phone)

     // address converter
     @TypeConverter
     fun stringToAddress(data: String?): List<Address> {
         return Gson().fromJson(data, object: TypeToken<List<Address>>(){}.type)
     }

     @TypeConverter
     fun addressToString(address: Address?): String = Gson().toJson(address)


     // bags converter
     @TypeConverter
     fun stringToBag(data: String?): List<Bag> {
          return Gson().fromJson(data, object: TypeToken<List<Bag>>(){}.type)
     }

     @TypeConverter
     fun bagToString(bag: Bag?): String = Gson().toJson(bag)

}