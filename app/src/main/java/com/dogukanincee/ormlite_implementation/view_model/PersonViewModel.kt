package com.dogukanincee.ormlite_implementation.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dogukanincee.ormlite_implementation.DatabaseHelper
import com.dogukanincee.ormlite_implementation.model.Person

/**
 * The ViewModel class that handles the communication between the UI and the repository.
 *
 * @param application The application context.
 */
class PersonViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "PersonViewModel"
    }

    lateinit var lifecycleOwner: LifecycleOwner
    private val repository: PersonRepository
    val personList: MutableLiveData<List<Person>> = MutableLiveData(emptyList())

    init {
        Log.i(TAG, "Initializing PersonViewModel")
        val databaseHelper = DatabaseHelper(application)
        repository = PersonRepository(databaseHelper)
    }

    /**
     * Refreshes the list of persons and updates the `personList` LiveData object.
     *
     * @param lifecycleOwner The lifecycle owner that will observe the `personList` LiveData object.
     */
    private fun refreshPersonList(lifecycleOwner: LifecycleOwner) {
        Log.i(TAG, "Refreshing person list")
        this.lifecycleOwner = lifecycleOwner
        repository.getPersons().observe(lifecycleOwner) { persons ->
            personList.value = persons ?: emptyList()
        }
    }

    /**
     * Inserts a new person to the database and updates the `personList` LiveData object.
     *
     * @param person The new person to be inserted.
     */
    fun insertPerson(person: Person) {
        Log.i(TAG, "Inserting person: $person")
        repository.addPerson(person)
        val currentList = personList.value ?: emptyList()
        val updatedList = currentList.toMutableList().apply { add(person) }
        personList.postValue(updatedList)
    }

    /**
     * Deletes a person from the database and updates the `personList` LiveData object.
     *
     * @param person The person to be deleted.
     */
    fun deletePerson(person: Person) {
        Log.i(TAG, "Deleting person: $person")
        repository.deletePerson(person)
        refreshPersonList(lifecycleOwner)
    }

    /**
     * Clears the entire database and updates the `personList` LiveData object.
     */
    fun clearDatabase() {
        Log.i(TAG, "Clearing database")
        repository.clearDatabase()
        refreshPersonList(lifecycleOwner)
    }

    /**
     * @return The `personList` LiveData object.
     */
    fun getPersonList(): LiveData<List<Person>> {
        return personList
    }
}