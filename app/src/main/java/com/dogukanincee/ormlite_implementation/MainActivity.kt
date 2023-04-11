package com.dogukanincee.ormlite_implementation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.dogukanincee.ormlite_implementation.databinding.ActivityMainBinding

/**
 * The main activity of the application.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is starting. This method initializes the view binding and the database helper, and sets up the click listeners for the save and retrieve buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)

        binding.saveButton.setOnClickListener {
            val person = Person(
                name = binding.nameEditText.text.toString(),
                age = binding.ageEditText.text.toString().toInt()
            )
            val dao = databaseHelper.getDao(Person::class.java)
            dao.create(person)
            Toast.makeText(this, "Person saved!", Toast.LENGTH_SHORT).show()
        }

        binding.retrieveButton.setOnClickListener {
            val dao = databaseHelper.getDao(Person::class.java)
            val persons = dao.queryForAll()
            binding.personsTextView.text = persons.joinToString(separator = "\n")
        }
    }

    /**
     * Called when the activity is destroyed. This method releases the database helper.
     */
    override fun onDestroy() {
        super.onDestroy()
        OpenHelperManager.releaseHelper()
    }
}