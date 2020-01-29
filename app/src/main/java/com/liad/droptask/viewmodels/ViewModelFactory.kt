package com.liad.droptask.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.liad.droptask.repositories.DropRepository
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val repository: DropRepository,
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        viewModels.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return when(modelClass) {
            ContactFragViewModel::class.java -> ContactFragViewModel(repository) as T
            AddressFragViewModel::class.java -> AddressFragViewModel(repository) as T
            BagsFragViewModel::class.java -> BagsFragViewModel(repository) as T
            else -> throw RuntimeException("cannot find view model $modelClass")
        }
    }
}