package com.maulana.weathermobile.util

import android.content.SharedPreferences
import androidx.core.content.edit
import com.maulana.weathermobile.domain.model.Coord

private const val PREF_TOKEN = "token"
private const val PREF_ACT_WR_CODE = "wr_code"
private const val PREF_ACT_WR_NAME = "wr_name"
private const val PREF_USER_NAME = "username"
private const val PREF_ACT_PRD_ID = "active_product_id"
private const val PREF_LOCATION = "location"

operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { putString(key, value) }
        is Int -> edit { putInt(key, value) }
        is Boolean -> edit { putBoolean(key, value) }
        is Float -> edit { putFloat(key, value) }
        is Long -> edit { putLong(key, value) }
        is HashSet<*> -> edit { putStringSet(key, value as MutableSet<String>?) }
        else -> throw UnsupportedOperationException("Pref factory set not yet implemented")
    }
}

inline operator fun <reified T : Any?> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T? =
    when (T::class) {
        String::class -> getString(key, defaultValue as? String ?: "") as? T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as? T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as? T?
        Float::class -> getFloat(key, defaultValue as? Float ?: -1F) as? T?
        Long::class -> getLong(key, defaultValue as? Long ?: -1L) as? T?
        HashSet::class -> getStringSet(
            key,
            defaultValue as? HashSet<String> ?: hashSetOf<String>()
        ) as? T?

        else -> throw UnsupportedOperationException("Pref factory get not yet implemented")
    }

fun SharedPreferences.saveToken(token: String) {
    set(PREF_TOKEN, token)
}

fun SharedPreferences.saveUsername(username: String) {
    set(PREF_USER_NAME, username)
}

fun SharedPreferences.saveActiveProductId(productId: String) {
    set(PREF_ACT_PRD_ID, productId)

}

fun SharedPreferences.saveLocation(coordList: List<Coord>) {
    val pairListString = coordList.joinToString(";") { "${it.lat}-${it.lon}" }

    set(PREF_LOCATION, pairListString)
}

fun SharedPreferences.loadLocation(): List<Coord>? {
    val pairListString: String? = get(PREF_LOCATION)

    return pairListString?.let {
        it.split(";").mapNotNull { pairString ->
            val parts = pairString.split("-")
            if (parts.size == 2) {
                Coord(
                    parts[0].toDoubleOrNull() ?: return@mapNotNull null,
                    parts[1].toDoubleOrNull() ?: return@mapNotNull null
                )
            } else {
                null // If the data format is incorrect, skip this pair
            }
        }
    }
}


fun SharedPreferences.loadUsername(): String? = get(PREF_USER_NAME)

fun SharedPreferences.loadToken(): String? = get(PREF_TOKEN)

fun SharedPreferences.loadActiveProductId(): String? = get(PREF_ACT_PRD_ID)


