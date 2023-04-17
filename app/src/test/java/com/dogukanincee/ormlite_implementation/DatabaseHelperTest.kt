package com.dogukanincee.ormlite_implementation

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.dogukanincee.ormlite_implementation.model.DatabaseHelper
import com.j256.ormlite.db.DatabaseType
import com.j256.ormlite.support.ConnectionSource
import io.mockk.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DatabaseHelperTest {

    private lateinit var context: Context
    private lateinit var helper: DatabaseHelper
    private val connectionSource = mockk<ConnectionSource>()


    @Before
    fun setup() {
        context = mockk(relaxed = true)
        helper = DatabaseHelper(context)
    }

    @Test
    fun `onCreate should create the persons table`() {
        val database = mockk<SQLiteDatabase>()
        val connectionSource = mockk<ConnectionSource>()

        every { database.execSQL(any()) } just Runs

        helper.onCreate(database, connectionSource)

        verify { database.execSQL("CREATE TABLE IF NOT EXISTS `persons` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL, `age` INTEGER NOT NULL)") }
    }

    @Test
    fun `onUpgrade should drop the persons table and create a new one`() {
        val database = mockk<SQLiteDatabase>()
        val connectionSource = mockk<ConnectionSource>()

        every { database.execSQL(any()) } just Runs

        helper.onUpgrade(database, connectionSource, 1, 2)

        verify { database.execSQL("DROP TABLE IF EXISTS `persons`") }
        verify { helper.onCreate(database, connectionSource) }
    }
}