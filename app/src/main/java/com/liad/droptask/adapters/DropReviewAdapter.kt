package com.liad.droptask.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.utils.extensions.clearAndAddAll
import com.liad.droptask.utils.extensions.inflate
import com.liad.droptask.models.DropReview

class DropReviewAdapter :
    RecyclerView.Adapter<DropReviewAdapter.ViewHolder>() {


    private var dropReviews = mutableListOf<DropReview>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.drop_review_list_item))
    }

    override fun getItemCount(): Int {
        return dropReviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dropReview = dropReviews[position]

        val resources = holder.itemView.resources
        holder.fullNameTextView.text = resources.getString(R.string.full_name_formatted , dropReview.contact.fullName)
        holder.phoneTextView.text = resources.getString(R.string.phone_formatted , dropReview.contact.phoneNumber.countryCode , dropReview.contact.phoneNumber.number)
        holder.addressTextView.text =  resources.getString(R.string.address_formatted , dropReview.address)
        holder.bagsTextView.text = resources.getString(R.string.bags_formatted , dropReview.bags)
    }

    fun setDropReviews(newDropReviews: List<DropReview>) {
        this.dropReviews.clearAndAddAll(newDropReviews)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fullNameTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_full_name_text_view)
        val phoneTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_phone_text_view)
        val addressTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_address_text_view)
        val bagsTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_bags_text_view)
    }
}