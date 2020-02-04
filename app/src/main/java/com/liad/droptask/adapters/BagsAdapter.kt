package com.liad.droptask.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.extensions.inflate
import com.liad.droptask.fragments.BagsFragment
import com.liad.droptask.models.Bag

class BagsAdapter(private val baseFragment: BagsFragment?, private var bags: List<Bag>) :
    RecyclerView.Adapter<BagsAdapter.ViewHolder>() {

    val bagsMutableList = mutableListOf<Bag>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.bag_list_item))
    }

    override fun getItemCount(): Int {
        return bags.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bag: Bag = bags[position]

        holder.textView.text = "item: ${bag.tag}"

        holder.cardView.setOnClickListener {
            bag.isAdded = !bag.isAdded
            Log.d("Liad", "${bag.isAdded}")
            baseFragment?.updateSelectedBags(bag)
            toggleItemState(holder, bag)
        }

        if (bag.isAdded) toggleItemState(holder, bag)
    }

    private fun toggleItemState(holder: ViewHolder, bag: Bag) {
        holder.cardView.setCardBackgroundColor(
            baseFragment?.activity?.resources!!.getColor(
                if (bag.isAdded) android.R.color.black
                else android.R.color.white
            )
        )
        holder.textView.setTextColor(
            baseFragment.activity?.resources!!.getColor(
                if (bag.isAdded) android.R.color.white
                else android.R.color.black
            )
        )
    }

    fun setBags(bags: List<Bag>) {
        val bagMutableList = mutableListOf<Bag>()
        for (bag in bags) {
            bagMutableList.add(bag)
        }
        this.bags = bagMutableList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView = itemView.findViewById(R.id.bag_list_item_text_view)
        var cardView: CardView = itemView.findViewById(R.id.bag_list_item_card_view)
    }
}