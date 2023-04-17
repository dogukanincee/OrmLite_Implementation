package com.dogukanincee.ormlite_implementation

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dogukanincee.ormlite_implementation.model.Person
import com.dogukanincee.ormlite_implementation.view_model.PersonViewModel
import io.mockk.mockk
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PersonViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: PersonViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        val application: Application = mockk()
        viewModel = PersonViewModel(application)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `insertPerson adds person to the database`() = testDispatcher.runBlockingTest {
        val initialSize = viewModel.personList.value!!.size
        val person = Person(name = "John Doe", age = 30)

        viewModel.insertPerson(person)

        val persons = viewModel.personList.value!!
        assertEquals(initialSize + 1, persons.size)
        assertTrue(viewModel.personList.value!!.isNotEmpty())
        assertTrue(persons.contains(person))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `deletePerson removes person from the database`() = testDispatcher.runBlockingTest {
        val person = Person(name = "John Doe", age = 30)
        viewModel.insertPerson(person)

        val initialSize = viewModel.personList.value!!.size

        viewModel.deletePerson(person)

        val persons = viewModel.personList.value!!
        assertEquals(initialSize - 1, persons.size)
        assertFalse(persons.contains(person))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `clearDatabase removes all persons from the database`() = testDispatcher.runBlockingTest {
        viewModel.insertPerson(Person(name = "John Doe", age = 30))
        viewModel.insertPerson(Person(name = "Jane Doe", age = 25))
        viewModel.insertPerson(Person(name = "Bob Smith", age = 45))

        viewModel.clearDatabase()

        assertTrue(viewModel.personList.value!!.isEmpty())
    }
}