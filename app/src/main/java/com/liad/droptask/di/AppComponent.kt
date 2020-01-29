package com.liad.droptask.di

import com.liad.droptask.fragments.AddressFragment
import com.liad.droptask.fragments.BagsFragment
import com.liad.droptask.fragments.ContactFragment
import com.liad.droptask.fragments.ReviewDropFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelFactoryModule::class])
interface AppComponent {

    fun inject(contactFragment: ContactFragment)
    fun inject(addressFragment: AddressFragment)
    fun inject(bagsFragment: BagsFragment)
    fun inject(reviewDropFragment: ReviewDropFragment)
}