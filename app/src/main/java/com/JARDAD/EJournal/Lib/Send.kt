package com.JARDAD.EJournal.Lib

import android.util.Base64
import android.util.Log
import com.JARDAD.EJournal.JCipher
import org.json.JSONObject
import java.net.Socket
import kotlin.concurrent.thread

abstract class Send(val socket: Socket) {

    private val tag = "#JARDAD #" + this.javaClass.simpleName

    private val queue = ArrayList<JSONObject>()

    private var isEmitting = false

    private val output = socket.getOutputStream()

    var cipher: JCipher? = null

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

                var text_64= ""

                if (cipher == null) {

                    text_64 = Base64.encodeToString(json.toString().toByteArray(Charsets.UTF_8), Base64.DEFAULT)

                } else {
                    text_64 = cipher!!.Encrypt(json)
                }

                Log.d(tag,text_64)

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