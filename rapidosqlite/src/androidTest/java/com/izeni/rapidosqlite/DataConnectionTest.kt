package com.izeni.rapidosqlite

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.izeni.rapidosqlite.query.Query
import com.izeni.rapidosqlite.util.*
import com.izeni.rapidosqlite.util.DataHelper.personOneAge
import com.izeni.rapidosqlite.util.DataHelper.personOneId
import com.izeni.rapidosqlite.util.DataHelper.personOneName
import com.izeni.rapidosqlite.util.DataHelper.petOneId
import com.izeni.rapidosqlite.util.DataHelper.petOneName
import com.izeni.rapidosqlite.util.DataHelper.toyOneId
import com.izeni.rapidosqlite.util.DataHelper.toyOneName
import com.izeni.rapidosqlite.util.DataHelper.toyTwoId
import com.izeni.rapidosqlite.util.DataHelper.toyTwoName
import org.junit.After
import org.junit.Assert.*
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
    fun testAsyncDoAndClose() {
        var ran = false

        DataConnection.asyncDoAndClose {
            ran = true

            val toy = Toy(toyOneId, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)

            assertEquals(toyOneId, savedToy?.id)
            assertEquals(toyOneName, savedToy?.name)
        }.blockingSubscribe()

        assertTrue(ran)
    }

    @Test
    fun testGetAsyncDoAndClose() {
        var ran = false

        DataConnection.asyncGetAndClose {
            ran = true

            val toy = Toy(toyOneId, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            it.findFirst(Toy.BUILDER, query)
        }.blockingSubscribe(
                {
                    assertNotNull(it)

                    assertEquals(toyOneId, it?.id)
                    assertEquals(toyOneName, it?.name)
                })

        assertTrue(ran)
    }

    @Test
    fun testFindFirst() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneId, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)

            assertEquals(toyOneId, savedToy?.id)
            assertEquals(toyOneName, savedToy?.name)
        }
    }

    @Test
    fun testFindAll() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneId, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            var toys = it.findAll(Toy.BUILDER, query)

            assertNotNull(toys)
            assertEquals(toys.size, 1)

            toys[0].let {
                assertEquals(toyOneId, it.id)
                assertEquals(toyOneName, it.name)
            }

            val toyTwo = Toy(toyTwoId, toyTwoName)

            it.insert(toyTwo)

            toys = it.findAll(Toy.BUILDER, query)

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

            it.insertAll(listOf(toyOne, toyTwo))

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val toys = it.findAll(Toy.BUILDER, query)

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

            it.insert(person)

            val query = Query(Person.TABLE_NAME, null, null, null, null, null)

            val loadedPerson = it.findFirst(Person.BUILDER, query)

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

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)
            assertEquals(toyOneId, savedToy?.id)
            assertEquals(toyOneName, savedToy?.name)

            val toyTwo = Toy(toyOneId, toyTwoName)

            it.updateForColumn(toyTwo, Toy.ID, toyTwo.id)

            val updatedToy = it.findFirst(Toy.BUILDER, query)

            assertEquals(toyOneId, updatedToy?.id)
            assertEquals(toyTwoName, updatedToy?.name)

            val allToys = it.findAll(Toy.BUILDER, query)

            assertEquals(1, allToys.size)
        }
    }

    @Test
    fun testUpdateWithColumn() {

        DataConnection.doAndClose {

            val toy = Toy(toyOneId, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)
            assertEquals(toyOneId, savedToy?.id)
            assertEquals(toyOneName, savedToy?.name)

            val toyTwo = Toy(toyOneId, toyTwoName)

            it.updateForColumn(toyTwo, Toy.ID, toyOneId)

            val updatedToy = it.findFirst(Toy.BUILDER, query)

            assertEquals(toyOneId, updatedToy?.id)
            assertEquals(toyTwoName, updatedToy?.name)

            val allToys = it.findAll(Toy.BUILDER, query)

            assertEquals(1, allToys.size)
        }
    }

    @Test
    fun testDelete() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneId, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val loadedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(loadedToy)

            it.delete(Toy.TABLE_NAME, Toy.ID, toyOneId)

            val loadedAfterDeleteToy = it.findFirst(Toy.BUILDER, query)

            assertEquals(null, loadedAfterDeleteToy)
        }
    }

    @Test
    fun testDeleteAll() {
        DataConnection.doAndClose {

            it.insertAll(listOf(Toy(toyOneId, toyOneName), Toy(toyTwoId, toyTwoName)))

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val loadedToys = it.findAll(Toy.BUILDER, query)

            assertEquals(2, loadedToys.size)

            it.deleteAll(Toy.TABLE_NAME)

            val loadedAfterDelete = it.findAll(Toy.BUILDER, query)

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