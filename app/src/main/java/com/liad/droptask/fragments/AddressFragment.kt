package com.liad.droptask.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.liad.droptask.R
import com.liad.droptask.di.DaggerAppComponent
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.models.Address
import com.liad.droptask.viewmodels.AddressFragViewModel
import com.liad.droptask.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_address.*
import javax.inject.Inject


class AddressFragment : Fragment() {


    private lateinit var streetTextInputLayout: TextInputLayout
    private lateinit var cityTextInputLayout: TextInputLayout
    private lateinit var countryTextInputLayout: TextInputLayout

    private lateinit var streetTextInputEditText: TextInputEditText
    private lateinit var cityTextInputEditText: TextInputEditText
    private lateinit var countryTextInputEditText: TextInputEditText

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var viewModel: AddressFragViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DaggerAppComponent.create().inject(this)

        viewModel = ViewModelProviders.of(this, factory).get(AddressFragViewModel::class.java)
        viewModel.getContact().observe(this, Observer {
            updateAddress(it.address)
        })

        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    private fun updateAddress(address: List<Address>) {
        streetTextInputEditText.setText(address[0].street)
        cityTextInputEditText.setText(address[0].city)
        countryTextInputEditText.setText(address[0].country)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListeners()
    }

    private fun initView() {
        streetTextInputLayout = address_fragment_street_til
        streetTextInputEditText = address_fragment_street_tie

        cityTextInputLayout = address_fragment_city_til
        cityTextInputEditText = address_fragment_city_tie

        countryTextInputLayout = address_fragment_country_til
        countryTextInputEditText = address_fragment_country_tie
    }

    private fun setListeners() {
        address_fragment_submit_button.setOnClickListener {
            val bagsFragment = BagsFragment.newInstance()
            changeFragment(
                activity!!.supportFragmentManager,
                R.id.main_activity_frame_layout,
                bagsFragment,
                true
            )
        }
    }


    companion object {
        fun newInstance(): AddressFragment {
            return AddressFragment()
        }
    }

}
