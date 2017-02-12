package com.dtp.sample.rx

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dtp.sample.common.database.Person
import com.dtp.sample.common.database.Pet
import com.izeni.rapidocommon.d
import com.izeni.rapidocommon.filterNetworkErrors
import com.izeni.rapidocommon.errors.TransactionErrorHandler
import com.izeni.rapidocommon.errors.Transaction
import com.izeni.rapidocommon.errors.Unauthorized
import com.izeni.rapidocommon.runOnIo
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.query.QueryBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by ner on 2/10/17.
 */
class RxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savePeople()

        loadPeople()
                .observeOn(AndroidSchedulers.mainThread())
                .filterNetworkErrors(TransactionErrorHandler(this, { d("If there is something else you would like to do when this fails do it here.") }))
                .subscribe {
                    if (it is Transaction.Success)
                        it.value.forEach { d("Person $it") }
                }
    }

    fun savePeople() {
        val person = Person(244L, "RxPerson", 23, arrayListOf<Pet>())

        DataConnection.doAndClose { it.save(person) }
    }

    fun loadPeople(): Observable<Transaction<List<Person>, Unit>> {
        val subject: BehaviorSubject<Transaction<List<Person>, Unit>> = BehaviorSubject.create()

        runOnIo {
            val query = QueryBuilder.all(Person.TABLE_NAME)

            val people = DataConnection.getAndClose { it.findAll(Person.BUILDER, query) }

            subject.onNext(Transaction.Success(people))

            subject.onNext(Transaction.Failure(Unauthorized()))
        }

        return subject
    }
}