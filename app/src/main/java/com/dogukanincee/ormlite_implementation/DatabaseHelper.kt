package com.dogukanincee.ormlite_implementation

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

/**
 * A helper class to manage database creation and version management.
 *
 * @property context The context of the application.
 */
class DatabaseHelper(context: Context) :
    OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
    }

    /**
     * Called when the database is created for the first time. This method creates the table for the Person class.
     *
     * @param database The database.
     * @param connectionSource The connection source to use for the table creation.
     */
    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTable(connectionSource, Person::class.java)
    }

    /**
     * Called when the database needs to be upgraded. This method drops the old table and creates a new one for the Person class.
     *
     * @param database The database.
     * @param connectionSource The connection source to use for the table creation.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    override fun onUpgrade(
        database: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TableUtils.dropTable<Person, Any>(connectionSource, Person::class.java, true)
        onCreate(database, connectionSource)
    }
}