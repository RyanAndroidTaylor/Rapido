package com.dtp.sample

import android.app.Application
import com.dtp.sample.common.database.DatabaseOpenHelper
import com.izeni.rapidosqlite.DataConnection
import com.idescout.sql.SqlScoutServer

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

        com.izeni.rapidosqlite.DataConnection.init(DatabaseOpenHelper(this))

        SqlScoutServer.create(this, packageName)
    }
}