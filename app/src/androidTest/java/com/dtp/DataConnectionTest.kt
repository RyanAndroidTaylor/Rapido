package com.dtp

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.dtp.DataHelper.Companion.personOneAge
import com.dtp.DataHelper.Companion.personOneId
import com.dtp.DataHelper.Companion.personOneName
import com.dtp.DataHelper.Companion.petOneId
import com.dtp.DataHelper.Companion.petOneName
import com.dtp.DataHelper.Companion.toyOneId
import com.dtp.DataHelper.Companion.toyOneName
import com.dtp.sample.common.database.Person
import com.dtp.sample.common.database.Toy
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.query.QueryBuilder
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by ner on 2/8/17.
 */

@RunWith(AndroidJUnit4::class)
class DataConnectionTest {

    lateinit var context: Context

    lateinit var dataHelper: DataHelper

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getTargetContext()

        dataHelper = DataHelper(context)
        dataHelper.clearDatabase()
    }

    @After
    fun tearDown() {
        dataHelper.clearDatabase()
    }

    @Test
    fun save() {
        dataHelper.saveFullPerson()

        DataConnection.doAndClose {
            val person = it.findFirst(Person.BUILDER, QueryBuilder().with(Person.TABLE_NAME).build())

            assertNotNull(person)

            assertEquals(personOneId, person?.id)
            assertEquals(personOneName, person?.name)
            assertEquals(personOneAge, person?.age)

            assertEquals(1, person?.pets?.size)

            person?.pets?.get(0)?.let { pet ->
                assertEquals(personOneId, pet.foreignKey)
                assertEquals(petOneId, pet.id)
                assertEquals(petOneName, pet.name)
                assertEquals(1, pet.toys.size)

                pet.toys[0].let { toy ->
                    assertEquals(toyOneId, toy.id)
                    assertEquals(toyOneName, toy.name)
                }
            }
        }
    }
}