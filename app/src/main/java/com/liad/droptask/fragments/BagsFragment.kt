package com.liad.droptask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.climacell.statefulLiveData.core.StatefulData
import com.liad.droptask.R
import com.liad.droptask.adapters.BagsAdapter
import com.liad.droptask.extensions.changeFragment
import com.liad.droptask.models.Bag
import com.liad.droptask.viewmodels.BagsFragViewModel
import kotlinx.android.synthetic.main.fragment_bags.*
import org.koin.android.ext.android.inject

class BagsFragment : Fragment() {

    private val viewModel: BagsFragViewModel by inject()
    private lateinit var bagsAdapter: BagsAdapter
    private var selectedBags: MutableList<Bag> = mutableListOf()
    private val bagsList = listOf(Bag(), Bag(), Bag())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.bagsStatefulLiveData.observe(this, Observer {
            when (it) {
                is StatefulData.Success -> updateBags(it.data)
                is StatefulData.Loading -> {}
                is StatefulData.Error -> {}
            }
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
        bagsAdapter = BagsAdapter(this, bagsList)
        bags_fragment_recycler_view.apply {
            adapter = bagsAdapter
            layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        }
    }

    private fun setListeners() {
        bags_fragment_submit_button.setOnClickListener {
            if (selectedBags.isNotEmpty()) {
                viewModel.addBags(selectedBags)
                changeFragment(
                    activity!!.supportFragmentManager,
                    R.id.main_activity_frame_layout,
                    DropReviewFragment.newInstance(),
                    true
                )
            } else {
                Toast.makeText(activity!!, "Please choose bags to continue", Toast.LENGTH_LONG).show()
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
