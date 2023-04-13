package com.dogukanincee.ormlite_implementation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dogukanincee.ormlite_implementation.model.Person

/**
 * An adapter for displaying a list of persons in a RecyclerView.
 */
class PersonAdapter :
    ListAdapter<Person, PersonAdapter.PersonViewHolder>(DiffCallback()) {

    /**
     * Creates a new ViewHolder by inflating the "person_item" layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return PersonViewHolder(itemView)
    }

    /**
     * Binds a person to a ViewHolder by setting the name and age TextViews.
     */
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person)
        Log.i("PersonAdapter", "Binding person ${person.id} to ViewHolder")
    }

    /**
     * A ViewHolder that holds a reference to the name and age TextViews.
     */
    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        private val ageTextView = itemView.findViewById<TextView>(R.id.ageTextView)

        /**
         * Binds a person to this ViewHolder by setting the name and age TextViews.
         */
        fun bind(person: Person) {
            nameTextView.text = person.name
            ageTextView.text = person.age.toString()
        }
    }

    /**
     * Callback for calculating the difference between two lists of persons.
     */
    class DiffCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}
