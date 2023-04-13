package com.dogukanincee.ormlite_implementation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dogukanincee.ormlite_implementation.model.Person
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.dao.RuntimeExceptionDao

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
     * Returns a [LiveData] instance that contains a list of [Person] objects.
     * Any changes made to the database will trigger the [LiveData] to update its value.
     */
    fun getPersons(): LiveData<List<Person>> {
        return personsLiveData
    }

    /**
     * Adds a new [Person] object to the database.
     * @param person The [Person] object to add to the database.
     */
    fun addPerson(person: Person) {
        personRuntimeExceptionDao.create(person)
        updatePersonsLiveData()
    }

    /**
     * Deletes an existing [Person] object from the database.
     * @param person The [Person] object to delete from the database.
     */
    fun deletePerson(person: Person) {
        personRuntimeExceptionDao.delete(person)
        updatePersonsLiveData()
    }

    /**
     * Clears all data from the database.
     * This method should be used with caution.
     */
    fun clearDatabase() {
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
        val queryBuilder = personDao.queryBuilder().orderBy("name", true)
        val query = queryBuilder.prepare()
        val result = personDao.query(query)
        personsLiveData.postValue(result)
    }
}