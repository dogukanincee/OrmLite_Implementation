package com.dogukanincee.ormlite_implementation

import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dogukanincee.ormlite_implementation.model.Person
import com.dogukanincee.ormlite_implementation.view.MainActivity
import com.dogukanincee.ormlite_implementation.view_model.PersonRepository
import com.dogukanincee.ormlite_implementation.view_model.PersonViewModel
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
class MainActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    private lateinit var viewModel: PersonViewModel
    private lateinit var repository: PersonRepository
    private lateinit var databaseHelper: OrmLiteSqliteOpenHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var clearButton: Button

    @Before
    fun setUp() {
        // Create a mock DatabaseHelper
        databaseHelper = mockk()

        // Create a mock PersonRepository
        repository = mockk()
        every { repository.getPersons() } returns MutableLiveData(emptyList())
        every { repository.addPerson(any()) } returns true

        // Create a mock PersonViewModel
        viewModel = mockk(relaxed = true)
        every { viewModel.personList } returns repository.getPersons()

        // Launch the activity and get the views
        activityScenarioRule.scenario.onActivity { activity ->
            recyclerView = activity.findViewById(R.id.recyclerView)
            saveButton = activity.findViewById(R.id.saveButton)
            nameEditText = activity.findViewById(R.id.nameEditText)
            ageEditText = activity.findViewById(R.id.ageEditText)
            clearButton = activity.findViewById(R.id.clearButton)
        }
    }

    @Test
    fun testSaveButton() {
        // Set up the name and age EditTexts
        val name = "John"
        val age = 30
        onView(withId(R.id.nameEditText)).perform(typeText(name))
        onView(withId(R.id.ageEditText)).perform(typeText(age.toString()))

        // Click the Save button
        onView(withId(R.id.saveButton)).perform(click())

        // Verify that the repository method was called with the correct Person object
        val expectedPerson = Person(name = name, age = age)
        verify { repository.addPerson(expectedPerson) }

        // Verify that the ViewModel method was called to refresh the person list
        verify { viewModel.insertPerson(expectedPerson) }
    }

    @Test
    fun testClearButton() {
        // Set up the person list
        val persons = listOf(Person(name = "John", age = 30), Person(name = "Mary", age = 25))
        every { repository.getPersons() } returns MutableLiveData(persons)

        // Click the Clear button
        onView(withId(R.id.clearButton)).perform(click())

        // Verify that the repository method was called to clear the database
        verify { repository.clearDatabase() }

        // Verify that the ViewModel method was called to refresh the person list
        verify { viewModel.clearDatabase() }
    }
}
 **/