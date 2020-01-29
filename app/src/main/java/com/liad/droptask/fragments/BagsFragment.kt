package com.liad.droptask.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.adapters.BagsAdapter
import com.liad.droptask.di.DaggerAppComponent
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.models.Bag
import com.liad.droptask.viewmodels.BagsFragViewModel
import com.liad.droptask.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_bags.*
import javax.inject.Inject


class BagsFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var viewModel: BagsFragViewModel

    private lateinit var bagsAdapter: BagsAdapter

    private var selectedBags: MutableList<Bag> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DaggerAppComponent.create().inject(this)

        viewModel = ViewModelProviders.of(this, factory).get(BagsFragViewModel::class.java)
        viewModel.getContact().observe(this, Observer {
            updateBags(it.bags)
        })

        return inflater.inflate(R.layout.fragment_bags, container, false)
    }

    private fun updateBags(bags: List<Bag>) {
        bagsAdapter.setBags(bags)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListeners()
    }

    private fun initView() {
        bagsAdapter = BagsAdapter(this , emptyList())
        bags_fragment_recycler_view.apply {
            adapter = bagsAdapter
            layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        }
    }

    private fun setListeners() {
        bags_fragment_submit_button.setOnClickListener {
            if (selectedBags.isNotEmpty()) {
                val reviewDropFragment = ReviewDropFragment.newInstance()
                changeFragment(
                    activity!!.supportFragmentManager,
                    R.id.main_activity_frame_layout,
                    reviewDropFragment,
                    true
                )
            } else {
                Toast.makeText(activity!!, "Please choose bag to continue", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateSelectedBags(bag: Bag) {
        if (bag.isAdded) selectedBags.add(bag)
        else selectedBags.remove(bag)
    }


    companion object {
        fun newInstance(): BagsFragment {
            return BagsFragment()
        }
    }

}
