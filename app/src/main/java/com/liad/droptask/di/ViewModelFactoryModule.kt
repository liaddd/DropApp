package com.liad.droptask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.liad.droptask.viewmodels.AddressFragViewModel
import com.liad.droptask.viewmodels.BagsFragViewModel
import com.liad.droptask.viewmodels.ContactFragViewModel
import com.liad.droptask.viewmodels.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {

    @Singleton
    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(ContactFragViewModel::class)
    fun provideContactFragmentViewModel(viewModel: ContactFragViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(AddressFragViewModel::class)
    fun provideAddressFragmentViewModel(viewModel: AddressFragViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(BagsFragViewModel::class)
    fun provideBagsFragmentViewModel(viewModel: BagsFragViewModel): ViewModel = viewModel
}