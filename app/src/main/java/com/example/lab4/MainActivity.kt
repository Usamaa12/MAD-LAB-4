package com.example.lab4

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferencesManager = PreferencesManager(context = this)
            FavoriteColorScreen(preferencesManager = preferencesManager)
        }
    }
}

@Composable
fun FavoriteColorScreen(preferencesManager: PreferencesManager) {
    var key by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = key,
            onValueChange = { key = it },
            label = { Text("Key") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Value") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            preferencesManager.saveData(key = key, value = value)
            Toast.makeText(context, "$value is favourite color", Toast.LENGTH_LONG).show()
            Log.d("ButtonClicked", "Favorite color saved: Key=$key, Value=$value")
        }) {
            Text("Save Favorite Color")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for entering the key
        var retrieveKey by remember { mutableStateOf("") }
        TextField(
            value = retrieveKey,
            onValueChange = { retrieveKey = it },
            label = { Text("Enter Key to Retrieve Color") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Button to retrieve color for the entered key
        Button(onClick = {
            val retrievedColor = preferencesManager.getData(retrieveKey, "No color found for this key")
            Toast.makeText(context, "Color for $retrieveKey is $retrievedColor", Toast.LENGTH_LONG).show()
            Log.d("RetrieveButtonClicked", "Retrieved color for key $retrieveKey: $retrievedColor")
        }) {
            Text("Retrieve Color")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewFavoriteColorScreen() {
    val preferencesManager = PreferencesManager(androidx.compose.ui.platform.LocalContext.current)
    FavoriteColorScreen(preferencesManager = preferencesManager)
}