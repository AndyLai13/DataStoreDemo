package com.andylai.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.myPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val keyBoolean = booleanPreferencesKey("key_boolean")
    private val keyInt = intPreferencesKey("key_int")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.setBoolean).setOnClickListener {
            GlobalScope.launch {
                setBoolean()
            }
        }

        findViewById<Button>(R.id.getBoolean).setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                (it as Button).text = getBoolean().toString()
            }
        }

        findViewById<Button>(R.id.setInt).setOnClickListener {
            GlobalScope.launch {
                setInt()
            }
        }

        findViewById<Button>(R.id.getInt).setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                (it as Button).text = getInt().toString()
            }
        }
    }

    suspend fun setBoolean() {
        applicationContext.myPreferencesDataStore.edit {
            it[keyBoolean] = true
        }
    }

    suspend fun getBoolean(): Boolean {
        return applicationContext.myPreferencesDataStore.data.map {
            it[keyBoolean] ?: false
        }.first()
    }

    suspend fun setInt() {
        applicationContext.myPreferencesDataStore.edit {
            it[keyInt] = 1
        }
    }

    suspend fun getInt(): Int {
        return applicationContext.myPreferencesDataStore.data.map {
            it[keyInt] ?: -1
        }.first()
    }
}