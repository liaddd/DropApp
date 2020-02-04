package com.liad.droptask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.climacell.statefulLiveData.core.subscribe
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.liad.droptask.R
import com.liad.droptask.adapters.ContactAdapter
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.extensions.validate
import com.liad.droptask.models.Contact
import com.liad.droptask.models.Phone
import com.liad.droptask.viewmodels.ContactFragViewModel
import kotlinx.android.synthetic.main.fragment_contact_details.*
import org.koin.android.ext.android.inject

class ContactFragment : Fragment() {

    private lateinit var fullNameTextInputLayout: TextInputLayout
    private lateinit var phoneTextInputLayout: TextInputLayout

    private lateinit var progressBar: ProgressBar
    private lateinit var phoneUnderline: View
    private lateinit var countryCodePicker: CountryCodePicker

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView

    private val contactList: MutableList<Contact> = mutableListOf()

    private val contactViewModel: ContactFragViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contact_details, container, false)

        contactViewModel.statefulLiveDataContact.subscribe(viewLifecycleOwner)
            .onSuccess {
                updateContact(it)
                progressBar.visibility = View.GONE
            }.onLoading {
                progressBar.visibility = View.VISIBLE
            }.onError {
                progressBar.visibility = View.GONE
            }.observe()

        contactViewModel.statefulLiveDataContactList.subscribe(viewLifecycleOwner)
            .onSuccess {
                if (it.size > 1) contactAdapter.setContacts(it)
            }.observe()

        return view
    }

    fun updateContact(contact: Contact) {
        contact_details_fragment_fname_tie.setText(contact.fullName)
        countryCodePicker.setCountryForPhoneCode(contact.phoneNumber.countryCode)
        contact_details_fragment_phone_tie.setText(contact.phoneNumber.number)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListeners()
    }

    private fun initView() {
        fullNameTextInputLayout = contact_details_fragment_fname_til
        phoneTextInputLayout = contact_details_fragment_phone_til

        progressBar = contact_details_fragment_progress_bar
        phoneUnderline = contact_details_fragment_phone_underline_view
        countryCodePicker = contact_details_fragment_country_code_picker

        contactAdapter = ContactAdapter(
            this,
            contactList
            // todo Liad - delete in production
            /*listOf(
                Contact("Liad h", Phone(972, "547320099")),
                Contact("Dani cohen", Phone(44, "123456784")),
                Contact()
            )*/
        )
        recyclerView = contact_details_fragment_recycler_view
        recyclerView.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        }

    }

    private fun setListeners() {
        contact_details_fragment_submit_button.setOnClickListener {
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
                changeFragment(
                    activity!!.supportFragmentManager,
                    R.id.main_activity_frame_layout,
                    AddressFragment.newInstance(),
                    true
                )
            }
        }

        // toggle phone edit text underline view state
        phoneTextInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.height = if (hasFocus) 6 else 3
            phoneUnderline.layoutParams = layoutParams

            phoneUnderline.setBackgroundColor(resources.getColor(if (hasFocus) R.color.colorAccent else R.color.phone_underline))
        }
    }

    private fun validateFields(): Boolean {
        return (fullNameTextInputLayout.validate() && phoneTextInputLayout.validate())
    }

    companion object {
        fun newInstance(): ContactFragment {
            return ContactFragment()
        }
    }

}
