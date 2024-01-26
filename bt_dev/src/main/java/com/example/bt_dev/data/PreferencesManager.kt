package com.example.bt_dev.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor(context:Context)  {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) // только наше приложение получает доступ

    companion object {
        private var instance: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            if (instance == null) {
                instance = PreferencesManager(context)
            }
            return instance!!
        }
    }

    fun contains(key: String): Boolean {
       return sharedPreferences.contains(key)
    }

    fun <T> read(typedStoreKey: TypeStoreKey<T>) : Any? {
        @Suppress("UNCHECKED_CAST")
        return when (typedStoreKey.key as T) {
            is Int ->  sharedPreferences.getInt(typedStoreKey.key, typedStoreKey.defaultValue as Int)
            is Long -> sharedPreferences.getLong(typedStoreKey.key, typedStoreKey.defaultValue as Long)
            is String -> sharedPreferences.getString(typedStoreKey.key, "")
            is Float ->sharedPreferences.getFloat(typedStoreKey.key, typedStoreKey.defaultValue as Float)
            is Boolean -> sharedPreferences.getBoolean(typedStoreKey.key, typedStoreKey.defaultValue as Boolean)
            is List<*> -> {
                @Suppress("UNCHECKED_CAST")
                sharedPreferences.getStringSet(typedStoreKey.key, typedStoreKey.defaultValue as Set<String>)
            }
            else -> {

            }
        }
    }

     fun <T> write(typedStoreKey: TypeStoreKey<T>, value: T?) {
        val editor = sharedPreferences.edit()
        if (value == null) {
            editor.remove(typedStoreKey.key)
            return
        }
        when (value) {
            is Int -> editor.putInt(typedStoreKey.key, value as Int)
            is Long -> editor.putLong(typedStoreKey.key, value as Long)
            is String -> editor.putString(typedStoreKey.key, value as String)
            is Float -> editor.putFloat(typedStoreKey.key, value as Float)
            is Boolean -> editor.putBoolean(typedStoreKey.key, value as Boolean)
            is List<*> -> {
                @Suppress("UNCHECKED_CAST")
                editor.putStringSet(typedStoreKey.key, value as Set<String>)
            }
        }
        editor.apply()
    }
}

data class TypeStoreKey<T>(val key:String, var defaultValue: T?) {}

class PrefsKeys {
    companion object {
        val selectedDeviceMac = TypeStoreKey<String>("devicesList", defaultValue = null)
    }
}
