package com.JARDAD.EJournal.Lib

import android.util.Base64
import android.util.Log
import com.JARDAD.EJournal.JCipher
import org.json.JSONObject
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.spec.EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.UUID
import javax.crypto.Cipher
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

                        if (json.has("Key")) {

                            val key_text = json.getString("Key")

                            val key_bytes = Base64.decode(key_text, Base64.DEFAULT)

                            val rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")

                            val keySpec: EncodedKeySpec = X509EncodedKeySpec(key_bytes)
                            val keyFactory = KeyFactory.getInstance("RSA")
                            val key = keyFactory.generatePublic(keySpec)

                            rsa.init(Cipher.ENCRYPT_MODE, key)

                            val k = UUID.randomUUID().toString().replace("-", "")

                            val encryptedBytes =
                                rsa.doFinal(k.toByteArray())

                            Log.i(tag, k)

                            var json = JSONObject()
                            json.put("Key", Base64.encodeToString(encryptedBytes, Base64.DEFAULT))

                            output!!.emit(json)

                            val cipher = JCipher(k.uppercase())
                            output!!.cipher = cipher

                            json = JSONObject()
                            json.put("Name", "-JARDAD-")
                            json.put("Description", "-صدام المعمري-")

                            output!!.emit(json)
                        }

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

//
//            val json = JSONObject()
//
//            json.put("Company", "JARDAD")
//
//            output?.emit(json)
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

    fun Send() {

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