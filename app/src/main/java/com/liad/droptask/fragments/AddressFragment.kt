package com.liad.droptask.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import co.climacell.statefulLiveData.core.StatefulData
import com.google.android.material.textfield.TextInputLayout
import com.liad.droptask.R
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.extensions.toast
import com.liad.droptask.extensions.validate
import com.liad.droptask.models.Address
import com.liad.droptask.viewmodels.AddressFragViewModel
import kotlinx.android.synthetic.main.fragment_address.*
import org.koin.android.ext.android.inject


class AddressFragment : Fragment() {


    private lateinit var streetTextInputLayout: TextInputLayout
    private lateinit var cityTextInputLayout: TextInputLayout
    private lateinit var countryTextInputLayout: TextInputLayout
    private lateinit var progressBar: ProgressBar

    private val addressViewModel: AddressFragViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addressViewModel.statefulLiveDataAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is StatefulData.Success -> {
                    progressBar.visibility = View.GONE
                    updateAddress(it.data)
                }
                is StatefulData.Loading -> progressBar.visibility = View.VISIBLE
                is StatefulData.Error -> toast(activity!!, "${it.throwable}")
            }
        })

        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    private fun updateAddress(address: Address?) {
        streetTextInputLayout.editText?.setText(address?.street)
        cityTextInputLayout.editText?.setText(address?.city)
        countryTextInputLayout.editText?.setText(address?.country)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListeners()
    }

    private fun initView() {
        streetTextInputLayout = address_fragment_street_til
        cityTextInputLayout = address_fragment_city_til
        countryTextInputLayout = address_fragment_country_til
        progressBar = address_fragment_progress_bar
    }

    private fun setListeners() {
        address_fragment_submit_button.setOnClickListener {
            if (validateFields()) {
                addressViewModel.insertAddress(
                    Address(
                        streetTextInputLayout.editText?.text.toString(),
                        cityTextInputLayout.editText?.text.toString(),
                        countryTextInputLayout.editText?.text.toString()
                    )
                )
                changeFragment(
                    activity!!.supportFragmentManager, R.id.main_activity_frame_layout, BagsFragment.newInstance(), true
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        return streetTextInputLayout.validate() && cityTextInputLayout.validate() && countryTextInputLayout.validate()
    }


    companion object {
        fun newInstance(): AddressFragment {
            return AddressFragment()
        }
    }

}
