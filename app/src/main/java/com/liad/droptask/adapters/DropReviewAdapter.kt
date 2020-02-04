package com.liad.droptask.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.extensions.inflate
import com.liad.droptask.models.DropReview

class DropReviewAdapter(private var dropReviews: List<DropReview>) :
    RecyclerView.Adapter<DropReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.drop_review_list_item))
    }

    override fun getItemCount(): Int {
        return dropReviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dropReview = dropReviews[position]

        holder.fullNameTextView.text = "Full name: ${dropReview.contact.fullName}"
        holder.phoneTextView.text =
            "Phone: +${dropReview.contact.phoneNumber.countryCode}${dropReview.contact.phoneNumber.number}"
        holder.addressTextView.text = "Address: ${dropReview.address}"
        holder.bagsTextView.text = "Bags: ${dropReview.bags}"
    }

    fun setDropReviews(dropReviews: List<DropReview>) {
        val dropReviewsMutableList = mutableListOf<DropReview>()
        for (dropReview in dropReviews) {
            dropReviewsMutableList.add(dropReview)
        }
        this.dropReviews = dropReviews
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fullNameTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_full_name_text_view)
        val phoneTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_phone_text_view)
        val addressTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_address_text_view)
        val bagsTextView: TextView = itemView.findViewById(R.id.drop_review_list_item_bags_text_view)
    }
}