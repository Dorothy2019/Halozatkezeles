package hu.bme.aut.android.labyrinth.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class LabyrinthAPI {

    companion object {
        private const val BASE_URL = "https://aut-android-labyrinth.herokuapp.com/api"
        private const val UTF_8 = "UTF-8"
        private const val TAG = "Network"
        private const val RESPONSE_ERROR = "ERROR"
    }

    private fun String.encode() = URLEncoder.encode(this, UTF_8)

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private fun httpGet(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        //The execute call blocks the thread
        val response = client.newCall(request).execute()
        return response.body?.string() ?: "EMPTY"
    }

    fun moveUser(username: String, direction: Int): String {
        return try {
            val moveUserUrl = "$BASE_URL/step/${username.encode()}/$direction"

            Log.d(TAG, "Call to $moveUserUrl")
            httpGet(moveUserUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            RESPONSE_ERROR
        }
    }


    fun writeMessage(username: String, message: String): String {
        return try {
            val writeMessageUrl = "$BASE_URL/message/${username.encode()}/${message.encode()}"

            Log.d(TAG, "Call to $writeMessageUrl")
            httpGet(writeMessageUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            RESPONSE_ERROR
        }
    }

}