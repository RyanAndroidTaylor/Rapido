package com.dtp.samplemvp

import android.app.Application
import com.dtp.samplemvp.common.database.DatabaseOpenHelper
import com.dtp.simplemvp.database.DataConnection
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

        DataConnection.init(DatabaseOpenHelper(this))

        SqlScoutServer.create(this, packageName)
    }
}