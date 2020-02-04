package com.liad.droptask.server_connection

import co.climacell.statefulLiveData.core.StatefulLiveData
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RequestApi {

    @GET("/user/contact")
    fun getContact(): StatefulLiveData<Contact>

    @Headers("Content-Type:application/json")
    @POST("/user/contact")
    fun postContact(@Body contact: Contact): StatefulLiveData<Unit>

    @GET("/user/address")
    fun getAddress(): StatefulLiveData<Address>

    @POST("/user/address")
    fun postAddress(@Body address: Address): StatefulLiveData<Unit>

    @POST("/drop")
    fun postBags(@Body bags: List<Bag>): StatefulLiveData<Unit>

}