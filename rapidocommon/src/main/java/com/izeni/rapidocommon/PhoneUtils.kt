package com.izeni.rapidocommon

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.izeni.rapidocommon.SharedPref

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Izeni, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
object PhoneUtils {

    var installId: String by SharedPref("", namespace = "PhoneUtils")
    /**
     * Retrieves a unique ID specific to this phone/tablet. In almost all cases this will
     * return the same ID across installs, but it is not guaranteed.
     * @return This device's unique ID
     */
    fun getPhoneId(context: Context): String {

        var id: String = installId
        if (id.length > 1) return id

        // Try for ANDROID_ID first, as this is recommended on modern Android versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (id != null && id.length > 1) {
                installId = id
                return installId
            }
        }

        // Try for WiFi address. Not all phones report this.
        val wManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wInfo = wManager.connectionInfo
        id = wInfo.macAddress
        if (id != null && id.length > 1) {
            installId = id
            return installId
        }

        // Try for Bluetooth address. This may not work if the adapter is currently disabled.
        id = BluetoothAdapter.getDefaultAdapter().address
        if (id != null && id.length > 1) {
            installId = id
            return installId
        }

        // Try for IMEI/MEID/ESN. Requires READ_PHONE_STATE permission so check for that first
        if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            id = tManager.deviceId
            if (id != null && id.length > 1) {
                installId = id
                return installId
            }
        }

        // Try for serial on 2.3 and greater. Devices w/o telephony are required to report a unique ID, some phones may also.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            id = Build.SERIAL
            if (id != null && id.length > 1) {
                installId = id
                return installId
            }
        }

        // If all else fails, create / retrieve installation UUID
        if (id == null || id.length < 1) {
            installId = id
            return installId
        }

        return id
    }

    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    fun getModel(): String {
        return Build.MODEL
    }

    fun getName(): String {
        return Build.PRODUCT
    }

    fun getOsVersion(): String {
        return Build.VERSION.RELEASE
    }

    fun getCarrier(context: Context): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    fun getWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getAppVersionCode(context: Context): Int {
        var versionCode = 1
        try {
            versionCode = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionCode
    }

    fun getAppVersionName(context: Context): String {
        var versionName = "1"
        try {
            versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionName
    }
}