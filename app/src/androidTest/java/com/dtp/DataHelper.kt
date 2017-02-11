package com.dtp

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.dtp.sample.common.database.Person
import com.dtp.sample.common.database.Pet
import com.dtp.sample.common.database.PetToToy
import com.dtp.sample.common.database.Toy
import com.izeni.rapidosqlite.DataConnection

/**
 * Created by ner on 2/8/17.
 */
class DataHelper(context: Context) {
    companion object {
        val toyOneId = 1235L
        val toyOneName = "Ball"

        val petOneId = 10512L
        val petOneName = "Butch"

        val personOneId = 94812L
        val personOneName = "Jake"
        val personOneAge = 21
    }

    val databaseHelper: SQLiteOpenHelper = TestOpenHelper(context)

    init {
        DataConnection.init(databaseHelper)
    }

    fun saveFullPerson() {
        val toyOne = Toy(toyOneId, toyOneName)

        val petOne = Pet(petOneId, personOneId, petOneName, listOf(toyOne))

        val personOne = Person(personOneId, personOneName, personOneAge, listOf(petOne))

        DataConnection.doAndClose {
            it.save(personOne)
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