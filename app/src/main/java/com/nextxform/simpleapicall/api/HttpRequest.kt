package com.nextxform.simpleapicall.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class HttpRequest(
    val requestUrl: String,
    val requestParams: HashMap<String, String>,
    val callBack: HTTPCallback
) {
    suspend fun performCall() {
        try {
            withContext(Dispatchers.IO) {
                //Create Connection
                val url = URL(requestUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 60000
                connection.readTimeout = 30000
                connection.requestMethod = "GET"
                connection.doInput = true
                connection.doOutput = false
                connection.setRequestProperty("Accept", "*")

                // Send Request
//                val os = connection.outputStream
//                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
//                os.use {
//                    writer.use {
//                        writer.write(getRequestData(requestParams))
//                        writer.flush()
//                    }
//                }

                // Get Response
                val responseCode = connection.responseCode
                val sb = StringBuilder()
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    inputStream.use {
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        sb.append(reader.use(BufferedReader::readLine))
                    }
                }
                if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    val inputStream = connection.inputStream
                    inputStream.use {
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        sb.append(reader.use(BufferedReader::readLine))
                    }
                }

                if(responseCode == HttpURLConnection.HTTP_OK){
                    callBack.processFinished(sb.toString())
                }else{
                    callBack.processFailed(responseCode, sb.toString())
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.message.orEmpty())
        }
    }

    suspend fun getRequestData(param: HashMap<String, String>): String {
        val sb = StringBuilder()

        param.entries.forEach { entry ->
            if (sb.isNotEmpty()) sb.append("&")
            sb.append("${entry.key}=${entry.value}")
            Log.d("Params", "${entry.key}=${entry.value}")
        }

        return sb.toString()
    }
}