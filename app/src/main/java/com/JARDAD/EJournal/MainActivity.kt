package com.JARDAD.EJournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.JARDAD.EJournal.Lib.SocketConnect
import com.JARDAD.EJournal.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v = ActivityMainBinding.inflate(layoutInflater)
        setContentView(v.root)


        val a = ' '.toByte()

        val socket = object : SocketConnect() {
            override fun onReceive(key: String, jaon: JSONObject?) {
                TODO("Not yet implemented")
            }
        }

        socket.Connect()

        v.btn.setOnClickListener {

            socket.Send()

        }

    }
}