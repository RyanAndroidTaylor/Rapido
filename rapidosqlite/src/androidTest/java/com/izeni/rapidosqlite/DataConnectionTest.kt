package com.izeni.rapidosqlite

import android.content.Context
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.izeni.rapidosqlite.query.Query
import com.izeni.rapidosqlite.query.QueryBuilder
import com.izeni.rapidosqlite.util.*
import com.izeni.rapidosqlite.util.DataHelper.personOneAge
import com.izeni.rapidosqlite.util.DataHelper.personOneUuid
import com.izeni.rapidosqlite.util.DataHelper.personOneName
import com.izeni.rapidosqlite.util.DataHelper.petOneUuid
import com.izeni.rapidosqlite.util.DataHelper.petOneName
import com.izeni.rapidosqlite.util.DataHelper.toyOneUuid
import com.izeni.rapidosqlite.util.DataHelper.toyOneName
import com.izeni.rapidosqlite.util.DataHelper.toyTwoUuid
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

            val toy = Toy(toyOneUuid, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)

            assertEquals(toyOneUuid, savedToy?.uuid)
            assertEquals(toyOneName, savedToy?.name)
        }

        Thread.sleep(1000)
        assertTrue(ran)
    }

    @Test
    fun testGetAsyncDoAndClose() {
        var ran = false

        val toy = DataConnection.asyncGetAndClose {
            ran = true

            val toy = Toy(toyOneUuid, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            it.findFirst(Toy.BUILDER, query)
        }.blockingGet()

        assertNotNull(toy)

        assertEquals(toyOneUuid, toy?.uuid)
        assertEquals(toyOneName, toy?.name)

        assertTrue(ran)
    }

    @Test
    fun testFindFirst() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneUuid, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)

            assertEquals(toyOneUuid, savedToy?.uuid)
            assertEquals(toyOneName, savedToy?.name)
        }
    }

    @Test
    fun testFindAll() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneUuid, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            var toys = it.findAll(Toy.BUILDER, query)

            assertNotNull(toys)
            assertEquals(toys.size, 1)

            toys[0].let {
                assertEquals(toyOneUuid, it.uuid)
                assertEquals(toyOneName, it.name)
            }

            val toyTwo = Toy(toyTwoUuid, toyTwoName)

            it.insert(toyTwo)

            toys = it.findAll(Toy.BUILDER, query)

            assertNotNull(toys)
            assertEquals(toys.size, 2)

            toys[0].let {
                assertEquals(toyOneUuid, it.uuid)
                assertEquals(toyOneName, it.name)
            }

            toys[1].let {
                assertEquals(toyTwoUuid, it.uuid)
                assertEquals(toyTwoName, it.name)
            }
        }
    }

    @Test
    fun testInsertAll() {
        DataConnection.doAndClose {

            val toyOne = Toy(toyOneUuid, toyOneName)
            val toyTwo = Toy(toyTwoUuid, toyTwoName)

            it.insertAll(listOf(toyOne, toyTwo))

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val toys = it.findAll(Toy.BUILDER, query)

            assertEquals(2, toys.size)

            toys[0].let {
                assertEquals(toyOneUuid, it.uuid)
                assertEquals(toyOneName, it.name)
            }

            toys[1].let {
                assertEquals(toyTwoUuid, it.uuid)
                assertEquals(toyTwoName, it.name)
            }
        }
    }

    @Test
    fun testParentChildAndJunctionSave() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneUuid, toyOneName)
            val pet = Pet(petOneUuid, personOneUuid, petOneName, listOf(toy))
            val person = Person(personOneUuid, personOneName, personOneAge, listOf(pet))

            it.insert(person)

            val loadedPerson = it.findFirst(Person.BUILDER, QueryBuilder.all(Person.TABLE_NAME))

            assertNotNull(loadedPerson)

            assertEquals(personOneUuid, loadedPerson?.uuid)
            assertEquals(personOneName, loadedPerson?.name)
            assertEquals(personOneAge, loadedPerson?.age)

            assertEquals(1, loadedPerson?.pets?.size)

            loadedPerson?.pets?.get(0)?.let { pet ->
                assertEquals(personOneUuid, pet.personUuid)
                assertEquals(petOneUuid, pet.uuid)
                assertEquals(petOneName, pet.name)
                assertEquals(1, pet.toys.size)

                pet.toys[0].let { toy ->
                    assertEquals(toyOneUuid, toy.uuid)
                    assertEquals(toyOneName, toy.name)
                }
            }
        }
    }

    @Test
    fun testUpsertWhenNoItemExistsWithId() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneUuid, toyOneName)

            it.upsert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)
            assertEquals(toyOneUuid, savedToy?.uuid)
            assertEquals(toyOneName, savedToy?.name)
        }
    }

    @Test
    fun testUpsertWhenItemWithIdAlreadyExists() {
        DataConnection.doAndClose {
            val toy = Toy(toyOneUuid, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)
            assertEquals(toyOneUuid, savedToy?.uuid)
            assertEquals(toyOneName, savedToy?.name)

            val toyTwo = Toy(toyOneUuid, toyTwoName)

            it.upsert(toyTwo)

            val updatedToy = it.findFirst(Toy.BUILDER, query)

            assertEquals(toyOneUuid, updatedToy?.uuid)
            assertEquals(toyTwoName, updatedToy?.name)

            val allToys = it.findAll(Toy.BUILDER, query)

            assertEquals(1, allToys.size)
        }
    }

    @Test
    fun testUpdate() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneUuid, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val savedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(savedToy)
            assertEquals(toyOneUuid, savedToy?.uuid)
            assertEquals(toyOneName, savedToy?.name)

            val toyTwo = Toy(toyOneUuid, toyTwoName)

            it.update(toyTwo)

            val updatedToy = it.findFirst(Toy.BUILDER, query)

            assertEquals(toyOneUuid, updatedToy?.uuid)
            assertEquals(toyTwoName, updatedToy?.name)

            val allToys = it.findAll(Toy.BUILDER, query)

            assertEquals(1, allToys.size)
        }
    }

    @Test
    fun testDelete() {
        DataConnection.doAndClose {

            val toy = Toy(toyOneUuid, toyOneName)

            it.insert(toy)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val loadedToy = it.findFirst(Toy.BUILDER, query)

            assertNotNull(loadedToy)

            it.delete(loadedToy!!)

            val loadedAfterDeleteToy = it.findFirst(Toy.BUILDER, query)

            assertEquals(null, loadedAfterDeleteToy)
        }
    }

    @Test
    fun testDeleteAll() {
        DataConnection.doAndClose {

            val toys = listOf(Toy(toyOneUuid, toyOneName), Toy(toyTwoUuid, toyTwoName))

            it.insertAll(toys)

            val query = Query(Toy.TABLE_NAME, null, null, null, null, null)

            val loadedToys = it.findAll(Toy.BUILDER, query)

            assertEquals(2, loadedToys.size)

            it.deleteAll(loadedToys)

            val loadedAfterDelete = it.findAll(Toy.BUILDER, query)

            assertEquals(0, loadedAfterDelete.size)
        }
    }

    fun clearDatabase() {
        DataConnection.doAndClose {
            it.clear(PetToToy.TABLE_NAME)
            it.clear(Toy.TABLE_NAME)
            it.clear(Pet.TABLE_NAME)
            it.clear(Person.TABLE_NAME)
        }
    }
}