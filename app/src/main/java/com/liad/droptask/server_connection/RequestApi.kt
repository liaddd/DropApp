package com.liad.droptask.server_connection

import com.liad.droptask.models.Contact
import retrofit2.http.Headers
import retrofit2.http.POST


interface RequestApi {

    @Headers("Content-Type:application/json")
    @POST("/user/contact")
    fun postContact(contact: Contact)
}