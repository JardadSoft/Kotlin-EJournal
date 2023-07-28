package com.JARDAD.EJournal.Lib

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.net.Socket
import kotlin.concurrent.thread

abstract class Receive(val socket: Socket) {

    private val tag = "#JARDAD #" + this.javaClass.simpleName

    private val input = socket.getInputStream()

    abstract fun on(json: JSONObject)
    abstract fun onError()

    private var buffer = ByteArray(1)

    private var reader = 0

    private val bytes = ArrayList<Byte>()

    fun begin() {

        thread {

            while (socket.isConnected) {

                reader = input.read(buffer)

                if (reader > 0) {

                    if (buffer[0] == 0XFF.toByte()) {

                        val data = bytes.toByteArray()
                        bytes.clear()

                        thread {

                            try {

                                val bytes = Base64.decode(data, Base64.DEFAULT)

                                val tex_json = bytes.toString(Charsets.UTF_8)

                                val json = JSONObject(tex_json)

                                Log.v(tag, "<< $json")

                                on(json)

                            } catch (ex: Exception) {
                                Log.e(tag, "<<", ex)
                                onError()
                            }

                        }

                    } else {
                        bytes.add(buffer[0])
                    }

                } else {
                    Log.e(tag, "Reader = 0")
                }
            }
        }

    }
}