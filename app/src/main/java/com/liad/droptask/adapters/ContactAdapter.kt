package com.liad.droptask.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.extensions.inflate
import com.liad.droptask.fragments.ContactFragment
import com.liad.droptask.models.Contact

class ContactAdapter(private val contactFragment: ContactFragment, private var contacts: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.contact_list_item))
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]

        holder.fullNameTextView.text = "Full name: ${contact.fullName}"
        holder.phoneTextView.text = "Phone: +${contact.phoneNumber.countryCode}${contact.phoneNumber.number}"
        holder.cardView.setOnClickListener {
            contactFragment.updateContact(contact)
        }
    }

    fun setContacts(contacts: List<Contact>) {
        val contactsMutableList = mutableListOf<Contact>()
        for (contact in contacts) {
            contactsMutableList.add(contact)
        }
        this.contacts = contactsMutableList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fullNameTextView: TextView = itemView.findViewById(R.id.contact_list_item_fullname_text_view)
        val phoneTextView: TextView = itemView.findViewById(R.id.contact_list_item_phone_text_view)
        val cardView: CardView = itemView.findViewById(R.id.contact_list_item_card_view)
    }
}