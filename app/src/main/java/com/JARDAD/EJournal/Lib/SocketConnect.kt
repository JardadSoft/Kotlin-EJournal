package com.JARDAD.EJournal.Lib

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

    var input: Receive? = null
    var output: Send? = null

    fun Connect() {

        thread {

            try {

                Log.d(tag, "Connecting...")

                socket = Socket("200.0.0.2", 10101)

                input = object : Receive(socket!!) {
                    override fun on(json: JSONObject) {

                    }

                    override fun onError() {

                    }

                }

                input?.begin()

                output = object : Send(socket!!) {
                    override fun on(json: JSONObject) {

                    }

                    override fun onError() {

                    }

                }

                Log.i(tag, "Connected!")

            } catch (ex: Exception) {
                Log.e(tag, "Connect", ex)
            }


            val json = JSONObject()

            json.put("Company", "JARDAD")

            output?.emit(json)
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

    fun Send(){

        val json = JSONObject()

        json.put("Name", "صدام المعمري")
        json.put("Desc", "JARDAD")

        output?.emit(json)
    }

    abstract fun onReceive(key: String, jaon: JSONObject?)

    fun Close() {
        socket?.close()
    }
}