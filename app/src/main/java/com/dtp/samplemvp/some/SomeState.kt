package com.dtp.samplemvp.some

import java.util.*
import android.os.Parcel
import android.os.Parcelable

import com.dtp.rapido.mvp.state.State

/**
 * Created by ner on 11/2/16.
 */
data class SomeState(val something: String) : State {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<SomeState> = object : Parcelable.Creator<SomeState> {
            override fun createFromParcel(source: Parcel): SomeState = SomeState(source)
            override fun newArray(size: Int): Array<SomeState?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(something)
    }
}