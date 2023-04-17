package com.dogukanincee.ormlite_implementation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.dogukanincee.ormlite_implementation.model.DatabaseHelper
import com.dogukanincee.ormlite_implementation.model.Person
import com.dogukanincee.ormlite_implementation.view_model.PersonRepository
import com.j256.ormlite.android.apptools.OpenHelperManager
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class PersonRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var repository: PersonRepository
    private lateinit var observer: Observer<List<Person>>

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        databaseHelper = OpenHelperManager.getHelper(
            appContext,
            DatabaseHelper::class.java
        )
        repository = PersonRepository(databaseHelper)

        observer = mockk(relaxed = true)

        repository.getPersons().observeForever(observer)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        repository.getPersons().removeObserver(observer)
        OpenHelperManager.releaseHelper()
    }

    @Test
    fun `addPerson should add a person to the database`() = testDispatcher.runBlockingTest {
        // GIVEN
        val person = Person(name = "Alice", age = 30)

        // WHEN
        val result = repository.addPerson(person)

        // THEN
        assertTrue(result)
        verify(exactly = 2) { observer.onChanged(any()) }
        val persons = repository.getPersons().value
        assertEquals(1, persons?.size)
        assertEquals(person.name, persons?.get(0)?.name)
        assertEquals(person.age, persons?.get(0)?.age)
    }

    @Test
    fun `deletePerson should delete a person from the database`() = testDispatcher.runBlockingTest {
        // GIVEN
        val person = Person(name = "Alice", age = 30)
        repository.addPerson(person)
        verify(exactly = 2) { observer.onChanged(any()) }

        // WHEN
        repository.deletePerson(person)

        // THEN
        verify(exactly = 2) { observer.onChanged(any()) }
        val persons = repository.getPersons().value
        assertTrue(persons.isNullOrEmpty())
    }

    @Test
    fun `clearDatabase should delete all persons from the database`() =
        testDispatcher.runBlockingTest {
            // GIVEN
            val person1 = Person(name = "Alice", age = 30)
            val person2 = Person(name = "Bob", age = 40)
            repository.addPerson(person1)
            repository.addPerson(person2)
            verify(exactly = 3) { observer.onChanged(any()) }

            // WHEN
            repository.clearDatabase()

            // THEN
            verify(exactly = 2) { observer.onChanged(any()) }
            val persons = repository.getPersons().value
            assertTrue(persons.isNullOrEmpty())
        }
}