package com.dogukanincee.ormlite_implementation.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dogukanincee.ormlite_implementation.model.DatabaseHelper
import com.dogukanincee.ormlite_implementation.model.Person
import kotlinx.coroutines.launch

/**
 * The ViewModel class that handles the communication between the UI and the repository.
 *
 * @param application The application context.
 */
class PersonViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "PersonViewModel"
    }

    private val repository: PersonRepository by lazy {
        PersonRepository(DatabaseHelper(application))
    }
    val personList: MutableLiveData<List<Person>> = MutableLiveData(emptyList())


    /**
     * Refreshes the list of persons and updates the `personList` LiveData object.
     */
    private fun refreshPersonList() {
        viewModelScope.launch {
            val persons = repository.getPersons().value ?: emptyList()
            Log.i(TAG, "Refreshing person list: $persons")
            personList.postValue(persons)
        }
    }

    /**
     * Inserts a new person to the database and updates the `personList` LiveData object.
     *
     * @param person The new person to be inserted.
     */
    fun insertPerson(person: Person) {
        viewModelScope.launch {
            Log.i(TAG, "Inserting person: $person")
            val createResult = repository.addPerson(person)
            if (createResult) {
                refreshPersonList()
                logPersonList()
            } else {
                insertPerson(person)
            }
        }
    }

    /**
     * Deletes a person from the database and updates the `personList` LiveData object.
     *
     * @param person The person to be deleted.
     */
    fun deletePerson(person: Person) {
        viewModelScope.launch {
            Log.i(TAG, "Deleting person: $person")
            repository.deletePerson(person)
            refreshPersonList()
        }
    }

    /**
     * Clears the entire database and updates the `personList` LiveData object.
     */
    fun clearDatabase() {
        viewModelScope.launch {
            Log.i(TAG, "Clearing database")
            repository.clearDatabase()
            refreshPersonList()
        }
    }

    private fun logPersonList() {
        viewModelScope.launch {
            val persons = repository.getPersons().value ?: emptyList()
            Log.i(TAG, "Person List: $persons")
        }
    }
}