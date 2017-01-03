@file:Suppress("UNUSED")
package com.izeni.rapidocommon

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.app.NotificationManager
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import java.io.Serializable

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
fun Float.pxToDp() = this / (Resources.getSystem().displayMetrics.densityDpi / 160f)
fun Float.dpToPx() = this * (Resources.getSystem().displayMetrics.densityDpi / 160f)

fun Int.pxToDp() = Math.round(this / (Resources.getSystem().displayMetrics.densityDpi / 160f))
fun Int.dpToPx() = Math.round(this * (Resources.getSystem().displayMetrics.densityDpi / 160f))

fun Context.getStringRes(@StringRes resId: Int): String = resources.getString(resId)

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View
        = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.clipboardManager: ClipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

val Context.wifiManager: WifiManager
    get() = getSystemService(Context.WIFI_SERVICE) as WifiManager

val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

val Context.telephonyManager: TelephonyManager
    get() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

val Context.wifiP2PManager: WifiP2pManager
    get() = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager

private val version: Int by lazy { Build.VERSION.SDK_INT }

@TargetApi(21)
fun Context.formatNumber(phone: String): String? {
    apiOr(19, {
        return PhoneNumberUtils.formatNumber(phone,
                                             telephonyManager.networkCountryIso.toUpperCase())
    }, {
        @Suppress("DEPRECATION")
        return PhoneNumberUtils.formatNumber(phone)
    })

    return null
}

inline fun apiOr(version: Int, action_greater: () -> Unit, action_lower: () -> Unit, inclusive: Boolean = false) {
    fromApi(version, action_greater, inclusive)
    toApi(version, action_lower, inclusive)
}

inline fun toApi(toVersion: Int, action: () -> Unit, inclusive: Boolean = false) {
    if (Build.VERSION.SDK_INT < toVersion || (inclusive && Build.VERSION.SDK_INT == toVersion)) action()
}

inline fun fromApi(fromVersion: Int, action: () -> Unit, inclusive: Boolean = true) {
    if (Build.VERSION.SDK_INT > fromVersion || (inclusive && Build.VERSION.SDK_INT == fromVersion)) action()
}

fun Activity.missingPermissions(permissions: Array<String>) = permissions.filter { !hasPermission(it) }

fun Activity.hasPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Activity.hasPermissions(permissions: Array<String>) = permissions.all { hasPermission(it) }

fun Activity.askPermissionMulti(permissions: Array<String>, request_id: Int, reasoning: (permissions: Array<String>, request_id: Int) -> Unit, has_permission: () -> Unit) {
    if (!hasPermissions(permissions)) {
        if (permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }) reasoning(permissions, request_id)
        else ActivityCompat.requestPermissions(this, missingPermissions(permissions).toTypedArray(), request_id)
    } else has_permission()
}

fun Activity.askPermission(permission: String, request_id: Int, reasoning: (permission: String, request_id: Int) -> Unit, has_permission: () -> Unit) {
    if (!hasPermission(permission)) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) reasoning(permission, request_id)
        else ActivityCompat.requestPermissions(this, arrayOf(permission), request_id)
    } else has_permission()
}

fun Context.isNetworkAvalible(): Boolean {
    connectivityManager.activeNetworkInfo.let { return it != null && it.isConnected }
}

fun <T : Fragment> T.withArguments(vararg params: Pair<String, Any>): T {
    arguments = bundleOf(*params)
    return this
}

fun <T : android.support.v4.app.Fragment> T.withArguments(vararg params: Pair<String, Any>): T {
    arguments = bundleOf(*params)
    return this
}

fun SpannableStringBuilder.appendWithSpan(str: String, ss: Any) {
    val start = this.length
    this.append(str)
    this.setSpan(ss, start, this.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun <T> createIntent(ctx: Context, clazz: Class<out T>, params: Array<out Pair<String, Any>>): Intent {
    val intent = Intent(ctx, clazz)
    if (params.isNotEmpty()) fillIntentArguments(intent, params)
    return intent
}

fun internalStartActivity(
        ctx: Context,
        activity: Class<out Activity>,
        params: Array<out Pair<String, Any>>
) {
    ctx.startActivity(createIntent(ctx, activity, params))
}

fun internalStartActivityForResult(
        act: Activity,
        activity: Class<out Activity>,
        requestCode: Int,
        params: Array<out Pair<String, Any>>
) {
    act.startActivityForResult(createIntent(act, activity, params), requestCode)
}

fun internalStartService(
        ctx: Context,
        activity: Class<out Service>,
        params: Array<out Pair<String, Any>>
) {
    ctx.startService(createIntent(ctx, activity, params))
}


inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any>) {
    internalStartActivity(this, T::class.java, params)
}

inline fun <reified T : Activity> Activity.startActivity(vararg params: Pair<String, Any>) {
    internalStartActivity(this, T::class.java, params)
}

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any>) {
    internalStartActivity(activity, T::class.java, params)
}

inline fun <reified T : Activity> android.support.v4.app.Fragment.startActivity(vararg params: Pair<String, Any>) {
    internalStartActivity(activity, T::class.java, params)
}

fun bundleOf(vararg params: Pair<String, Any>): Bundle {
    val b = Bundle()
    for (p in params) {
        val (k, v) = p
        when (v) {
            is Boolean -> b.putBoolean(k, v)
            is Byte -> b.putByte(k, v)
            is Char -> b.putChar(k, v)
            is Short -> b.putShort(k, v)
            is Int -> b.putInt(k, v)
            is Long -> b.putLong(k, v)
            is Float -> b.putFloat(k, v)
            is Double -> b.putDouble(k, v)
            is String -> b.putString(k, v)
            is CharSequence -> b.putCharSequence(k, v)
            is Parcelable -> b.putParcelable(k, v)
            is Serializable -> b.putSerializable(k, v)
            is BooleanArray -> b.putBooleanArray(k, v)
            is ByteArray -> b.putByteArray(k, v)
            is CharArray -> b.putCharArray(k, v)
            is DoubleArray -> b.putDoubleArray(k, v)
            is FloatArray -> b.putFloatArray(k, v)
            is IntArray -> b.putIntArray(k, v)
            is LongArray -> b.putLongArray(k, v)
            is Array<*> -> {
                @Suppress("UNCHECKED_CAST")
                when {
                    v.isArrayOf<Parcelable>() -> b.putParcelableArray(k, v as Array<out Parcelable>)
                    v.isArrayOf<CharSequence>() -> b.putCharSequenceArray(k, v as Array<out CharSequence>)
                    v.isArrayOf<String>() -> b.putStringArray(k, v as Array<out String>)
                    else -> throw RuntimeException("Unsupported bundle component (${v.javaClass})")
                }
            }
            is ShortArray -> b.putShortArray(k, v)
            is Bundle -> b.putBundle(k, v)
            else -> throw RuntimeException("Unsupported bundle component (${v.javaClass})")
        }
    }

    return b
}

fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any>>) {
    params.forEach {
        val value = it.second
        when (value) {
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
    }
}