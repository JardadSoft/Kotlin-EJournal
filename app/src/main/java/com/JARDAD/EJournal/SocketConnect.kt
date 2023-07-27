package com.JARDAD.EJournal

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import kotlin.concurrent.thread

abstract class SocketConnect {

    private val tag = "#JARDAD #" + this.javaClass.simpleName

    var socket: Socket? = null

    fun Connect() {

        thread {

            Log.d(tag, "Connecting...")

            socket = Socket("200.0.0.2", 10101)

            Log.i(tag, "Connected!")

            val json = JSONObject()

            json.put("Name", "JARDAD")

            val bytes = json.toString().toByteArray(Charsets.UTF_8)

            val text_64 = Base64.encodeToString(bytes, Base64.DEFAULT)
            Log.d(tag, "text_64 = $text_64")

            val output = socket?.getOutputStream()

             output?.write(text_64.toByteArray())

            output?.write(byteArrayOf(0xFF.toByte()))
        }
//        val output = socket?.getOutputStream()
//
//        // Send a message to the server
//        val message = "Hello, server!"
//        output.write(message.toByteArray())
//
//        // Receive a message from the server
//        val buffer = ByteArray(1024)
//        val count = input.read(buffer)
//        val response = String(buffer, 0, count)
//        println("Server response: $response")

        // Close the socket

    }


    abstract fun onReceive(key: String, jaon: JSONObject?)

    private fun receive() {

        val input = socket?.getInputStream()
        val buffer = BufferedReader(InputStreamReader(input))

        thread {

            while (socket?.isConnected == true) {

                val line = buffer.readLine()
            }

        }

    }

    fun Close() {
        socket?.close()
    }
}