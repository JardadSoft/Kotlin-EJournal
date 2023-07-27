package com.JARDAD.EJournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val socket = object : SocketConnect(){
            override fun onReceive(key: String, jaon: JSONObject?) {
                TODO("Not yet implemented")
            }

        }.Connect()
    }
}