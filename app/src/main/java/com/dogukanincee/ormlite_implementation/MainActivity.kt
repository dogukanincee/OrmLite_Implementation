package com.dogukanincee.ormlite_implementation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dogukanincee.ormlite_implementation.model.Person
import com.dogukanincee.ormlite_implementation.view_model.PersonViewModel

/**
 * The main activity of the application.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PersonViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var clearButton: Button
    private lateinit var scrollView: ScrollView
    private lateinit var personsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the ViewModel instance
        viewModel = ViewModelProvider(this)[PersonViewModel::class.java]
        viewModel.lifecycleOwner = this

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        saveButton = findViewById(R.id.saveButton)
        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        clearButton = findViewById(R.id.clearButton)
        scrollView = findViewById(R.id.scrollView)
        personsTextView = findViewById(R.id.personsTextView)

        // Set up the RecyclerView
        val adapter = PersonAdapter()
        recyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel and update the RecyclerView and TextView when it changes
        viewModel.personList.observe(this) { persons ->
            adapter.submitList(persons)
            Log.i("MainActivity", "List of persons updated: $persons")
            val text = persons.joinToString(separator = "\n") { "${it.name}, ${it.age}" }
            personsTextView.text = text
        }

        // Set up the Save button
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()

            if (name.matches("[a-zA-Z]+".toRegex()) && age != null) {
                // Insert the new person into the database and update the RecyclerView and TextView
                viewModel.insertPerson(Person(name = name, age = age))
                nameEditText.text.clear()
                ageEditText.text.clear()
                Log.i("MainActivity", "New person added: $name, $age")
            } else {
                // Show an error message if the name or age is invalid
                Toast.makeText(this, "Please enter a valid name and age", Toast.LENGTH_SHORT).show()
                nameEditText.text.clear()
                ageEditText.text.clear()
            }
        }

        // Set up the Clear button
        clearButton.setOnClickListener {
            val persons = viewModel.personList.value

            if (persons != null && persons.isNotEmpty()) {
                // Clear the database and update the RecyclerView and TextView
                viewModel.clearDatabase()
                Log.i("MainActivity", "Database cleared")
            } else {
                // Show a message if the database has no entries
                Toast.makeText(this, "There are no entries in the database", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Adjust the RecyclerView height to avoid overlapping with the other views
        recyclerView.post {
            val layoutParams = recyclerView.layoutParams
            layoutParams.height = scrollView.height - personsTextView.height
            recyclerView.layoutParams = layoutParams
        }
    }
}