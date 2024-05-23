@file:Suppress("UNCHECKED_CAST")

package com.xiaoyv.flexiflix.extension.utils


import android.content.Context
import android.content.SharedPreferences
import com.xiaoyv.flexiflix.extension.ExtensionProvider
import com.xiaoyv.flexiflix.extension.editMode
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val GLOBAL_SP_NAME = "default_prefs"


/**
 * 本地 SP 储存
 *
 * @author why
 * @since 5/15/24
 */
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
    private val default: T,
) : ReadWriteProperty<Any, T> {

    private val prefs: SharedPreferences by lazy {
        ExtensionProvider.application.getSharedPreferences("default_prefs", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (editMode) {
            return default
        }

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
        if (editMode) return

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

fun <T> SharedPreferences.put(key: String, value: T) {
    val preference = this
    when (value) {
        is Int -> preference.edit().apply {
            putInt(key, value)
            apply()
        }

        is String -> preference.edit().apply {
            putString(key, value)
            apply()
        }

        is Long -> preference.edit().apply {
            putLong(key, value)
            apply()
        }

        is Float -> preference.edit().apply {
            putFloat(key, value)
            apply()
        }

        is Boolean -> preference.edit().apply {
            putBoolean(key, value)
            apply()
        }

        else -> error("Unsupported class: ${value}")
    }
}

fun <T> SharedPreferences.get(key: String, cls: Class<T>?, default: Any? = null): T {
    val preference = this
    return when (cls) {
        Int::class.javaPrimitiveType -> {
            preference.getInt(key, (default as? Int) ?: 0) as T
        }

        String::class.java -> {
            preference.getString(key, (default as? String) ?: "").orEmpty() as T
        }

        Long::class.javaPrimitiveType -> {
            preference.getLong(key, (default as? Long) ?: 0) as T
        }

        Float::class.javaPrimitiveType -> {
            preference.getFloat(key, (default as? Float) ?: 0f) as T
        }

        Boolean::class.javaPrimitiveType -> {
            preference.getBoolean(key, (default as? Boolean) ?: false) as T
        }

        else -> {
            error("Unsupported class: $cls")
        }
    }
}
