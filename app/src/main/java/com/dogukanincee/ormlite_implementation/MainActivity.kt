package com.dogukanincee.ormlite_implementation

import DatabaseHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.dogukanincee.ormlite_implementation.databinding.ActivityMainBinding
import com.j256.ormlite.table.TableUtils
import java.sql.SQLException

/**
 * The main activity of the application.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is starting. This method initializes the view binding and the database helper,
     * and sets up the click listeners for the save, retrieve, and clear database buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     * then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)

        // Add input filters to nameEditText to allow only alphabetical characters
        binding.nameEditText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.toString().filter { it.isLetter() }
        })

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val age = binding.ageEditText.text.toString().trim()

            if (name.isBlank()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (age.isBlank() || age.toIntOrNull()?.let { it in 0..99 } != true) {
                Toast.makeText(this, "Please enter a valid age (0-99)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dao = databaseHelper.getDao(Person::class.java)

            val existingPersons = dao.queryForEq("name", name)

            if (existingPersons.isEmpty()) {
                val person = Person(
                    name = name,
                    age = age.toInt()
                )
                dao.create(person)
                Toast.makeText(this, "Person saved!", Toast.LENGTH_SHORT).show()
            } else {
                val sameAgePerson = existingPersons.find { it.age == age.toIntOrNull() }
                if (sameAgePerson != null) {
                    Toast.makeText(
                        this,
                        "Person with the same name and age already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val person = Person(
                        name = name,
                        age = age.toInt()
                    )
                    dao.create(person)
                    Toast.makeText(this, "Person saved!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.retrieveButton.setOnClickListener {
            val dao = databaseHelper.getDao(Person::class.java)
            val persons = dao.queryForAll()
            binding.personsTextView.text = persons.joinToString(separator = "\n")
        }

        binding.clearButton.setOnClickListener {
            try {
                TableUtils.clearTable(databaseHelper.connectionSource, Person::class.java)
                binding.personsTextView.text = "Database is cleared."
                Toast.makeText(this, "Database is cleared.", Toast.LENGTH_SHORT).show()
            } catch (e: SQLException) {
                e.printStackTrace()
                Toast.makeText(this, "Error clearing database", Toast.LENGTH_SHORT).show()
            }
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