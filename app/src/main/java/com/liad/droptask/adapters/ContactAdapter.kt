package com.liad.droptask.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.liad.droptask.R
import com.liad.droptask.extensions.clearAndAddAll
import com.liad.droptask.extensions.inflate
import com.liad.droptask.models.Contact

class ContactAdapter :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var listener: IContactsListener? = null
    private val contacts = mutableListOf<Contact>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.contact_list_item))
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]

        val context = holder.itemView.context
        holder.fullNameTextView.text = context.getString(R.string.fullname, contact.fullName)
        holder.phoneTextView.text =
            context.getString(R.string.phone, contact.phoneNumber.countryCode, contact.phoneNumber.number)
        holder.cardView.setOnClickListener {
            listener?.onContactClicked(contact)
        }
    }

    fun setContacts(contacts: List<Contact>) {
        this.contacts.clearAndAddAll(contacts)
        notifyDataSetChanged()
    }

    fun getContactAt(position: Int): Contact {
        return contacts[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fullNameTextView: TextView = itemView.findViewById(R.id.contact_list_item_fullname_text_view)
        val phoneTextView: TextView = itemView.findViewById(R.id.contact_list_item_phone_text_view)
        val cardView: CardView = itemView.findViewById(R.id.contact_list_item_card_view)
    }

    interface IContactsListener {
        fun onContactClicked(contact: Contact)
    }
}