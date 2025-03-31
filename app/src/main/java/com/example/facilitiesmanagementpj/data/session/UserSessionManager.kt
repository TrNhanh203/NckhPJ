package com.example.facilitiesmanagementpj.data.session

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by preferencesDataStore(name = "user_prefs")

object UserSessionManager {
    private val gson = Gson()
    private val KEY_USER = stringPreferencesKey("logged_in_user")

    suspend fun saveUser(context: Context, user: TaiKhoanWithRole) {
        val json = gson.toJson(user)
        context.userDataStore.edit { prefs ->
            prefs[KEY_USER] = json
        }
    }

    suspend fun clearUser(context: Context) {
        context.userDataStore.edit { prefs ->
            prefs.remove(KEY_USER)
        }
    }

    fun getUserFlow(context: Context): Flow<TaiKhoanWithRole?> {
        return context.userDataStore.data.map { prefs: Preferences ->
            prefs[KEY_USER]?.let { json ->
                gson.fromJson(json, TaiKhoanWithRole::class.java)
            }
        }
    }
}
