package com.liad.droptask.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.models.Bag
import com.liad.droptask.utils.extensions.clearAndAddAll
import com.liad.droptask.utils.extensions.inflate

class BagsAdapter :
    RecyclerView.Adapter<BagsAdapter.ViewHolder>() {

    private val bagsList = mutableListOf<Bag>()
    var listener: IBagClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.bag_list_item))
    }

    override fun getItemCount(): Int {
        return bagsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bag: Bag = bagsList[position]

        holder.textView.text = holder.itemView.context.getString(R.string.item, bag.tag)

        holder.cardView.setOnClickListener {
            bag.isAdded = !bag.isAdded
            listener?.onBagClickListener(bag)
            toggleItemState(holder, bag)
        }

        if (bag.isAdded) toggleItemState(holder, bag)
    }

    private fun toggleItemState(holder: ViewHolder, bag: Bag) {
        val context = holder.itemView.context
        holder.cardView.setCardBackgroundColor(
            context.resources.getColor(
                if (bag.isAdded) android.R.color.black
                else android.R.color.white
            )
        )
        holder.textView.setTextColor(
            context.resources.getColor(
                if (bag.isAdded) android.R.color.white
                else android.R.color.black
            )
        )
    }

    fun setBags(bags: List<Bag>) {
        this.bagsList.clearAndAddAll(bags)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView = itemView.findViewById(R.id.bag_list_item_text_view)
        var cardView: CardView = itemView.findViewById(R.id.bag_list_item_card_view)
    }

    interface IBagClickListener {
        fun onBagClickListener(bag: Bag)
    }
}