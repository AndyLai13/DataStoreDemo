package com.andylai.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.myPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val keyBoolean = booleanPreferencesKey("key_boolean")
    private val keyInt = intPreferencesKey("key_int")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.setBoolean).setOnClickListener {
            lifecycleScope.launch {
                setBoolean()
            }
        }

        findViewById<Button>(R.id.getBoolean).setOnClickListener {
            lifecycleScope.launch {
                (it as Button).text = getBoolean().toString()
            }
        }

        findViewById<Button>(R.id.setInt).setOnClickListener {
            lifecycleScope.launch {
                setInt()
            }
        }

        findViewById<Button>(R.id.getInt).setOnClickListener {
            lifecycleScope.launch {
                (it as Button).text = getInt().toString()
            }
        }

        findViewById<Button>(R.id.getAsync).setOnClickListener {
            lifecycleScope.launch {
                val startTime = System.currentTimeMillis()
                val a = lifecycleScope.async { getDataDeferred1Second() }
                val b = lifecycleScope.async { getDataDeferred2Seconds() }
                val sum = a.await() + b.await()
                Log.d("Andy", "sum = $sum, elapsed time: ${System.currentTimeMillis() - startTime}")
                (it as Button).text = sum.toString()
            }
        }
    }

    private suspend fun getDataDeferred1Second(): Int {
        delay(1000)
        return 1
    }
    private suspend fun getDataDeferred2Seconds(): Int {
        delay(2000)
        return 2
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