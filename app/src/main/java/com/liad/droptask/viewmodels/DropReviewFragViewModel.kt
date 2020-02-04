package com.liad.droptask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import co.climacell.statefulLiveData.core.mapToLiveData
import com.liad.droptask.models.Address
import com.liad.droptask.models.Bag
import com.liad.droptask.models.Contact
import com.liad.droptask.models.DropReview
import com.liad.droptask.repositories.IDropRepository

class DropReviewFragViewModel constructor(private val repository: IDropRepository) : ViewModel() {

    val dropReviewData: LiveData<DropReview?> = MediatorLiveData<DropReview?>().apply {
        val contactLiveData = repository.statefulLiveDataContact.mapToLiveData()
        val addressLiveData = repository.statefulLiveDataAddress.mapToLiveData()
        val bagsLiveData = repository.statefulLiveDataBags.mapToLiveData()

        addSource(contactLiveData) {
            this.value = createDropReviewData(it, addressLiveData.value, bagsLiveData.value)
        }
        addSource(addressLiveData) {
            this.value = createDropReviewData(contactLiveData.value, it, bagsLiveData.value)
        }
        addSource(bagsLiveData) {
            this.value = createDropReviewData(contactLiveData.value, addressLiveData.value, it)
        }
    }

    private fun createDropReviewData(contact: Contact?, address: Address?, bags: List<Bag>?): DropReview? {
        return if (contact != null && address != null && !bags.isNullOrEmpty()) {
            DropReview(contact, address, bags)
        } else {
            null
        }
    }

}