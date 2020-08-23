package com.tfg.workoutagent.vo.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject

fun sendNotification(context: Context, title: String, body: String, topic: String) {

    val FCM_API = "https://fcm.googleapis.com/fcm/send"
    val serverKey =
        "key=" + "AAAARqwpjPA:APA91bG_ZLDaldHd9F4XZajW0Fth4iLyiJ0dHy-vGaHz0gnTPpnDd7ln0sNg4t8lRxhH7wMMNvBNA-rmc3QrrGkPEB7fUTH6X-ipQCW8ygjPxd_AwN2rFa6qxTRdoCG8Ylpmmj771b6Z"
    val contentType = "application/json"

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }
    val notification = JSONObject()
    val notifcationBody = JSONObject()

    try {
        notifcationBody.put("title", title)
        notifcationBody.put("message", body)
        notification.put("to", topic)
        notification.put("data", notifcationBody)
    } catch (e: JSONException) {
    }

    val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
        Response.Listener<JSONObject> { response ->
        },
        Response.ErrorListener {
        }) {

        override fun getHeaders(): Map<String, String> {
            val params = HashMap<String, String>()
            params["Authorization"] = serverKey
            params["Content-Type"] = contentType
            return params
        }
    }
    requestQueue.add(jsonObjectRequest)
}

/*
    private fun sendNotification(notification: JSONObject) {
        Log.i("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
            },
            Response.ErrorListener {
                Toast.makeText(this.context, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun createMsg(){

        val topic = "/topics/admin" //topic has to match what the receiver subscribed to
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        //FirebaseMessaging.getInstance().subscribeToTopic(topic)

        val notification = JSONObject()
        val notifcationBody = JSONObject()

        try {
            notifcationBody.put("title", "Firebase Notification")
            notifcationBody.put("message", "Este mensaje se ha enviado :D")
            notification.put("to", topic)
            notification.put("data", notifcationBody)
            Log.e("TAG", "try")
        } catch (e: JSONException) {
            Log.e("TAG", "onCreate: " + e.message)
        }

        sendNotification(notification)
    }

 */