package com.liad.droptask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.climacell.statefulLiveData.core.StatefulData
import com.liad.droptask.R
import com.liad.droptask.adapters.BagsAdapter
import com.liad.droptask.models.Bag
import com.liad.droptask.utils.extensions.changeFragment
import com.liad.droptask.utils.extensions.toast
import com.liad.droptask.viewmodels.BagsFragViewModel
import kotlinx.android.synthetic.main.fragment_bags.*
import org.koin.android.ext.android.inject

class BagsFragment : Fragment() {

    companion object {
        fun newInstance(): BagsFragment {
            return BagsFragment()
        }
    }

    private val viewModel: BagsFragViewModel by inject()
    private val bagsAdapter: BagsAdapter = BagsAdapter().apply { listener = createAdapterListener() }
    private val selectedBags: MutableList<Bag> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_bags, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        viewModel.bagsStatefulLiveData.observe(this, Observer {
            when (it) {
                is StatefulData.Success -> updateBags(it.data)
                is StatefulData.Loading -> {
                }
                is StatefulData.Error -> {
                }
            }
        })
    }

    private fun updateBags(bags: List<Bag>) {
        bagsAdapter.setBags(bags)
    }

    private fun initView() {
        bags_fragment_recycler_view.apply {
            adapter = bagsAdapter
            activity?.let { layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false) }
        }
    }

    private fun setListeners() {
        bags_fragment_submit_button.setOnClickListener {
            submitBtnOnClickListener()
        }
    }

    private fun createAdapterListener() = object : BagsAdapter.IBagClickListener {
        override fun onBagClickListener(bag: Bag) {
            updateSelectedBags(bag)
        }
    }

    private fun submitBtnOnClickListener() {
        if (!selectedBags.isNullOrEmpty()) {
            viewModel.addBags(selectedBags)
            activity?.let {
                changeFragment(
                    it.supportFragmentManager,
                    R.id.main_activity_frame_layout,
                    DropReviewFragment.newInstance(),
                    true
                )
            }
        } else {
            activity?.let { toast(it, "Please choose at least one bag to continue") }
        }
    }

    private fun updateSelectedBags(bag: Bag) {
        if (bag.isAdded) selectedBags.add(bag)
        else selectedBags.remove(bag)
    }

}
