package com.liad.droptask.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.adapters.DropReviewAdapter
import com.liad.droptask.models.DropReview
import com.liad.droptask.viewmodels.DropReviewFragViewModel
import kotlinx.android.synthetic.main.fragment_review_drop.*
import org.koin.android.ext.android.inject


class DropReviewFragment : Fragment() {

    private val viewModel: DropReviewFragViewModel by inject()
    private lateinit var dropReviewAdapter: DropReviewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.dropReviewData.observe(viewLifecycleOwner, Observer {
            review_drop_fragment_progress_bar.visibility = View.VISIBLE
            if (it != null) {
                showDropReview(it)
                review_drop_fragment_progress_bar.visibility = View.GONE
            }
        })

        return inflater.inflate(R.layout.fragment_review_drop, container, false)
    }

    private fun showDropReview(review: DropReview) {
        dropReviewAdapter.setDropReviews(listOf(review))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        review_drop_fragment_submit_button.setOnClickListener {
            for (i in 0..activity!!.supportFragmentManager.backStackEntryCount) {
                activity!!.supportFragmentManager.popBackStack()
            }
        }

        dropReviewAdapter = DropReviewAdapter(emptyList())
        recyclerView = review_drop_fragment_recycler_view.apply {
            adapter = dropReviewAdapter
            layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        }
    }

    companion object {
        fun newInstance(): DropReviewFragment {
            return DropReviewFragment()
        }
    }

}
