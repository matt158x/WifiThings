package com.example.wifithings.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.wifithings.data.model.Device
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

@Singleton
class AppDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        private val DEVICES_KEY = stringPreferencesKey("devices")
    }

    suspend fun addDevice(newDevice: Device) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[DEVICES_KEY]?.let {
                Json.decodeFromString<List<Device>>(it)
            } ?: emptyList()

            val updatedList = currentList + newDevice
            preferences[DEVICES_KEY] = Json.encodeToString(updatedList)
        }
    }

    suspend fun removeDevice(device: Device) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[DEVICES_KEY]?.let {
                Json.decodeFromString<List<Device>>(it)
            } ?: emptyList()

            val updatedList = currentList.filterNot { it.id == device.id }
            preferences[DEVICES_KEY] = Json.encodeToString(updatedList)
        }
    }

    fun getDevices(): Flow<List<Device>> {
        return context.dataStore.data.map { preferences ->
            preferences[DEVICES_KEY]?.let { Json.decodeFromString<List<Device>>(it) } ?: emptyList()
        }
    }

}