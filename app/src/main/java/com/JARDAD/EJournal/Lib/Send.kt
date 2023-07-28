package com.JARDAD.EJournal.Lib

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.net.Socket
import kotlin.concurrent.thread

abstract class Send(val socket: Socket) {

    private val tag = "#JARDAD #" + this.javaClass.simpleName

    private val queue = ArrayList<JSONObject>()

    private var isEmitting = false

    private val output = socket.getOutputStream()

    abstract fun on(json: JSONObject)
    abstract fun onError()
    private fun emitted(json: JSONObject) {

        Log.v(tag, ">> $json")

        queue.remove(json)

        thread {
            on(json)
        }

        isEmitting = false

        if (queue.size > 0)
            emitting(queue[0])
    }

    fun emit(json: JSONObject) {

        queue.add(json)

        if (!isEmitting)
            emitting(queue[0])
    }

    private fun emitting(json: JSONObject) {

        if (isEmitting) return

        isEmitting = true

        thread {

            try {

                val bytes = json.toString().toByteArray()

                val text_64 = Base64.encodeToString(bytes, Base64.DEFAULT)

                output.write(text_64.toByteArray())
                output.write(byteArrayOf(0XFF.toByte()))

                emitted(json)

            } catch (ex: Exception) {
                Log.e(tag, ">>", ex)
                onError()
            }

        }

    }
}