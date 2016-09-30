package com.dtp.samplemvp.deal

import android.os.Parcel
import android.os.Parcelable
import com.dtp.simplemvp.mvp.state.State

/**
 * Created by ryantaylor on 9/26/16.
 */
data class DealState(var newText: String? = null) : State {

    companion object {
        @JvmStatic
        val CREATOR = object : Parcelable.Creator<DealState> {
            override fun newArray(size: Int): Array<out DealState?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): DealState {
                return DealState(source)
            }

        }
    }

    constructor(source: Parcel) : this() {
        newText = source.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(newText)
    }

    override fun describeContents(): Int {
        return 0
    }


}