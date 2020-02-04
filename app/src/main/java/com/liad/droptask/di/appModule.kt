package com.liad.droptask.di

import com.liad.droptask.DropApplication
import com.liad.droptask.database.DropDatabase
import com.liad.droptask.repositories.DropRepository
import com.liad.droptask.repositories.IDropRepository
import com.liad.droptask.viewmodels.AddressFragViewModel
import com.liad.droptask.viewmodels.BagsFragViewModel
import com.liad.droptask.viewmodels.ContactFragViewModel
import com.liad.droptask.viewmodels.DropReviewFragViewModel
import org.koin.dsl.module

val appModule = module {

    // single instance of Retrofit
    single { RetrofitImpl().getRetrofit() }

    // single instance of DropDatabase
    single { DropDatabase.getDatabase(DropApplication.instance) }

    // single instance of DropRepository
    single<IDropRepository> { DropRepository(get(), get()) }


    factory { ContactFragViewModel(get()) }
    factory { AddressFragViewModel(get()) }
    factory { BagsFragViewModel(get()) }
    factory { DropReviewFragViewModel(get()) }
}