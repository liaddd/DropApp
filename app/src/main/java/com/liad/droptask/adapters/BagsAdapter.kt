package com.liad.droptask.adapters

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


    //var onItemClick: ((Bag) -> Unit)? = null

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
            baseFragment!!.updateSelectedBags(bag)
            (it as CardView).setCardBackgroundColor(
                baseFragment.activity?.resources!!.getColor(
                    if (bags[position].isAdded) android.R.color.black
                    else android.R.color.white
                )
            )
            holder.textView.setTextColor(
                baseFragment.activity?.resources!!.getColor(
                    if (bags[position].isAdded) android.R.color.white
                    else android.R.color.black
                )
            )
        }
    }

    fun setBags(bags: List<Bag>) {
        this.bags = bags
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView = itemView.findViewById(R.id.bag_list_item_text_view)
        var cardView: CardView = itemView.findViewById(R.id.bag_list_item_card_view)

        init {
            /*itemView.setOnClickListener {
                onItemClick?.invoke(bags[adapterPosition])
            }*/
        }


    }
}