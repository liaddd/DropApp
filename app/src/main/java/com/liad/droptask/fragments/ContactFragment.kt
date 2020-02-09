package com.liad.droptask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.climacell.statefulLiveData.core.subscribe
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.liad.droptask.R
import com.liad.droptask.adapters.ContactAdapter
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.extensions.toast
import com.liad.droptask.extensions.validate
import com.liad.droptask.models.Contact
import com.liad.droptask.models.Phone
import com.liad.droptask.viewmodels.ContactFragViewModel
import kotlinx.android.synthetic.main.fragment_contact_details.*
import org.koin.android.ext.android.inject

//private const val

class ContactFragment : Fragment() {

    companion object {
        fun newInstance(): ContactFragment {
            return ContactFragment()
        }
    }


    private lateinit var fullNameTextInputLayout: TextInputLayout
    private lateinit var phoneTextInputLayout: TextInputLayout

    private lateinit var progressBar: ProgressBar
    private lateinit var phoneUnderline: View
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var submitButton : Button

    private lateinit var recyclerView: RecyclerView

    private val contactAdapter: ContactAdapter = ContactAdapter().apply { listener = createAdapterListener() }

    private val contactViewModel: ContactFragViewModel by inject()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_contact_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setRecyclerView()
        setListeners()
        setObserver()
    }

    private fun setObserver() {
        contactViewModel.statefulLiveDataContactList.subscribe(viewLifecycleOwner)
            .onSuccess {
                updateContacts(it)
                progressBar.visibility = View.GONE
            }.onLoading {
                progressBar.visibility = View.VISIBLE
            }.onError {

            }.observe()
    }

    private fun updateContacts(contacts: List<Contact>) {
        updateContact(contacts[contacts.lastIndex])
        if (contacts.size > 1) {
            recyclerView.visibility = View.VISIBLE
            contactAdapter.setContacts(contacts)
        } else recyclerView.visibility = View.GONE
    }

    private fun updateContact(contact: Contact) {
        contact_details_fragment_fname_tie.setText(contact.fullName)
        countryCodePicker.setCountryForPhoneCode(contact.phoneNumber.countryCode)
        contact_details_fragment_phone_tie.setText(contact.phoneNumber.number)
    }

    private fun initView() {
        fullNameTextInputLayout = contact_details_fragment_fname_til
        phoneTextInputLayout = contact_details_fragment_phone_til

        progressBar = contact_details_fragment_progress_bar
        phoneUnderline = contact_details_fragment_phone_underline_view
        countryCodePicker = contact_details_fragment_country_code_picker
        submitButton = contact_details_fragment_submit_button

        recyclerView = contact_details_fragment_recycler_view
    }

    private fun setRecyclerView() {
        recyclerView.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.RIGHT) {
                        contactViewModel.deleteContact(contactAdapter.getContactAt(viewHolder.adapterPosition).id)
                        activity?.let {
                            toast(
                                it,
                                "Contact ${contactAdapter.getContactAt(viewHolder.adapterPosition).fullName} Deleted"
                            )
                        }
                    }
                }


            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun createAdapterListener(): ContactAdapter.IContactsListener {
        return object : ContactAdapter.IContactsListener {
            override fun onContactClicked(contact: Contact) {
                updateContact(contact)
            }
        }
    }

    private fun setListeners() {
        submitButton.setOnClickListener {
            onSubmitClick()
        }

        // toggle phone edit text underline view state
        phoneTextInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            onPhoneFocusChanged(hasFocus)
        }
    }

    private fun onPhoneFocusChanged(hasFocus: Boolean) {
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.height = if (hasFocus) 6 else 3 // TODO("move to constants")
        phoneUnderline.layoutParams = layoutParams

        phoneUnderline.setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                if (hasFocus) R.color.colorAccent else R.color.phone_underline,
                null
            )
        )
    }

    private fun onSubmitClick() {
        if (validateFields()) {
            contactViewModel.insertContact(
                Contact(
                    fullNameTextInputLayout.editText?.text.toString(),
                    Phone(
                        countryCodePicker.selectedCountryCode.toInt(),
                        phoneTextInputLayout.editText?.text.toString()
                    )
                )
            )
            activity?.let {
                changeFragment(
                    it.supportFragmentManager,
                    R.id.main_activity_frame_layout,
                    AddressFragment.newInstance(),
                    true
                )
            }
        }
    }

    // TODO("move Logic to view model)
    private fun validateFields(): Boolean {
        return (fullNameTextInputLayout.validate() && phoneTextInputLayout.validate())
    }

}
