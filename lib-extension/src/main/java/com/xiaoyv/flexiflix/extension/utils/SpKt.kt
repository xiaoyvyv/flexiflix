@file:Suppress("UNCHECKED_CAST")

package com.xiaoyv.flexiflix.extension.utils

/**
 * 本地 SP 储存
 *
 * @author why
 * @since 5/15/24
 */

import android.content.Context
import android.content.SharedPreferences
import com.xiaoyv.flexiflix.extension.ExtensionProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun sharePreference(name: String, default: String = ""): SharedPreferencesDelegate<String> {
    return SharedPreferencesDelegate(name, default)
}

fun sharePreference(name: String, default: Int = 0): SharedPreferencesDelegate<Int> {
    return SharedPreferencesDelegate(name, default)
}

fun sharePreference(name: String, default: Long = 0): SharedPreferencesDelegate<Long> {
    return SharedPreferencesDelegate(name, default)
}

fun sharePreference(name: String, default: Boolean = false): SharedPreferencesDelegate<Boolean> {
    return SharedPreferencesDelegate(name, default)
}

fun sharePreference(name: String, default: Float = 0f): SharedPreferencesDelegate<Float> {
    return SharedPreferencesDelegate(name, default)
}

class SharedPreferencesDelegate<T>(
    private val name: String,
    private val default: T
) : ReadWriteProperty<Any, T> {

    private val prefs: SharedPreferences by lazy {
        ExtensionProvider.application.getSharedPreferences("default_prefs", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return when (default) {
            is Int -> prefs.getInt(name, default) as T
            is Long -> prefs.getLong(name, default) as T
            is Float -> prefs.getFloat(name, default) as T
            is Boolean -> prefs.getBoolean(name, default) as T
            is String -> prefs.getString(name, default) as T ?: default
            else -> throw IllegalArgumentException("Unsupported type.")
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        with(prefs.edit()) {
            when (value) {
                is Int -> putInt(name, value)
                is Long -> putLong(name, value)
                is Float -> putFloat(name, value)
                is Boolean -> putBoolean(name, value)
                is String -> putString(name, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }.apply()
        }
    }
}
