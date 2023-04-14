package com.dogukanincee.ormlite_implementation

import com.dogukanincee.ormlite_implementation.model.Person
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PersonTest {

    @Test
    fun testPersonProperties() {
        val person = Person(name = "John", age = 30)
        assertEquals(0, person.id)
        assertEquals("John", person.name)
        assertEquals(30, person.age)
    }

    @Test
    fun testPersonEquals() {
        val person1 = Person(name = "John", age = 30)
        val person2 = Person(name = "John", age = 30)
        val person3 = Person(name = "Jane", age = 25)
        assertEquals(person1, person2)
        assertNotEquals(person1, person3)
    }

    @Test
    fun testPersonToString() {
        val person = Person(name = "John", age = 30)
        assertEquals("Person(id=0, name=John, age=30)", person.toString())
    }
}