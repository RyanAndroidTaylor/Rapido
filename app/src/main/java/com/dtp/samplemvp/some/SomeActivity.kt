package com.dtp.samplemvp.some

import android.os.Bundle
import android.util.Log
import com.dtp.rapido.database.DataConnection
import com.dtp.rapido.database.query.QueryBuilder
import com.dtp.rapido.mvp.presenter.PresenterData
import com.dtp.samplemvp.common.BaseActivity
import com.dtp.samplemvp.common.database.Person
import com.dtp.samplemvp.common.database.Pet

/**
 * Created by ner on 11/2/16.
 */
class SomeActivity: BaseActivity<SomeView, SomePresenter>(), SomeView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = this

        presenter = SomePresenter()

        if (savedInstanceState != null)
            presenter.load(PresenterData(savedInstanceState))
        else
            presenter.load(null)

        val dog = Pet("dog1", 0)
        val cat = Pet("Cat1", 1)
        val dogTwo = Pet("dog2", 0)

        val person = Person("Person1", "Tim", listOf(dog, dogTwo, cat))

        DataConnection.save(person)

        val savedPerson = DataConnection.findFirst(Person.BUILDER, QueryBuilder().from(Person.TABLE_NAME).build())

        Log.i("SomeActivity", "SavedPerson $savedPerson")
    }
}