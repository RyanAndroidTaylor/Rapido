package com.izeni.rapidosqlite.query

import android.support.test.runner.AndroidJUnit4
import com.izeni.rapidosqlite.table.Column.Companion.ANDROID_ID
import com.izeni.rapidosqlite.util.Toy
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ner on 2/10/17.
 */
@RunWith(AndroidJUnit4::class)
class QueryBuilderTest {

    val someUUid = "SomeUuid"

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun testWith() {
        val query = QueryBuilder.with(Toy.TABLE_NAME).build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)
        assertNull(query.selection)
        assertNull(query.selectionArgs)
        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testAll() {
        val query = QueryBuilder.all(Toy.TABLE_NAME)

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)
        assertNull(query.selection)
        assertNull(query.selectionArgs)
        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun joinWithColumns() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .select(Toy.COLUMNS)
                .join("This is a join string")
                .build()

        assertTrue(query is RawQuery)

        if (query is RawQuery) {
            assertEquals(query.query, "SELECT ${Toy.TABLE_NAME}.${Toy.UUID.name}, ${Toy.TABLE_NAME}.${Toy.NAME.name}, ${Toy.TABLE_NAME}.${ANDROID_ID.name} FROM This is a join string ")
            assertNull(query.columns)
            assertNull(query.selection)
            assertNull(query.selectionArgs)
            assertNull(query.limit)
            assertNull(query.order)
        }
    }

    @Test
    fun testJoinWithoutColumns() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .join("This is a join string")
                .build()

        assertTrue(query is RawQuery)

        if (query is RawQuery) {
            assertEquals(query.query, "SELECT * FROM This is a join string ")
            assertNull(query.columns)
            assertNull(query.selection)
            assertNull(query.selectionArgs)
            assertNull(query.limit)
            assertNull(query.order)
        }
    }

    @Test
    fun testJoinWithSelectionArgs() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereEquals(Toy.UUID, someUUid)
                .join("This is a join string")
                .build()

        assertTrue(query is RawQuery)

        if (query is RawQuery) {
            assertEquals(query.query, "SELECT * FROM This is a join string WHERE Uuid=?")
            assertEquals(1, query.selectionArgs?.size)
            assertEquals(someUUid, query.selectionArgs!![0])
            assertNull(query.columns)
            assertNull(query.selection)
            assertNull(query.limit)
            assertNull(query.order)
        }
    }

    @Test
    fun testSelect() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .select(Toy.COLUMNS)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)

        assertEquals(3, query.columns?.size)
        assertEquals(Toy.UUID.name, query.columns?.get(0))
        assertEquals(Toy.NAME.name, query.columns?.get(1))

        assertNull(query.selection)
        assertNull(query.selectionArgs)
        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereEquals() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereEquals(Toy.UUID, someUUid)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.UUID.name} =? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals(someUUid, query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereEqualsWithTableName() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereEquals(Toy.TABLE_NAME, Toy.UUID, someUUid)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.TABLE_NAME}.${Toy.UUID.name} =? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals(someUUid, query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereLessThan() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereLessThan(Toy.UUID, someUUid)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.UUID.name} <? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals(someUUid, query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun whereLessThanOrEqualTo() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereLessThanOrEqual(Toy.UUID, someUUid)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.UUID.name} <=? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals(someUUid, query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereGreaterThan() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereGreaterThan(Toy.UUID, someUUid)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.UUID.name} >? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals(someUUid, query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereGreaterThanOrEqualTo() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereGreaterThanOrEqual(Toy.UUID, someUUid)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.UUID.name} >=? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals(someUUid, query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testOr() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereEquals(Toy.UUID, someUUid)
                .or()
                .whereGreaterThan(Toy.NAME, "a")
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.UUID.name} =?  OR ${Toy.NAME.name} >? ")
        assertEquals(2, query.selectionArgs?.size)
        assertEquals(someUUid, query.selectionArgs?.get(0))
        assertEquals("a", query.selectionArgs?.get(1))

        assertNull(query.limit)
        assertNull(query.order)
    }
}