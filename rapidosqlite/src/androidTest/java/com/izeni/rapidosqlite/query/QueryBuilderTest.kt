package com.izeni.rapidosqlite.query

import android.support.test.runner.AndroidJUnit4
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
            assertEquals(query.query, "SELECT Toy.Id, Toy.Name FROM This is a join string ")
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
                .whereEquals(Toy.ID, 12345)
                .join("This is a join string")
                .build()

        assertTrue(query is RawQuery)

        if (query is RawQuery) {
            assertEquals(query.query, "SELECT * FROM This is a join string WHERE Id=?")
            assertEquals(1, query.selectionArgs?.size)
            assertEquals("12345", query.selectionArgs!![0])
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

        assertEquals(2, query.columns?.size)
        assertEquals(Toy.ID.name, query.columns?.get(0))
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
                .whereEquals(Toy.ID, 12345)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "Id =? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals("12345", query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereEqualsWithTableName() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereEquals(Toy.TABLE_NAME, Toy.ID, 12345)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.TABLE_NAME}.Id =? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals("12345", query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereLessThan() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereLessThan(Toy.ID, 12345)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "Id <? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals("12345", query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun whereLessThanOrEqualTo() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereLessThanOrEqual(Toy.ID, 12345)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "Id <=? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals("12345", query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereGreaterThan() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereGreaterThan(Toy.ID, 12345)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "Id >? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals("12345", query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testWhereGreaterThanOrEqualTo() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereGreaterThanOrEqual(Toy.ID, 12345)
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "Id >=? ")
        assertEquals(1, query.selectionArgs?.size)
        assertEquals("12345", query.selectionArgs?.get(0))

        assertNull(query.limit)
        assertNull(query.order)
    }

    @Test
    fun testOr() {
        val query = QueryBuilder
                .with(Toy.TABLE_NAME)
                .whereEquals(Toy.ID, 12345)
                .or()
                .whereGreaterThan(Toy.NAME, "123")
                .build()

        assertEquals(query.tableName, Toy.TABLE_NAME)
        assertNull(query.columns)

        assertEquals(query.selection, "${Toy.ID.name} =?  OR ${Toy.NAME.name} >? ")
        assertEquals(2, query.selectionArgs?.size)
        assertEquals("12345", query.selectionArgs?.get(0))
        assertEquals("123", query.selectionArgs?.get(1))

        assertNull(query.limit)
        assertNull(query.order)
    }
}