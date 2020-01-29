package com.liad.droptask.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.liad.droptask.R
import com.liad.droptask.di.DaggerAppComponent
import com.liad.droptask.models.Contact
import com.liad.droptask.viewmodels.ContactFragViewModel
import com.liad.droptask.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_review_drop.*
import javax.inject.Inject


class ReviewDropFragment : Fragment() {


    @Inject lateinit var factory: ViewModelFactory
    @Inject lateinit var viewModel : ContactFragViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DaggerAppComponent.create().inject(this)

        viewModel = ViewModelProviders.of(this , factory).get(ContactFragViewModel::class.java)
        viewModel.getContact().observe(this , Observer {
            if (it != null) showDropReview(it)
        })

        return inflater.inflate(R.layout.fragment_review_drop, container, false)
    }

    private fun showDropReview(contact: Contact) {
        review_drop_fragment_review_text_view.text = contact.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        review_drop_fragment_submit_button.setOnClickListener {
            for (i in 0..activity!!.supportFragmentManager.backStackEntryCount){
                activity!!.supportFragmentManager.popBackStack()
            }
        }
    }


    companion object {
        fun newInstance(): ReviewDropFragment {
            return ReviewDropFragment()
        }
    }

}
