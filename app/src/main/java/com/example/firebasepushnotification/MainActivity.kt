package com.example.firebasepushnotification

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.firebasepushnotification.FirebaseService.Companion.token
import com.example.firebasepushnotification.databinding.ActivityMainBinding

import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic2"

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = token
            binding.etToken.setText(token)
        }
//    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            FirebaseService.token = it.token
//            binding.etToken.setText(it.token)
//        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

//
//        Log.e("newToken", token.toString());
////Add your token in your sharepreferences.
//        FirebaseService.token= token
//        binding.etToken.setText(token)
//        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
      // f92BTqLTTJaQ8m8RXBwpww:APA91bHY_Twi1ZAiSIDWbW7xqDKaO_Dn5Q-tKVNVqqcyWi71gSlTPMJtvRqLB-45fHWGCmkdYo4JhF6oSLyxlOxAKQ8Cke2MPVyOfI88BuFMPH9vGBLNxBMGzhd1KhrclNMxszIMEntp


        binding.btnSend.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            val recipientToken = binding.etToken.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}