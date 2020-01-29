package com.liad.droptask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.liad.droptask.R
import com.liad.droptask.di.DaggerAppComponent
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.extensions.validate
import com.liad.droptask.models.Contact
import com.liad.droptask.viewmodels.ContactFragViewModel
import com.liad.droptask.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_contact_details.*
import javax.inject.Inject

class ContactFragment : Fragment() {

    private lateinit var fullNameTextInputLayout: TextInputLayout
    private lateinit var phoneTextInputLayout: TextInputLayout

    private lateinit var progressBar: ProgressBar
    private lateinit var phoneUnderline: View
    private lateinit var countryCodePicker: CountryCodePicker

    @Inject
    lateinit var contactFragViewModel: ContactFragViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DaggerAppComponent.create().inject(this)
        val view = inflater.inflate(R.layout.fragment_contact_details, container, false)

        contactFragViewModel = ViewModelProviders.of(this, factory).get(ContactFragViewModel::class.java)
        contactFragViewModel.getContact().observe(this, Observer {
            updateContact(it)
        })

        return view
    }

    private fun updateContact(contact: Contact) {
        fullNameTextInputLayout.editText?.setText(contact.fullName)
        phoneTextInputLayout.editText?.setText(contact.phoneNumber.number)
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
        countryCodePicker.registerCarrierNumberEditText(phoneTextInputLayout.editText)
    }

    private fun setListeners() {
        contact_details_fragment_submit_button.setOnClickListener {
            if (validateFields()) {
                val addressFragment = AddressFragment.newInstance()
                changeFragment(
                    activity!!.supportFragmentManager,
                    R.id.main_activity_frame_layout,
                    addressFragment,
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
