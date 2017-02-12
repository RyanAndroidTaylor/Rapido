package com.dtp

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.dtp.DataHelper.personOneAge
import com.dtp.DataHelper.personOneId
import com.dtp.DataHelper.personOneName
import com.dtp.DataHelper.petOneId
import com.dtp.DataHelper.petOneName
import com.dtp.DataHelper.toyOneId
import com.dtp.DataHelper.toyOneName
import com.dtp.DataHelper.toyTwoId
import com.dtp.DataHelper.toyTwoName
import com.dtp.sample.common.database.Person
import com.dtp.sample.common.database.Pet
import com.dtp.sample.common.database.PetToToy
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

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getTargetContext()

        val databaseHelper: SQLiteOpenHelper = TestOpenHelper(context)

        DataConnection.init(databaseHelper)

        clearDatabase()
    }

    @After
    fun tearDown() {
        clearDatabase()
    }

    @Test
    fun testFindFirst() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneId, toyOneName)

            it.save(toy)

            val savedToy = it.findFirst(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertNotNull(savedToy)

            assertEquals(toyOneId, savedToy?.id)
            assertEquals(toyOneName, savedToy?.name)
        }
    }

    @Test
    fun testFindAll() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneId, toyOneName)

            it.save(toy)

            var toys = it.findAll(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertNotNull(toys)
            assertEquals(toys.size, 1)

            toys[0].let {
                assertEquals(toyOneId, it.id)
                assertEquals(toyOneName, it.name)
            }

            val toyTwo = Toy(toyTwoId, toyTwoName)

            it.save(toyTwo)

            toys = it.findAll(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertNotNull(toys)
            assertEquals(toys.size, 2)

            toys[0].let {
                assertEquals(toyOneId, it.id)
                assertEquals(toyOneName, it.name)
            }

            toys[1].let {
                assertEquals(toyTwoId, it.id)
                assertEquals(toyTwoName, it.name)
            }
        }
    }

    @Test
    fun testSaveAll() {
        DataConnection.doAndClose {
            val toyOne = Toy(toyOneId, toyOneName)
            val toyTwo = Toy(toyTwoId, toyTwoName)

            it.saveAll(listOf(toyOne, toyTwo))

            val toys = it.findAll(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(2, toys.size)

            toys[0].let {
                assertEquals(toyOneId, it.id)
                assertEquals(toyOneName, it.name)
            }

            toys[1].let {
                assertEquals(toyTwoId, it.id)
                assertEquals(toyTwoName, it.name)
            }
        }
    }

    @Test
    fun testParentChildAndJunctionSave() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneId, toyOneName)
            val pet = Pet(petOneId, personOneId, petOneName, listOf(toy))
            val person = Person(personOneId, personOneName, personOneAge, listOf(pet))

            it.save(person)

            val loadedPerson = it.findFirst(Person.BUILDER, QueryBuilder().with(Person.TABLE_NAME).build())

            assertNotNull(loadedPerson)

            assertEquals(personOneId, loadedPerson?.id)
            assertEquals(personOneName, loadedPerson?.name)
            assertEquals(personOneAge, loadedPerson?.age)

            assertEquals(1, loadedPerson?.pets?.size)

            loadedPerson?.pets?.get(0)?.let { pet ->
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

    @Test
    fun testUpdateWithId() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneId, toyOneName)

            it.save(toy)

            val savedToy = it.findFirst(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertNotNull(savedToy)
            assertEquals(toyOneId, savedToy?.id)
            assertEquals(toyOneName, savedToy?.name)

            val toyTwo = Toy(toyOneId, toyTwoName)

            it.updateWithId(toyTwo)

            val updatedToy = it.findFirst(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(toyOneId, updatedToy?.id)
            assertEquals(toyTwoName, updatedToy?.name)

            val allToys = it.findAll(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(1, allToys.size)
        }
    }

    @Test
    fun testUpdateWithColumn() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneId, toyOneName)

            it.save(toy)

            val savedToy = it.findFirst(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertNotNull(savedToy)
            assertEquals(toyOneId, savedToy?.id)
            assertEquals(toyOneName, savedToy?.name)

            val toyTwo = Toy(toyOneId, toyTwoName)

            it.updateWithColumn(toyTwo, Toy.ID, toyOneId)

            val updatedToy = it.findFirst(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(toyOneId, updatedToy?.id)
            assertEquals(toyTwoName, updatedToy?.name)

            val allToys = it.findAll(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(1, allToys.size)
        }
    }

    @Test
    fun testDelete() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneId, toyOneName)

            it.save(toy)

            val loadedToy = it.findFirst(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertNotNull(loadedToy)

            it.delete(Toy.TABLE_NAME, Toy.ID, toyOneId)

            val loadedAfterDeleteToy = it.findFirst(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(null, loadedAfterDeleteToy)
        }
    }

    @Test
    fun testDeleteAll() {
        DataConnection.doAndClose {
            it.saveAll(listOf(Toy(toyOneId, toyOneName), Toy(toyTwoId, toyTwoName)))

            val loadedToys = it.findAll(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(2, loadedToys.size)

            it.deleteAll(Toy.TABLE_NAME)

            val loadedAfterDelete = it.findAll(Toy.BUILDER, QueryBuilder().all(Toy.TABLE_NAME))

            assertEquals(0, loadedAfterDelete.size)
        }
    }

    fun clearDatabase() {
        DataConnection.doAndClose {
            it.deleteAll(PetToToy.TABLE_NAME)
            it.deleteAll(Toy.TABLE_NAME)
            it.deleteAll(Pet.TABLE_NAME)
            it.deleteAll(Person.TABLE_NAME)
        }
    }
}