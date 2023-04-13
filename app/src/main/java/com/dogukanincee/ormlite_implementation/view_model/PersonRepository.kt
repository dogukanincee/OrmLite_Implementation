package com.dogukanincee.ormlite_implementation.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.dogukanincee.ormlite_implementation.model.Person
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.dao.RuntimeExceptionDao
import android.util.Log

/**
 * Repository class for managing [Person] objects in the database using ORMLite.
 * @property databaseHelper An instance of [OrmLiteSqliteOpenHelper] used for managing the database connection.
 */
class PersonRepository(private val databaseHelper: OrmLiteSqliteOpenHelper) {

    private val personDao: Dao<Person, Int> =
        DaoManager.createDao(databaseHelper.connectionSource, Person::class.java)
    private val personRuntimeExceptionDao: RuntimeExceptionDao<Person, Int> =
        RuntimeExceptionDao(personDao)
    private val personsLiveData = MutableLiveData<List<Person>>()

    init {
        updatePersonsLiveData()
    }

    /**
     * Returns a [MutableLiveData] instance that contains a list of [Person] objects.
     * Any changes made to the database will trigger the [MutableLiveData] to update its value.
     */
    fun getPersons(): LiveData<List<Person>> = personsLiveData

    /**
     * Adds a new [Person] object to the database.
     * @param person The [Person] object to add to the database.
     */
    fun addPerson(person: Person): Boolean {
        val createResult = personRuntimeExceptionDao.create(person)
        Log.i("PersonRepository", "Adding person to the database result: $createResult")
        updatePersonsLiveData()
        return createResult == 1
    }

    /**
     * Deletes an existing [Person] object from the database.
     * @param person The [Person] object to delete from the database.
     */
    fun deletePerson(person: Person) {
        Log.i("PersonRepository", "Deleting person from the database: $person")
        personRuntimeExceptionDao.delete(person)
        updatePersonsLiveData()
    }

    /**
     * Clears all data from the database.
     * This method should be used with caution.
     */
    fun clearDatabase() {
        Log.i("PersonRepository", "Clearing database")
        databaseHelper.writableDatabase.execSQL("DELETE FROM persons;")
        databaseHelper.writableDatabase.execSQL("DELETE FROM sqlite_sequence WHERE name='persons';")
        databaseHelper.writableDatabase.execSQL("VACUUM;")
        updatePersonsLiveData()
    }

    /**
     * Updates the [personsLiveData] with the current list of [Person] objects from the database.
     * This method is called automatically whenever a change is made to the database.
     */
    private fun updatePersonsLiveData() {
        Log.i("PersonRepository", "Updating the LiveData of persons in the database")
        val queryBuilder = personDao.queryBuilder().orderBy("name", true)
        val query = queryBuilder.prepare()
        val result = personDao.query(query)
        personsLiveData.value = result
    }
}