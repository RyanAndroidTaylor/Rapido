package com.dtp.sample

import android.app.Application
import com.dtp.sample.common.database.DatabaseOpenHelper
import com.facebook.stetho.Stetho
import com.izeni.rapidocommon.transaction.OutOfRoomError
import com.izeni.rapidocommon.transaction.TransactionErrorParser
import com.izeni.rapidosqlite.DataConnection

/**
 * Created by ryantaylor on 9/22/16.
 */
class App : Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        DataConnection.init(DatabaseOpenHelper(this))

        Stetho.initializeWithDefaults(this)
    }
}