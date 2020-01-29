package com.liad.droptask.di

import com.liad.droptask.server_connection.RequestApi
import com.liad.droptask.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class NetworkModule {

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */

    @Provides
    internal fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides the RequestApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the RequestApi service implementation.
     */

    @Provides
    internal fun provideRequestApi(retrofit: Retrofit): RequestApi{
        return retrofit.create(RequestApi::class.java)
    }


}